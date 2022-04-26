package br.com.meli.PIFrescos.controller;

import br.com.meli.PIFrescos.dtos.InboundOrderUpdateDTO;
import br.com.meli.PIFrescos.models.Batch;
import br.com.meli.PIFrescos.models.InboundOrder;
import br.com.meli.PIFrescos.controller.dtos.InboundOrderDTO;
import br.com.meli.PIFrescos.models.Section;
import br.com.meli.PIFrescos.service.InboundOrderService;
import br.com.meli.PIFrescos.service.SectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Busca todos os InboundOrders existentes
 * para armazenar.
 * @return List<inboundOrder>
 * @author Julio César Gama
 */

@RestController
@RequestMapping("/fresh-products/inboundorder")
public class InBoundOrderController {

    @Autowired
    InboundOrderService service;

    @Autowired
    SectionService sectionService;

    @GetMapping("")
    public ResponseEntity<List<InboundOrder>> getInboundOrders(){
        return ResponseEntity.ok(service.getAll());
    }


    /**
     * Salva nova InboundOrder
     * @author Ana Preis
     */
    @PostMapping("")
    public ResponseEntity<List<Batch>> postInboundOrders(@RequestBody InboundOrderDTO orderDTO){
        InboundOrder order = InboundOrderDTO.convert(orderDTO);
        InboundOrder savedOrder = service.save(order);

        return new ResponseEntity(savedOrder.getBatchStock(), HttpStatus.CREATED);
    }

    /**
     * Atualiza InboundOrder existente
     * @author Ana Preis / Felipe Myose
     */
    @PutMapping("/{orderNumber}")
    public ResponseEntity<List<Batch>> putInboundOrders(@RequestBody InboundOrderUpdateDTO orderDTO, @PathVariable Integer orderNumber){
        InboundOrder order = InboundOrderUpdateDTO.convert(orderDTO);

        InboundOrder savedOrder = service.update(orderNumber, order);

        return new ResponseEntity(savedOrder.getBatchStock(), HttpStatus.CREATED);
    }

    /**
     * Cria uma nova Section, usado para o teste da Validation das Sections
     * @author Ana Preis
     */
    @PostMapping("/section")
    public ResponseEntity<Section> postSection(@Valid @RequestBody Section section){
        sectionService.createSection(section);

        return new ResponseEntity(section, HttpStatus.CREATED);
    }
}
