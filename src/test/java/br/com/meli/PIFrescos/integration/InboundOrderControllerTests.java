package br.com.meli.PIFrescos.integration;


import br.com.meli.PIFrescos.controller.InBoundOrderController;
import br.com.meli.PIFrescos.models.*;
import br.com.meli.PIFrescos.models.dto.InboundOrderDTO;
import br.com.meli.PIFrescos.repository.BatchRepository;
import br.com.meli.PIFrescos.repository.InboundOrderRepository;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author Antonio Hugo
 */
@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest
public class InboundOrderControllerTests {

    @MockBean
    private InboundOrderRepository inboundOrderRepository;

    @Autowired
    private MockMvc mockMvc;

    ObjectMapper objectMapper = new ObjectMapper();

    Warehouse warehouseMock = new Warehouse();
    Section sectionMock = new Section();
    Product mockProduct = new Product();
    InboundOrder mockInboundOrder = new InboundOrder();
    InboundOrder mockInboundOrder2 = new InboundOrder();
    @BeforeEach
    public void setUp() {

        warehouseMock.setWarehouseCode(1);

        mockProduct.setProductId(1);
        mockProduct.setProductType(StorageType.FRESH);
        mockProduct.setProductName("Uva");
        mockProduct.setProductDescription("Mock description");

        sectionMock.setSectionCode(1);
        sectionMock.setStorageType(StorageType.FRESH);
        sectionMock.setMaxCapacity(7);
        sectionMock.setCurrentCapacity(2);
        sectionMock.setWarehouse(warehouseMock);

        List<Batch> batches = new ArrayList<>();

        batches.add(Batch.builder()
                .batchNumber(1)
                .currentTemperature(7.0f)
                .minimumTemperature(6.0f)
                .initialQuantity(5)
                .currentQuantity(5)
                .manufacturingDate(LocalDate.now())
                .dueDate(LocalDate.now())
                .inboundOrder(mockInboundOrder)
                .product(mockProduct)
                .build());

        batches.add(Batch.builder()
                .batchNumber(1)
                .currentTemperature(7.0f)
                .minimumTemperature(6.0f)
                .initialQuantity(5)
                .currentQuantity(5)
                .manufacturingDate(LocalDate.now())
                .dueDate(LocalDate.now())
                .inboundOrder(mockInboundOrder)
                .product(mockProduct)
                .build());

        mockInboundOrder.setOrderNumber(1);
        mockInboundOrder.setOrderDate(LocalDate.now());
        mockInboundOrder.setSection(sectionMock);
        mockInboundOrder.setBatchStock(batches);

        mockInboundOrder2.setOrderNumber(2);

        mockInboundOrder2.setOrderDate(LocalDate.now());
        mockInboundOrder2.setSection(sectionMock);
        mockInboundOrder2.setBatchStock(batches);
    }


    @Test
    public void getAllInboundOrdersVoid() throws Exception{

        String expected = "[]";
        MvcResult result = mockMvc.perform(get("/fresh-products/inboundorder"))
                .andExpect(status().isOk())
                .andReturn();

        Assertions.assertEquals(expected, result.getResponse().getContentAsString());
    }

    @Test
    public void getAllInboundOrders() throws Exception {

        List <InboundOrder> inboundOrders = new  ArrayList<>();

        inboundOrders.add(mockInboundOrder);
        inboundOrders.add(mockInboundOrder2);

        Mockito.when(inboundOrderRepository.findAll()).thenReturn(inboundOrders);

        mockMvc.perform(get("/fresh-products/inboundorder"))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    public void createInboundOrder() throws Exception  {
     String payload = "{\n"
          +  "\"orderDate\": \"2022-04-01\", \n"
          +  "\"section\": {\n"
          +  "\"sectionCode\": 1,\n"
          +  "\"warehouse\": {\n"
          +  "\"warehouseCode\": 1\n"
          +  "}\n"
        +"},\n"
        +" \"batchStock\":[ \n"
        +"    {\n"
        +"        \"currentTemperature\": 7.0, \n"
        +"        \"minimumTemperature\": 6.0, \n"
        +"        \"initialQuantity\": 5, \n"
        +"        \"currentQuantity\": 5,\n"
        +"        \"manufacturingDate\": \"2022-03-24\", \n"
        +"        \"dueDate\": \"2022-04-24\" \n"
        +"    }, \n"
        +"   {\n"
        +"        \"currentTemperature\": 7.0,\n"
        +"         \"minimumTemperature\": 6.0,\n"
        +"         \"initialQuantity\": 4, \n"
        +"         \"currentQuantity\": 4, \n"
        +"        \"manufacturingDate\": \"2022-03-24\", \n"
        +"       \"dueDate\": \"2022-04-24\" \n"
        +"   }, \n"
        +"    { \n"
        +"       \"productId\": 2, \n"
        +"       \"currentTemperature\": 8.0,\n"
        +"       \"minimumTemperature\": 6.0, \n"
        +"      \"initialQuantity\": 6, \n"
        +"      \"currentQuantity\": 6, \n"
        +"       \"manufacturingDate\": \"2022-03-20\", \n"
        +"      \"dueDate\": \"2022-04-20\" \n"
        +"    },\n"
        +"   { \n"
        +"       \"currentTemperature\": 8.0, \n"
        +"       \"minimumTemperature\": 6.0, \n"
        +"       \"initialQuantity\": 7,\n"
        +"      \"currentQuantity\": 7,\n"
        +"      \"manufacturingDate\": \"2022-03-20\", \n"
        +"      \"dueDate\": \"2022-04-20\" \n"
        +"   \n}"
        +"    ] \n"
        +"}\n";

        mockMvc.perform(post("/fresh-products/inboundorder")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload)
        )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.orderNumber").value(1));

    }
}
