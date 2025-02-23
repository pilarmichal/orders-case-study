package com.pilarmichal.orderscasestudy.component;

import com.pilarmichal.orderscasestudy.model.Item;
import com.pilarmichal.orderscasestudy.model.OrderItem;
import com.pilarmichal.orderscasestudy.model.PurchaseOrder;
import com.pilarmichal.orderscasestudy.model.PurchaseOrderStatus;
import com.pilarmichal.orderscasestudy.repository.ItemRepository;
import com.pilarmichal.orderscasestudy.repository.OrderItemRepository;
import com.pilarmichal.orderscasestudy.repository.PurchaseOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("dev")
public class DataInitializer implements CommandLineRunner {

    private final ItemRepository itemRepository;

    private final PurchaseOrderRepository purchaseOrderRepository;

    private final OrderItemRepository orderItemRepository;

    @Autowired
    public DataInitializer(ItemRepository itemRepository, PurchaseOrderRepository purchaseOrderRepository,
                           OrderItemRepository orderItemRepository) {
        this.itemRepository = itemRepository;
        this.purchaseOrderRepository = purchaseOrderRepository;
        this.orderItemRepository = orderItemRepository;
    }

    @Override
    public void run(String... args) {
        // Item
        Item item1 = new Item();
        item1.setItemName("Apple");
        item1.setItemPricePerUnit(1.0);
        item1.setItemQuantity(100);
        this.itemRepository.save(item1);

        Item item2 = new Item();
        item2.setItemName("Banana");
        item2.setItemPricePerUnit(0.8);
        item2.setItemQuantity(150);
        this.itemRepository.save(item2);

        // PurchaseOrder
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setOrderStatus(PurchaseOrderStatus.PENDING);
        this.purchaseOrderRepository.save(purchaseOrder);

        // OrderItems
        OrderItem orderItem1 = new OrderItem();
        orderItem1.setPurchaseOrder(purchaseOrder);
        orderItem1.setItem(item1);
        orderItem1.setQuantity(2);
        this.orderItemRepository.save(orderItem1);

        OrderItem orderItem2 = new OrderItem();
        orderItem2.setPurchaseOrder(purchaseOrder);
        orderItem2.setItem(item2);
        orderItem2.setQuantity(3);
        this.orderItemRepository.save(orderItem2);
    }
}

