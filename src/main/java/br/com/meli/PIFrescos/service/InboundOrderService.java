package br.com.meli.PIFrescos.service;

import br.com.meli.PIFrescos.models.InboundOrder;
import br.com.meli.PIFrescos.models.Product;
import br.com.meli.PIFrescos.models.Section;
import br.com.meli.PIFrescos.repository.InboundOrderRepository;
import br.com.meli.PIFrescos.repository.ProductRepository;
import br.com.meli.PIFrescos.repository.SectionRepository;
import br.com.meli.PIFrescos.service.interfaces.IBatchService;
import br.com.meli.PIFrescos.service.interfaces.IInboundOrderService;
import br.com.meli.PIFrescos.service.interfaces.ISectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InboundOrderService implements IInboundOrderService {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    InboundOrderRepository inboundOrderRepository;

    @Autowired
    IBatchService batchService;

    @Autowired
    ISectionService sectionService;


    /**
     * Salva uma inbound order. Antes de salvar, é verificado se a Section correspondente ainda possui capacidade
     * para armazenar.
     * @return List<inboundOrder>
     * @author Julio César Gama
     */
    @Override
    public List<InboundOrder> getAll() {
        return inboundOrderRepository.findAll();
    }

    /**
     * Salva uma inbound order. Antes de salvar, é verificado se a Section correspondente ainda possui capacidade
     * para armazenar, se o produto está cadastrado.
     * @param inboundOrder
     * @return inboundOrder
     * @author Felipe Myose
     */
    @Override
    public InboundOrder save(InboundOrder inboundOrder) {


        // Busca os valores dos produtos nos batches e os atualiza
        inboundOrder = checkProductsAndSectionFromBatchListAndUpdateValues(inboundOrder);

        // Verificar se os produtos correspondem à seção correta de armazenamento
        checkIfSectionsMatches(inboundOrder);

        // verificar se o setor comporta a quantidade
        Integer inboundOrderSectionCode = inboundOrder.getSection().getSectionCode();
        Integer inboundOrderQuantity = calculateVolume(inboundOrder);
        sectionService.updateCapacity(inboundOrderSectionCode, inboundOrderQuantity);

        // salvar previamente para ter o id do inbound order - este valor deve entrar na lista de batch
        InboundOrder savedInboundOrder = inboundOrderRepository.save(inboundOrder);
        savedInboundOrder.getBatchStock().stream().forEach(batch -> {
            batch.setInboundOrder(savedInboundOrder);
        });

        return inboundOrderRepository.save(savedInboundOrder);
    }

    /**
     * Atualiza uma entrada existente. Neste update, será necessário verificar se a entrada é valida,
     * se é possível adicionar ou não a ordem.
     * @param inboundOrderNumber
     * @param newInboundOrderValues
     * @return inboundOrder
     */
    @Override
    public InboundOrder update(Integer inboundOrderNumber, InboundOrder newInboundOrderValues) {
        InboundOrder inboundOrder = inboundOrderRepository.getByOrderNumber(inboundOrderNumber);

        if(inboundOrder == null) {
            throw new RuntimeException("Ordem solicitada não existe.");
        }
        // verificar se os batch existem - se não existir, mandar uma mensagem avisando a criaçao de um novo batch
        checkIfBatchesExist(newInboundOrderValues);
        // verificar se os produtos existem
        newInboundOrderValues = checkProductsAndSectionFromBatchListAndUpdateValues(newInboundOrderValues);
        // Verificar se os produtos correspondem à seção correta de armazenamento
        checkIfSectionsMatches(inboundOrder);
        // verificar se o setor comporta a quantidade
        // se o batch existe, devemos atualizar o valor de acordo com a diferença que acontecer
        Integer inboundOrderSectionCode = newInboundOrderValues.getSection().getSectionCode();
        Integer inboundOrderQuantity = calculateVolume(inboundOrder);
        Integer newInboundOrderQuantity = calculateVolume(newInboundOrderValues);
        sectionService.updateCapacity(inboundOrderSectionCode, newInboundOrderQuantity - inboundOrderQuantity);

        // atualizar valores para salvar
        inboundOrder.setOrderDate(newInboundOrderValues.getOrderDate());
        inboundOrder.setSection(newInboundOrderValues.getSection());
        inboundOrder.setBatchStock(newInboundOrderValues.getBatchStock());

        // salvar previamente para ter o id do inbound order - este valor deve entrar na lista de batch
        InboundOrder savedInboundOrder = inboundOrderRepository.save(inboundOrder);
        savedInboundOrder.getBatchStock().stream().forEach(batch -> {
            batch.setInboundOrder(savedInboundOrder);
        });

        return inboundOrderRepository.save(savedInboundOrder);
    }

    /**
     * Remove um inbound order. Quando removido, atualizar a capacidade do setor correspondente -
     * subtraindo a quantidade correspondente.
     * @param inboundOrderId
     * @author Felipe Myose
     */
    @Override
    public void delete(Integer inboundOrderId) {
        InboundOrder inboundOrder = inboundOrderRepository.getById(inboundOrderId);

        inboundOrderRepository.deleteById(inboundOrderId);
        sectionService.updateCapacity(inboundOrder.getSection().getSectionCode(),
                -calculateVolume(inboundOrder));
    }

    /**
     * Calcula o volume do inbound order.
     * @param inboundOrder
     * @return integer
     * @author Felipe Myose
     */
    private Integer calculateVolume(InboundOrder inboundOrder) {
        if (inboundOrder.getBatchStock().size() == 0) {
            return 0;
        }
        return inboundOrder.getBatchStock().stream().mapToInt(batch -> batch.getCurrentQuantity()).sum();
    }

    /**
     * Verifica se o produto existe. Se sim, preenche os atributos de product de Batch.
     * Caso o productId não seja encontrado, retornar uma mensagem de erro.
     * @param inboundOrder
     * @return inboundOrder
     * @author Felipe Myose
     *
     *  Verifica se a seção existe. Se sim, preenche os atributos de seção
     *  Caso o sectionCode não seja encontrado, retornar uma mensagem de erro.
     *  @param inboundOrder
     *  @return inboundOrder
     *  @author Julio César Gama
     */
    private InboundOrder checkProductsAndSectionFromBatchListAndUpdateValues(InboundOrder inboundOrder) {
        // se o inboundOrder estiver vazio
        if (inboundOrder.getBatchStock().size() == 0) {
            return inboundOrder;
        }
        inboundOrder.getBatchStock().forEach(batch -> {
            Optional<Product> product = productRepository.findById(batch.getProduct().getProductId());
            if (product.isEmpty() ) {
                throw new RuntimeException("Produto não existe.");
            }
            batch.setProduct(product.get());
        });

        Optional<Section> section = sectionService.findById(inboundOrder.getSection().getSectionCode());
        if(section.isEmpty()){
            throw new RuntimeException("Seção não existente.");
        }

        inboundOrder.setSection(section.get());

        return inboundOrder;
    }

    /**
     * Verifica se a seção dos produtos é compatível com o local de armazenamento
     * @param inboundOrder
     * @return void
     * @author Julio César Gama
     */

    private void checkIfSectionsMatches(InboundOrder inboundOrder){

        inboundOrder.getBatchStock().forEach(batch ->{
            if(!batch.getProduct().getProductType().equals(inboundOrder.getSection().getStorageType())){
                throw new RuntimeException("Tipo de produto e local de armazenamento incompatíveis.");
            }
        });

    }

    /**
     * Verifica se o batch de inboundOrder existe
     * @param inboundOrder
     * @return
     */

    private boolean checkIfBatchesExist(InboundOrder inboundOrder) {
        if (inboundOrder.getBatchStock().size() == 0) {
            throw new RuntimeException("Batch não existe. Lista de Batch vazia");
        }
        inboundOrder.getBatchStock().forEach(batch -> {
            if(!batchService.checkIfBatchExists(batch)) {
                System.out.println("Batch não existe. Criando novo batch");
            }
        });
        return true;
    }
}
