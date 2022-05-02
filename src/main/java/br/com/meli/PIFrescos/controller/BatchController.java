package br.com.meli.PIFrescos.controller;

import br.com.meli.PIFrescos.controller.dtos.BatchStockDTO;
import br.com.meli.PIFrescos.models.Batch;
import br.com.meli.PIFrescos.service.BatchServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/fresh-products/due-date")
public class BatchController {

    @Autowired
    BatchServiceImpl batchService;

    /**
     * Endpoint para listar os batches de acordo com a categoria (FRESH, REFRIGERATED, FROZEN), com a quantidade de dias
     * de hoje até a data de validade e ordenação crescente (asc) ou descrescente (desc).
     * @author Ana Preis
     */
    @GetMapping("/list")
    public ResponseEntity<List<BatchStockDTO>> getBatchesByCategory(@RequestParam(required = false) Integer days,
                                                                    @RequestParam(required = false) String category,
                                                                    @RequestParam(required = false) String order){

        List<Batch> batchList = batchService.findBatchesOrderBy(days, category, order);
        return ResponseEntity.ok(BatchStockDTO.convert(batchList));
    }

    /**
     * endpoint para listar os batches de um determinado setor - FRESH, REFRIGERATED, FROZEN
     * @author Felipe Myose
     */
    @GetMapping("")
    public ResponseEntity<List<BatchStockDTO>> getBatchesBySectionAndDueDateLessThan(@RequestParam Integer sectionId,
                                                                                     @RequestParam Integer expiringLimit) {
        List<Batch> batches = batchService.findBatchesByDueDateGreaterThanEqualAndSectorEquals(expiringLimit, sectionId);

        return new ResponseEntity<>(BatchStockDTO.convert(batches), HttpStatus.OK);
    }

}
