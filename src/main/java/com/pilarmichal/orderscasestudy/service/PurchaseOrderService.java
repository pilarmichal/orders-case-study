package com.pilarmichal.orderscasestudy.service;

import com.pilarmichal.orderscasestudy.dto.OrderRequestDTO;
import com.pilarmichal.orderscasestudy.model.Item;
import com.pilarmichal.orderscasestudy.model.OrderItem;
import com.pilarmichal.orderscasestudy.model.PurchaseOrder;
import com.pilarmichal.orderscasestudy.model.PurchaseOrderStatus;
import com.pilarmichal.orderscasestudy.repository.ItemRepository;
import com.pilarmichal.orderscasestudy.repository.OrderItemRepository;
import com.pilarmichal.orderscasestudy.repository.PurchaseOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PurchaseOrderService {

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
    public PurchaseOrder createOrder(List<OrderRequestDTO> orderRequestDTOs) {
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setOrderStatus(PurchaseOrderStatus.PENDING);
        purchaseOrder = purchaseOrderRepository.save(purchaseOrder);

        for (OrderRequestDTO orderRequest : orderRequestDTOs) {
            Long itemId = orderRequest.getItemId();
            int quantity = orderRequest.getQuantity();

            Item item = itemRepository.findById(itemId)
                    .orElseThrow(() -> new IllegalArgumentException("Item not found with id " + itemId));

            if (item.getItemQuantity() < quantity) {
                throw new IllegalArgumentException("Insufficient quantity for item " + itemId);
            }

            item.setItemQuantity(item.getItemQuantity() - quantity);
            itemRepository.save(item);

            OrderItem orderItem = new OrderItem();
            orderItem.setPurchaseOrder(purchaseOrder);
            orderItem.setItem(item);
            orderItem.setQuantity(quantity);
            orderItemRepository.save(orderItem);
        }

        return purchaseOrder;
    }
}