package br.com.meli.PIFrescos.controller;

import br.com.meli.PIFrescos.service.BatchServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fresh-products")
public class BatchController {

    @Autowired
    BatchServiceImpl batchService;


}
