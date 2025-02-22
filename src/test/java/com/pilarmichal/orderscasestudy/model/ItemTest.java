package com.pilarmichal.orderscasestudy.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class ItemTest {
    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    @Test
    void testItemNameValidation() {
        Item item = new Item();
        item.setItemName("");
        item.setItemPricePerUnit(10.0);
        item.setItemQuantity(5);

        Set<ConstraintViolation<Item>> violations = validator.validate(item);

        assertEquals(1, violations.size());
        assertTrue(violations.iterator().next().getMessage().contains("Item name cannot be empty"));
    }

    @Test
    void testItemPricePerUnitValidation() {
        Item item = new Item();
        item.setItemName("Croissant");
        item.setItemPricePerUnit(-10.0);
        item.setItemQuantity(5);

        Set<ConstraintViolation<Item>> violations = validator.validate(item);

        assertEquals(1, violations.size());
        assertTrue(violations.iterator().next().getMessage().contains("Item price must be greater than or equal to 0"));
    }

    @Test
    void testItemQuantityValidation() {
        Item item = new Item();
        item.setItemName("Croissant");
        item.setItemPricePerUnit(19.9);
        item.setItemQuantity(-16);

        Set<ConstraintViolation<Item>> violations = validator.validate(item);

        assertEquals(1, violations.size());
        assertTrue(violations.iterator().next().getMessage().contains("Item quantity must be greater than or equal to 0"));
    }
}
