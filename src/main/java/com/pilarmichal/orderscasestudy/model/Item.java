package com.pilarmichal.orderscasestudy.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Getter @Setter
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itemId;

    @Column(nullable = false, columnDefinition = "varchar(255) default 'XNA'")
    @NotBlank(message = "Item name cannot be empty")
    private String itemName;

    @Column(nullable = false, columnDefinition = "double default 0")
    @Min(value = 0, message = "Item price must be greater than or equal to 0")
    private double itemPricePerUnit;

    @Column(nullable = false, columnDefinition = "int default 0")
    @Min(value = 0, message = "Item quantity must be greater than or equal to 0")
    private int itemQuantity;
}
