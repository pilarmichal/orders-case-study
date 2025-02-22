package com.pilarmichal.orderscasestudy.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
public class PurchaseOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long purchaseOrderId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PurchaseOrderStatus orderStatus;

    @Column(nullable = false)
    private LocalDateTime lastUpdated;

    @ManyToMany
    @JoinTable(
            name = "purchase_order_items",
            joinColumns = @JoinColumn(name = "purchase_order_id"),
            inverseJoinColumns = @JoinColumn(name = "item_id"))
    private List<OrderItem> orderItems;

    public void updateQuantityForItems() {
        for (OrderItem orderItem : orderItems) {
            // Update item quantity based on the order's quantity
            Item item = orderItem.getItem();
            int orderQuantity = orderItem.getQuantity();
            item.setItemQuantity(item.getItemQuantity() - orderQuantity);
        }
    }
}
