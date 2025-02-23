package com.pilarmichal.orderscasestudy.service;

import com.pilarmichal.orderscasestudy.dto.OrderItemDTO;
import com.pilarmichal.orderscasestudy.exception.InsufficientQuantityException;
import com.pilarmichal.orderscasestudy.exception.ResourceNotFoundException;
import com.pilarmichal.orderscasestudy.model.*;
import com.pilarmichal.orderscasestudy.repository.ItemRepository;
import com.pilarmichal.orderscasestudy.repository.OrderItemRepository;
import com.pilarmichal.orderscasestudy.repository.PurchaseOrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class PurchaseOrderServiceTest {

    @Mock
    private PurchaseOrderRepository purchaseOrderRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    @InjectMocks
    private PurchaseOrderService purchaseOrderService;

    private Item item;
    private PurchaseOrder purchaseOrder;
    private OrderItemDTO orderItemDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Setup mock Item
        this.item = new Item();
        this.item.setItemId(1L);
        this.item.setItemName("Croissant");
        this.item.setItemPricePerUnit(10.99);
        this.item.setItemQuantity(10);

        // Setup mock PurchaseOrder
        this.purchaseOrder = new PurchaseOrder();
        this.purchaseOrder.setPurchaseOrderId(1L);
        this.purchaseOrder.setOrderStatus(PurchaseOrderStatus.PENDING);

        // Setup mock OrderItemDTO
        this.orderItemDTO = new OrderItemDTO();
        this.orderItemDTO.setItemId(this.item.getItemId());
        this.orderItemDTO.setQuantity(2);
    }

    @Test
    void testCreateOrderSuccessful() {
        List<OrderItemDTO> orderItemDTOS = new ArrayList<>();
        orderItemDTOS.add(this.orderItemDTO);

        when(this.itemRepository.findById(1L)).thenReturn(Optional.of(this.item));
        when(this.purchaseOrderRepository.save(any(PurchaseOrder.class))).thenReturn(this.purchaseOrder);
        when(this.purchaseOrderRepository.findById(1L)).thenReturn(Optional.of(this.purchaseOrder));

        PurchaseOrder createdOrder = this.purchaseOrderService.createOrder(orderItemDTOS);

        assertNotNull(createdOrder);
        assertEquals(PurchaseOrderStatus.PENDING, createdOrder.getOrderStatus());
        verify(this.itemRepository, times(1)).save(any(Item.class));
        verify(this.orderItemRepository, times(1)).save(any(OrderItem.class));
    }

    @Test
    void testCreateOrderInsufficientQuantity() {
        this.orderItemDTO.setQuantity(15);

        List<OrderItemDTO> orderItemDTOS = new ArrayList<>();
        orderItemDTOS.add(this.orderItemDTO);

        when(this.itemRepository.findById(1L)).thenReturn(Optional.of(this.item));

        InsufficientQuantityException exception = assertThrows(InsufficientQuantityException.class, () -> this.purchaseOrderService.createOrder(orderItemDTOS));

        assertNotNull(exception.getInsufficientItems());
        assertEquals(1, exception.getInsufficientItems().size());
        assertEquals(1L, exception.getInsufficientItems().get(0).getItemId());
        assertEquals(5, exception.getInsufficientItems().get(0).getQuantity());  // 15 requested, 10 available

        verify(this.itemRepository, never()).save(any(Item.class));
    }

    @Test
    void testCancelPurchaseOrderSuccessful() {
        List<OrderItem> orderItems = new ArrayList<>();
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(this.item);
        orderItem.setQuantity(2);
        orderItems.add(orderItem);

        this.purchaseOrder.setOrderItems(orderItems);

        when(this.purchaseOrderRepository.findById(1L)).thenReturn(Optional.of(this.purchaseOrder));
        when(this.itemRepository.save(any(Item.class))).thenReturn(this.item);
        when(this.purchaseOrderRepository.save(any(PurchaseOrder.class))).thenReturn(this.purchaseOrder);

        PurchaseOrder canceledOrder = this.purchaseOrderService.cancelPurchaseOrder(1L);

        assertEquals(PurchaseOrderStatus.CANCELLED, canceledOrder.getOrderStatus());
        assertEquals(12, this.item.getItemQuantity());  // 10 original + 2 returned from the canceled order
        verify(this.purchaseOrderRepository, times(1)).save(any(PurchaseOrder.class));
    }

    @Test
    void testCancelPurchaseOrderNotSuccessful() {
        List<OrderItem> orderItems = new ArrayList<>();
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(this.item);
        orderItem.setQuantity(2);
        orderItems.add(orderItem);

        this.purchaseOrder.setOrderStatus(PurchaseOrderStatus.PAYED);
        this.purchaseOrder.setOrderItems(orderItems);

        when(this.purchaseOrderRepository.findById(1L)).thenReturn(Optional.of(this.purchaseOrder));

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> this.purchaseOrderService.cancelPurchaseOrder(1L));

        assertEquals("Order can not be cancelled", exception.getMessage());

        // Verify no other repository calls were made after the exception is thrown
        verify(this.purchaseOrderRepository, never()).save(any(PurchaseOrder.class));
        verify(this.itemRepository, never()).save(any(Item.class));
    }

    @Test
    void testPayPurchaseOrderSuccessful() {
        this.purchaseOrder.setOrderStatus(PurchaseOrderStatus.PENDING);
        when(this.purchaseOrderRepository.findById(1L)).thenReturn(Optional.of(this.purchaseOrder));
        when(this.purchaseOrderRepository.save(any(PurchaseOrder.class))).thenReturn(this.purchaseOrder);

        PurchaseOrder paidOrder = this.purchaseOrderService.payPurchaseOrder(1L);

        assertEquals(PurchaseOrderStatus.PAYED, paidOrder.getOrderStatus());
        verify(this.purchaseOrderRepository, times(1)).save(any(PurchaseOrder.class));
    }

    @Test
    void testExpirePurchaseOrders() {
        PurchaseOrder expiredOrder = new PurchaseOrder();
        expiredOrder.setPurchaseOrderId(2L);
        expiredOrder.setOrderStatus(PurchaseOrderStatus.PENDING);

        when(this.purchaseOrderRepository.findPendingOrdersOlderThan(any())).thenReturn(List.of(expiredOrder));
        when(this.purchaseOrderRepository.findById(2L)).thenReturn(Optional.of(expiredOrder));
        when(this.itemRepository.save(any(Item.class))).thenReturn(this.item);
        when(this.purchaseOrderRepository.save(any(PurchaseOrder.class))).thenReturn(expiredOrder);

        this.purchaseOrderService.expirePurchaseOrders();

        assertEquals(PurchaseOrderStatus.EXPIRED, expiredOrder.getOrderStatus());
        verify(this.purchaseOrderRepository, times(1)).save(expiredOrder);
    }

    @Test
    void testGetPurchaseOrderNotFound() {
        when(this.purchaseOrderRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> this.purchaseOrderService.getPurchaseOrder(1L));

        assertEquals("Order not found with id 1", exception.getMessage());
    }
}

