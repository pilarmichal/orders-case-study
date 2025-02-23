package com.pilarmichal.orderscasestudy.scheduler;

import com.pilarmichal.orderscasestudy.constants.Constants;
import com.pilarmichal.orderscasestudy.service.PurchaseOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class OrderExpirationTask {

    private static final Logger logger = LoggerFactory.getLogger(OrderExpirationTask.class);
    private final PurchaseOrderService purchaseOrderService;

    @Autowired
    public OrderExpirationTask(PurchaseOrderService purchaseOrderService) {
        this.purchaseOrderService = purchaseOrderService;
    }

    @Scheduled(fixedRate = Constants.FIXED_RATE)
    public void cleanupExpiredOrders() {
        logger.info("Orders expiration task started...");

        this.purchaseOrderService.expirePurchaseOrders();

        logger.info("Orders expiration task completed.");
    }
}

