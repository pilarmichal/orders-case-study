package com.pilarmichal.orderscasestudy.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pilarmichal.orderscasestudy.model.Item;
import com.pilarmichal.orderscasestudy.repository.ItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = "spring.profiles.active=test")
public class ItemControllerTest {

    private final MockMvc mockMvc;

    private final ItemRepository itemRepository;

    @Autowired
    public ItemControllerTest(MockMvc mockMvc, ItemRepository itemRepository) {
        this.mockMvc = mockMvc;
        this.itemRepository = itemRepository;
    }

    private Item item;

    @BeforeEach
    public void setUp() {
        item = new Item();
        item.setItemName("Croissant");
        item.setItemPricePerUnit(10.99);
        item.setItemQuantity(100);

        itemRepository.deleteAll();
    }

    @Test
    public void testCreateItem() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String itemJson = objectMapper.writeValueAsString(item);

        mockMvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(itemJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.itemName").value("Croissant"))
                .andExpect(jsonPath("$.itemPricePerUnit").value(10.99))
                .andExpect(jsonPath("$.itemQuantity").value(100));

        List<Item> items = itemRepository.findAll();
        assertEquals(items.size(),1); // Should have one item
        assertEquals(items.get(0).getItemName(), "Croissant");
        assertEquals(items.get(0).getItemPricePerUnit(), 10.99);
        assertEquals(items.get(0).getItemQuantity(), 100);
    }
}
