package br.com.meli.PIFrescos.service;

import br.com.meli.PIFrescos.controller.dtos.ProductWarehousesDTO;
import br.com.meli.PIFrescos.controller.dtos.WarehouseDTO;
import br.com.meli.PIFrescos.controller.dtos.WarehouseQuantityDTO;
import br.com.meli.PIFrescos.models.Batch;

import br.com.meli.PIFrescos.models.Product;
import br.com.meli.PIFrescos.models.StorageType;
import br.com.meli.PIFrescos.repository.BatchCustomRepository;

import br.com.meli.PIFrescos.repository.BatchRepository;
import br.com.meli.PIFrescos.repository.ProductRepository;
import br.com.meli.PIFrescos.service.interfaces.IBatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;

import javax.persistence.EntityNotFoundException;
import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.contains;

@Service
public class BatchServiceImpl implements IBatchService {

    @Autowired
    private BatchRepository batchRepository;

    @Autowired
    private BatchCustomRepository batchCustomRepository;

    @Autowired
    private ProductService productService;


    /**
     * @param id batch id
     * @return Batch
     * @author Antonio Hugo
     */
    @Override
    public Batch findBatchById(Integer id) {
        return batchRepository.findById(id).orElseThrow(() -> new RuntimeException("Not Found"));
    }

    @Override
    public Batch findByBatchNumber(Integer id) {
        return batchRepository.findByBatchNumber(id);
    }

    /**
     * @param productId  id do produto
     * @return List<Batch>
     * @author Antonio Hugo
     */
    @Override
    public List<Batch> findBatchesByProduct(Integer productId) {
        return this.batchRepository.findBatchesByProduct_ProductId(productId);
    }

    /**
     * @param batch recebe um Batch
     * @return boolean
     */
    @Override
    public boolean checkIfBatchExists(Batch batch) {
        return batchRepository.existsBatchByBatchNumber(batch.getBatchNumber());
    }

    @Override
    public List<Batch> findBatchesByDueDateGreaterThanEqualAndSectorEquals(Integer expiringLimit, Integer sectionId) {
        LocalDate maxDueDate = LocalDate.now().plusDays(expiringLimit);
        List<Batch> batches = batchRepository.findBatchesByDueDateGreaterThanEqualAndSectorEquals(maxDueDate, sectionId);

        return batches;
    }

    /**
     * Atualiza o a CurrentQuantity do Batch de acordo com a quantidade do ProductCart enviada por parametro.
     * @param quantity, batch
     * @return  Batch
     * @author Ana Preis
     */
    @Transactional
    @Override
    public Batch updateCurrentQuantity(Integer quantity, Batch batch){
        Optional<Batch> batchOptional = batchRepository.findById(batch.getBatchNumber());
        if (batchOptional.isEmpty()){
            throw new EntityNotFoundException("Can`t update quantity, Batch not found");
        }
        Batch newBatch = batchOptional.get();
        newBatch.setCurrentQuantity(newBatch.getCurrentQuantity() - quantity);
        return newBatch;
    }

    /**
     * Procura a lista de batches ordenada por Lote (L), CurrentQuantity(C) ou DueDate(F).
     * Se não for passado na query nenhum destes 3, retorna uma exceção.
     * @author Ana Preis
     */
    public List<Batch> findBatchesByProductOrderBy(Integer id, String orderBy){
        if(orderBy.equals("")){
            return batchRepository.findBatchesByProduct_ProductId(id);
        }
        if(orderBy.equalsIgnoreCase("L")){
            return batchRepository.findBatchesByProduct_ProductIdAndOrderByBatchNumber(id);
        }
        if(orderBy.equalsIgnoreCase("C")){
            return batchRepository.findBatchesByProduct_ProductIdAndOrderByCurrentQuantity(id);
        }
        if(orderBy.equalsIgnoreCase("F")){
            return batchRepository.findBatchesByProduct_ProductIdAndOrderBOrderByDueDate(id);
        }
        else {
            throw new RuntimeException("Invalid query for OrderBy");
        }
    }


    /**
     * Procura a lista de batches ordenada de acordo com o número de dias passados, com a categoria do produto,
     * podendo ordenar de forma crescente ou decrescente.
     * Se alguma query de categoria ou ordenação for inválida, retorna uma exceção.
     * Se não for passado na query nenhum destes 3, retorna a lista toda.
     * @author Ana Preis
     */
    @Override
    public List<Batch> findBatchesOrderBy(Integer days, String category, String order){

        StorageType storageType;

        if(days == null && category == null && order == null ){
            return batchRepository.findAll();
        }

        // Verifica se category é nula ou se é inválida.
        if (category == null) {
            storageType = null;
        } else if (category.equalsIgnoreCase("FRESH") || category.equalsIgnoreCase("FROZEN")
                || category.equalsIgnoreCase("REFRIGERATED")) {
            storageType = StorageType.valueOf(category.toUpperCase(Locale.ROOT));
        } else {
            throw new RuntimeException("Invalid query for category");
        }

        // Verifica se order é nula ou se é inválida. Se for inválida não faz a busca e retorna exceção.
        if((order == null || order.equalsIgnoreCase("asc") || order.equalsIgnoreCase("desc"))  && days != null){
            LocalDate maxDueDate = LocalDate.now().plusDays(days);
            return batchCustomRepository.find(maxDueDate, storageType, order);
        } else if (days == null){
            return batchCustomRepository.find(null, storageType, order);
        } else {
            throw new RuntimeException("Invalid query for order");
        }
    }

  
    public ProductWarehousesDTO getQuantityProductByWarehouse(Integer productId){
        Product product = productService.findProductById(productId);

        List<WarehouseQuantityDTO> quantityProductByWarehouse = batchRepository.getQuantityProductByWarehouse(productId);
        AtomicReference<Boolean> hasQuantity = new AtomicReference<>(false);

        List<WarehouseDTO> collect = quantityProductByWarehouse.stream()
                .map(warehouseQuantity -> {
                    hasQuantity.set(warehouseQuantity.getTotalquantity().compareTo(BigInteger.ZERO) > 0);

                    return new WarehouseDTO(
                            warehouseQuantity.getWarehousecode(),
                            Integer.parseInt(warehouseQuantity.getTotalquantity().toString()));
                })
                .collect(Collectors.toList());

        if(!hasQuantity.get())
            throw new EntityNotFoundException("Product do not have quantity");

        return new ProductWarehousesDTO(
                product.getProductId(),
                product.getProductName(),
                collect);
    }


}
