package br.com.meli.PIFrescos;

import br.com.meli.PIFrescos.controller.dtos.BatchDTO;
import br.com.meli.PIFrescos.controller.dtos.InboundOrderDTO;
import br.com.meli.PIFrescos.dtos.InboundOrderUpdateDTO;
import br.com.meli.PIFrescos.models.Batch;
import br.com.meli.PIFrescos.models.InboundOrder;
import br.com.meli.PIFrescos.models.Product;
import br.com.meli.PIFrescos.models.Section;
import br.com.meli.PIFrescos.models.StorageType;
import br.com.meli.PIFrescos.models.Warehouse;
import br.com.meli.PIFrescos.repository.BatchRepository;
import br.com.meli.PIFrescos.repository.InboundOrderRepository;
import br.com.meli.PIFrescos.repository.ProductRepository;
import br.com.meli.PIFrescos.repository.SectionRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
public class InBoundOrderIT {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private InboundOrderRepository inboundOrderRepository;
    @MockBean
    private ProductRepository productRepository;
    @MockBean
    private BatchRepository batchRepository;
    @MockBean
    SectionRepository sectionRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Warehouse warehouse1 = new Warehouse();
    private Section section1 = new Section(1, StorageType.FRESH, 100, 0, warehouse1);
    private Section section2 = new Section(2, StorageType.REFRIGERATED, 100, 0, warehouse1);
    private List<InboundOrder> inboundOrders = new ArrayList<>();
    private InboundOrder inboundOrder1 = new InboundOrder();
    private InboundOrder inboundOrder2 = new InboundOrder();
    private Batch batch1 = new Batch();
    private Batch batch2 = new Batch();
    private Product product1 = new Product(1, "product1", StorageType.FRESH, "desc1");
    private Product product2 = new Product(2, "product2", StorageType.FRESH, "desc2");

    @BeforeEach
    public void setup() {
        batch1.setBatchNumber(1);
        batch1.setCurrentQuantity(1);
        batch1.setProduct(product1);
        batch2.setBatchNumber(2);
        batch2.setCurrentQuantity(1);
        batch2.setProduct(product2);
        inboundOrder1.setOrderNumber(1);
        inboundOrder1.setSection(section1);
        inboundOrder1.setBatchStock(Arrays.asList(batch1));
        inboundOrder2.setOrderNumber(2);
        inboundOrder2.setSection(section2);
        inboundOrder2.setBatchStock(Arrays.asList(batch2));
    }

    @Test
    public void getAllInboundOrders() throws Exception {
        inboundOrders.add(inboundOrder1);
        inboundOrders.add(inboundOrder2);

        Mockito.when(inboundOrderRepository.findAll()).thenReturn(inboundOrders);

        MvcResult result = mockMvc.perform(get("/fresh-products/inboundorder"))
                .andExpect(status().isOk())
                .andReturn();

        TypeReference<List<InboundOrder>> typeReference = new TypeReference<List<InboundOrder>>() {};
        List<InboundOrder> inboundOrderResponse = objectMapper.readValue(result.getResponse().getContentAsString(), typeReference);

        assertEquals(inboundOrders.get(0).getOrderNumber(), inboundOrderResponse.get(0).getOrderNumber());
    }

    @Test
    public void postInboundOrder() throws Exception {
        section1.setMaxCapacity(100);
        section1.setCurrentCapacity(0);

        InboundOrderDTO inboundOrderDTO = new InboundOrderDTO();
        BatchDTO batchDTO1 = new BatchDTO();
        batchDTO1.setCurrentQuantity(1);
        batchDTO1.setProductId(1);
        inboundOrderDTO.setBatchStock(Arrays.asList(batchDTO1));
        inboundOrderDTO.setSection(section1);
        String inboundOrderString = objectMapper.writeValueAsString(inboundOrderDTO);

        Mockito.when(sectionRepository.findById(any())).thenReturn(java.util.Optional.of(section1));
        Mockito.when(productRepository.findById(any())).thenReturn(java.util.Optional.of(new Product()));
        Mockito.when(inboundOrderRepository.save(any())).thenAnswer(invocation -> inboundOrder1);

        MvcResult result = mockMvc.perform(post("/fresh-products/inboundorder")
                .contentType("application/json").content(inboundOrderString))
                .andExpect(status().isCreated())
                .andReturn();

        TypeReference<List<Batch>> typeReference = new TypeReference<List<Batch>>() {};
        List<Batch> inboundOrderResponse = objectMapper.readValue(result.getResponse().getContentAsString(), typeReference);
        // the response is a List<Batch>

        assertEquals(batch1.getProduct(), inboundOrderResponse.get(0).getProduct());
    }

    @Test
    public void putBatchStockInInboundOrder() throws Exception {
        // dtos
        InboundOrderUpdateDTO newValues = new InboundOrderUpdateDTO();
        BatchDTO batchDTO1 = new BatchDTO();
        BatchDTO batchDTO2 = new BatchDTO();
        batchDTO1.setBatchNumber(1);
        batchDTO1.setProductId(1);
        batchDTO1.setCurrentQuantity(1);
        batchDTO1.setInitialQuantity(1);
        batchDTO2.setBatchNumber(2);
        batchDTO2.setProductId(2);
        batchDTO2.setCurrentQuantity(1);
        batchDTO2.setInitialQuantity(1);
        newValues.setSection(section1);
        newValues.setBatchStock(Arrays.asList(batchDTO1, batchDTO2));
        // after saving values
        InboundOrder inboundOrderAfterSave = new InboundOrder();
        inboundOrderAfterSave.setOrderNumber(1);
        inboundOrderAfterSave.setSection(section1);
        batch1.setInboundOrder(inboundOrderAfterSave);
        batch2.setInboundOrder(inboundOrderAfterSave);
        inboundOrderAfterSave.setBatchStock(Arrays.asList(batch1, batch2));

        String inboundOrderString = objectMapper.writeValueAsString(newValues);

        Mockito.when(inboundOrderRepository.getById(1)).thenReturn(inboundOrder1);
        Mockito.when(inboundOrderRepository.save(any())).thenAnswer(invocation -> inboundOrderAfterSave);
        Mockito.when(inboundOrderRepository.getByOrderNumber(1)).thenReturn(inboundOrder1);
        Mockito.when(productRepository.findById(1)).thenReturn(java.util.Optional.ofNullable(product1));
        Mockito.when(productRepository.findById(2)).thenReturn(java.util.Optional.ofNullable(product2));
        Mockito.when(sectionRepository.findById(1)).thenReturn(java.util.Optional.ofNullable(section1));

        MvcResult result = mockMvc.perform(put("/fresh-products/inboundorder/1")
                .contentType("application/json").content(inboundOrderString))
                .andExpect(status().isCreated())
                .andReturn();

        TypeReference<List<Batch>> typeReference = new TypeReference<List<Batch>>() {};
        List<Batch> inboundOrderResponse = objectMapper.readValue(result.getResponse().getContentAsString(), typeReference);

        List<Batch> expected = Arrays.asList(batch1, batch2);
        assertEquals(expected.size(), inboundOrderResponse.size());
        assertEquals(expected.get(0).getProduct(), inboundOrderResponse.get(0).getProduct());
        assertEquals(expected.get(1).getProduct(), inboundOrderResponse.get(1).getProduct());
        assertEquals(expected.get(0).getBatchNumber(), inboundOrderResponse.get(0).getBatchNumber());
    }
}
