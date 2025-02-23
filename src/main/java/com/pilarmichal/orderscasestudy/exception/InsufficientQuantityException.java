package com.pilarmichal.orderscasestudy.exception;

import com.pilarmichal.orderscasestudy.dto.OrderItemDTO;
import lombok.Getter;

import java.util.List;

@Getter
public class InsufficientQuantityException extends RuntimeException {
    private final List<OrderItemDTO> insufficientItems;

    public InsufficientQuantityException(List<OrderItemDTO> insufficientItems) {
        this.insufficientItems = insufficientItems;
    }
}

