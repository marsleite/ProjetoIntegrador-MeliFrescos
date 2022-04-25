package br.com.meli.PIFrescos.service;

import br.com.meli.PIFrescos.repository.BatchRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class BatchServiceTests {

    @InjectMocks
    private BatchServiceImpl batchService;

    @Mock
    private BatchRepository batchRepository;

    @Mock
    private ProductService productService;

    @BeforeEach
    public void init() {
        batchService = new BatchServiceImpl(batchRepository, productService);
    }

    @Test
    public void shouldBeAbleCreateBatchAndReturnTheCreatedBatch(){

    }
}
