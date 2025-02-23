package com.pilarmichal.orderscasestudy.dto;

import com.pilarmichal.orderscasestudy.model.OrderItem;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class OrderItemDTO {
    private Long itemId;
    private int quantity;

    public OrderItemDTO(OrderItem orderItem) {
        this.itemId = orderItem.getItem().getItemId();
        this.quantity = orderItem.getQuantity();
    }

    public OrderItemDTO() {
    }
}
