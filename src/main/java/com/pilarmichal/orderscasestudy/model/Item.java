package com.pilarmichal.orderscasestudy.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itemId;

    @Column(nullable = false, columnDefinition = "varchar(255) default 'Item'")
    private String itemName;

    @Column(nullable = false, columnDefinition = "int default 0")
    private double itemPricePerUnit;

    @Column(nullable = false, columnDefinition = "int default 0")
    private int itemQuantity;
}
