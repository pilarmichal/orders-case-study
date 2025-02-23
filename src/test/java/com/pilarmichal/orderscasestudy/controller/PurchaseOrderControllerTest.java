package com.pilarmichal.orderscasestudy.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pilarmichal.orderscasestudy.dto.OrderItemDTO;
import com.pilarmichal.orderscasestudy.dto.PurchaseOrderDTO;
import com.pilarmichal.orderscasestudy.mapper.PurchaseOrderMapper;
import com.pilarmichal.orderscasestudy.model.PurchaseOrder;
import com.pilarmichal.orderscasestudy.model.PurchaseOrderStatus;
import com.pilarmichal.orderscasestudy.service.PurchaseOrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class PurchaseOrderControllerTest {

    @Mock
    private PurchaseOrderService purchaseOrderService;

    @Mock
    private PurchaseOrderMapper purchaseOrderMapper;

    @InjectMocks
    private PurchaseOrderController purchaseOrderController;

    private MockMvc mockMvc;

    private OrderItemDTO orderItemDTO;

    private PurchaseOrderDTO purchaseOrderDTO;

    @BeforeEach
    public void setUp() {
        // Initialize your test data
        this.orderItemDTO = new OrderItemDTO();
        this.orderItemDTO.setQuantity(10);
        this.orderItemDTO.setItemId(1L);

        LocalDateTime currentTime = LocalDateTime.now();
        List<OrderItemDTO> orderItemDTOS = new ArrayList<>();
        orderItemDTOS.add(this.orderItemDTO);

        this.purchaseOrderDTO = new PurchaseOrderDTO();
        this.purchaseOrderDTO.setPurchaseOrderId(1L);
        this.purchaseOrderDTO.setOrderStatus(PurchaseOrderStatus.PENDING);
        this.purchaseOrderDTO.setCreatedAt(currentTime);
        this.purchaseOrderDTO.setLastUpdated(currentTime);
        this.purchaseOrderDTO.setOrderItems(orderItemDTOS);

        this.mockMvc = MockMvcBuilders.standaloneSetup(purchaseOrderController).build();
    }

    @Test
    public void testCreatePurchaseOrder() throws Exception {


        List<OrderItemDTO> orderItemDTOS = new ArrayList<>();
        orderItemDTOS.add(this.orderItemDTO);

        ObjectMapper objectMapper = new ObjectMapper();
        String itemJson = objectMapper.writeValueAsString(orderItemDTOS);

        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setPurchaseOrderId(1L);
        when(this.purchaseOrderService.createOrder(Mockito.anyList())).thenReturn(purchaseOrder);
        when(this.purchaseOrderMapper.toDTO(purchaseOrder)).thenReturn(this.purchaseOrderDTO);

        this.mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(itemJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.purchaseOrderId").value(1L))
                .andExpect(jsonPath("$.orderStatus").value("PENDING"));

        verify(this.purchaseOrderService, times(1)).createOrder(Mockito.anyList());
    }
}
