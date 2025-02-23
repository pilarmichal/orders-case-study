package com.pilarmichal.orderscasestudy.repository;

import com.pilarmichal.orderscasestudy.model.PurchaseOrder;
import com.pilarmichal.orderscasestudy.model.PurchaseOrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class PurchaseOrderRepositoryTest {
    private final PurchaseOrderRepository purchaseOrderRepository;

    @Autowired
    public PurchaseOrderRepositoryTest(PurchaseOrderRepository purchaseOrderRepository) {
        this.purchaseOrderRepository = purchaseOrderRepository;
    }

    private PurchaseOrder purchaseOrder;

    @BeforeEach
    public void setUp() {
        this.purchaseOrder = new PurchaseOrder();
        this.purchaseOrder.setOrderStatus(PurchaseOrderStatus.PENDING);
    }

    @Test
    public void testStorePurchaseOrder() {
        PurchaseOrder savedPurchaseOrder = this.purchaseOrderRepository.save(this.purchaseOrder);
        assertNotNull(savedPurchaseOrder);
        assertTrue(savedPurchaseOrder.getPurchaseOrderId() > 0);
        assertEquals(savedPurchaseOrder.getOrderStatus(), PurchaseOrderStatus.PENDING);
    }

    @Test
    public void testFindPurchaseOrderById() {
        PurchaseOrder savedPurchaseOrder = this.purchaseOrderRepository.save(this.purchaseOrder);
        PurchaseOrder foundPurchaseOrder = this.purchaseOrderRepository.findById(savedPurchaseOrder.getPurchaseOrderId()).orElse(null);

        assertNotNull(foundPurchaseOrder);
        assertEquals(foundPurchaseOrder.getPurchaseOrderId(), savedPurchaseOrder.getPurchaseOrderId());
        assertEquals(foundPurchaseOrder.getOrderStatus(), savedPurchaseOrder.getOrderStatus());
    }

    @Test
    public void testFindByNonExistentId() {
        PurchaseOrder purchaseOrder = this.purchaseOrderRepository.findById(999L).orElse(null);
        assertNull(purchaseOrder);
    }

    @Test
    public void testDeletePurchaseOrder() {
        PurchaseOrder savedPurchasedOrder = this.purchaseOrderRepository.save(this.purchaseOrder);
        PurchaseOrder foundPurchasedOrder = this.purchaseOrderRepository.findById(savedPurchasedOrder.getPurchaseOrderId()).orElse(null);

        assertNotNull(foundPurchasedOrder);

        this.purchaseOrderRepository.delete(foundPurchasedOrder);
        PurchaseOrder deletedPurchasedOrder = this.purchaseOrderRepository.findById(savedPurchasedOrder.getPurchaseOrderId()).orElse(null);

        assertNull(deletedPurchasedOrder);
    }

    @Test void testUpdatePurchaseOrder() {
        PurchaseOrder savedPurchaseOrder = this.purchaseOrderRepository.save(this.purchaseOrder);
        savedPurchaseOrder.setOrderStatus(PurchaseOrderStatus.EXPIRED);

        PurchaseOrder updatedPurchaseOrder = this.purchaseOrderRepository.save(savedPurchaseOrder);
        assertEquals(updatedPurchaseOrder.getOrderStatus(), PurchaseOrderStatus.EXPIRED);
    }
}
