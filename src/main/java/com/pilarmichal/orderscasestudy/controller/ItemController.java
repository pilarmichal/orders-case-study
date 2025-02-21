package com.pilarmichal.orderscasestudy.controller;

import com.pilarmichal.orderscasestudy.model.Item;
import com.pilarmichal.orderscasestudy.repository.ItemRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemRepository itemRepository;

    public ItemController(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @GetMapping
    public List<Item> getItems() {
        return this.itemRepository.findAll();
    }

    @PostMapping
    public Item createItem(@RequestBody Item item) {
        if (item.getItemId() != null) {
            throw new IllegalArgumentException("Item ID must be null");
        }

        return this.itemRepository.save(item);
    }
}
