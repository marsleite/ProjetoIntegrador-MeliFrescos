package br.com.meli.PIFrescos.controller;

import br.com.meli.PIFrescos.controller.dtos.BatchDTO;
import br.com.meli.PIFrescos.controller.dtos.BatchStockDTO;
import br.com.meli.PIFrescos.models.Batch;
import br.com.meli.PIFrescos.service.BatchServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.util.MultiValueMap;

import java.util.List;

@RestController
@RequestMapping("/fresh-products/due-date")
public class BatchController {

    @Autowired
    BatchServiceImpl batchService;

    @GetMapping("/list")
    public ResponseEntity<List<BatchStockDTO>> getBatchesByCategory(@RequestParam(required = false) Integer days,
                                                                    @RequestParam(required = false) String category,
                                                                    @RequestParam(required = false) String order){

        List<Batch> batchList = batchService.findBatchesOrderBy(days, category, order);
        return ResponseEntity.ok(BatchStockDTO.convert(batchList));
    }

}
