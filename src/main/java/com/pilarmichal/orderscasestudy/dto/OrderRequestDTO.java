package com.pilarmichal.orderscasestudy.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class OrderRequestDTO {
    private Long itemId;
    private int quantity;
}
