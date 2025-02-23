package com.pilarmichal.orderscasestudy.service;

import com.pilarmichal.orderscasestudy.constants.Constants;
import com.pilarmichal.orderscasestudy.dto.OrderItemDTO;
import com.pilarmichal.orderscasestudy.exception.InsufficientQuantityException;
import com.pilarmichal.orderscasestudy.exception.ResourceNotFoundException;
import com.pilarmichal.orderscasestudy.model.Item;
import com.pilarmichal.orderscasestudy.model.OrderItem;
import com.pilarmichal.orderscasestudy.model.PurchaseOrder;
import com.pilarmichal.orderscasestudy.model.PurchaseOrderStatus;
import com.pilarmichal.orderscasestudy.repository.ItemRepository;
import com.pilarmichal.orderscasestudy.repository.OrderItemRepository;
import com.pilarmichal.orderscasestudy.repository.PurchaseOrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.abs;

@Service
public class PurchaseOrderService {
    private static final Logger logger = LoggerFactory.getLogger(PurchaseOrderService.class);
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final ItemRepository itemRepository;
    private final OrderItemRepository orderItemRepository;

    @Autowired
    public PurchaseOrderService(PurchaseOrderRepository purchaseOrderRepository, ItemRepository itemRepository, OrderItemRepository orderItemRepository) {
        this.purchaseOrderRepository = purchaseOrderRepository;
        this.itemRepository = itemRepository;
        this.orderItemRepository = orderItemRepository;
    }

    @Transactional
    public PurchaseOrder createOrder(List<OrderItemDTO> orderItemDTOS) {
        this.expirePurchaseOrders();

        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setOrderStatus(PurchaseOrderStatus.PENDING);
        purchaseOrder = this.purchaseOrderRepository.save(purchaseOrder);

        List<OrderItemDTO> insufficientItems = new ArrayList<>();

        List<OrderItem> orderItems = new ArrayList<>();

        for (OrderItemDTO orderRequest : orderItemDTOS) {
            Long itemId = orderRequest.getItemId();
            int quantity = orderRequest.getQuantity();

            Item item = this.itemRepository.findById(itemId)
                    .orElseThrow(() -> new ResourceNotFoundException("Item not found with id " + itemId));

            if (item.getItemQuantity() < quantity) {
                OrderItemDTO insufficientItemDTO = new OrderItemDTO();
                insufficientItemDTO.setItemId(itemId);
                insufficientItemDTO.setQuantity(abs(item.getItemQuantity() - quantity));
                insufficientItems.add(insufficientItemDTO);

                continue;
            }

            item.setItemQuantity(item.getItemQuantity() - quantity);
            this.itemRepository.save(item);

            OrderItem orderItem = new OrderItem();
            orderItem.setPurchaseOrder(purchaseOrder);
            orderItem.setItem(item);
            orderItem.setQuantity(quantity);
            this.orderItemRepository.save(orderItem);
            orderItems.add(orderItem);
        }

        if (!insufficientItems.isEmpty()) {
            throw new InsufficientQuantityException(insufficientItems);
        }

        Long purchaseOrderId = purchaseOrder.getPurchaseOrderId();
        purchaseOrder.setOrderItems(orderItems);

        return this.purchaseOrderRepository.findById(purchaseOrder.getPurchaseOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id " + purchaseOrderId));
    }

    public PurchaseOrder getPurchaseOrder(Long purchaseOrderId) {
        return this.purchaseOrderRepository.findById(purchaseOrderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id " + purchaseOrderId));
    }

    public List<PurchaseOrder> getPurchaseOrders() {
        return this.purchaseOrderRepository.findAll();
    }

    public PurchaseOrder cancelPurchaseOrder(Long purchaseOrderId) {
        this.expirePurchaseOrders();

        PurchaseOrder purchaseOrder = purchaseOrderRepository.findById(purchaseOrderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id " + purchaseOrderId));

        if (purchaseOrder.getOrderStatus() != PurchaseOrderStatus.PENDING) {
            throw new IllegalStateException("Order can not be cancelled");
        }

        for (OrderItem orderItem : purchaseOrder.getOrderItems()) {
            Item item = orderItem.getItem();
            item.setItemQuantity(item.getItemQuantity() + orderItem.getQuantity());
            this.itemRepository.save(item);
        }

        purchaseOrder.setOrderStatus(PurchaseOrderStatus.CANCELLED);

        return this.purchaseOrderRepository.save(purchaseOrder);
    }


    public PurchaseOrder payPurchaseOrder(Long purchaseOrderId) {
        this.expirePurchaseOrders();

        PurchaseOrder purchaseOrder = purchaseOrderRepository.findById(purchaseOrderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id " + purchaseOrderId));

        if (purchaseOrder.getOrderStatus() != PurchaseOrderStatus.PENDING) {
            throw new IllegalStateException("Order can not be payed");
        }

        purchaseOrder.setOrderStatus(PurchaseOrderStatus.PAYED);
        return purchaseOrderRepository.save(purchaseOrder);
    }

    public void expirePurchaseOrders() {
        LocalDateTime expirationPeriod = LocalDateTime.now().minusMinutes(Constants.EXPIRATION_PERIOD);
        List<PurchaseOrder> expiredOrders = purchaseOrderRepository.findPendingOrdersOlderThan(expirationPeriod);

        if (expiredOrders.isEmpty()) {
            logger.info("No expired orders found.");
        } else {
            for (PurchaseOrder expiredOrder : expiredOrders) {
                if (expiredOrder.getOrderStatus() == PurchaseOrderStatus.PENDING) {
                    Long expiredOrderId = expiredOrder.getPurchaseOrderId();
                    expiredOrder = this.purchaseOrderRepository.findById(expiredOrderId)
                            .orElseThrow(() -> new ResourceNotFoundException("Order not found with id " + expiredOrderId));


                    expiredOrder.setOrderStatus(PurchaseOrderStatus.EXPIRED);

                    for (OrderItem orderItem : expiredOrder.getOrderItems()) {
                        Item item = orderItem.getItem();
                        item.setItemQuantity(item.getItemQuantity() + orderItem.getQuantity());
                        itemRepository.save(item);
                    }

                    purchaseOrderRepository.save(expiredOrder);
                    logger.info("Expired order with ID {} has been marked as EXPIRED.", expiredOrder.getPurchaseOrderId());
                }
            }
        }
    }
}