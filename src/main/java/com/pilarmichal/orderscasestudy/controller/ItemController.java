package com.pilarmichal.orderscasestudy.controller;

import com.pilarmichal.orderscasestudy.dto.ErrorDTO;
import com.pilarmichal.orderscasestudy.exception.ResourceNotFoundException;
import com.pilarmichal.orderscasestudy.model.Item;
import com.pilarmichal.orderscasestudy.repository.ItemRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "Item Controller", description = "APIs for managing items in the system")
@RequestMapping("/items")
public class ItemController {
    private final ItemRepository itemRepository;

    @Autowired
    public ItemController(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Operation(summary = "Get all items", description = "Retrieves a list of all items in the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of items")
    })
    @GetMapping
    public List<Item> getItems() {
        return this.itemRepository.findAll();
    }

    @Operation(summary = "Get a specific item", description = "Retrieves an item by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the item"),
            @ApiResponse(responseCode = "404", description = "Item not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class)))
    })
    @GetMapping("/{id}")
    public Item getItem(@PathVariable Long id) {
        return this.itemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Item with ItemId " + id + " not found"));
    }

    @Operation(summary = "Create a new item", description = "Adds a new item to the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Item created successfully"),
            @ApiResponse(responseCode = "400", description = "ItemId must be null",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class)))
    })
    @PostMapping
    public ResponseEntity<Item> createItem(@Valid @RequestBody Item item) {
        if (item.getItemId() != null) {
            throw new RuntimeException("ItemId must be null");
        }

        Item savedItem = this.itemRepository.save(item);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedItem);
    }

    @Operation(summary = "Update an existing item", description = "Updates the details of an existing item by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item updated successfully"),
            @ApiResponse(responseCode = "404", description = "Item not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorDTO.class)))
    })
    @PutMapping("/{id}")
    public Item updateItem(@PathVariable Long id, @Valid @RequestBody Item updatedItem) {
        return itemRepository.findById(id)
                .map(existingItem -> {
                    existingItem.setItemName(updatedItem.getItemName());
                    existingItem.setItemQuantity(updatedItem.getItemQuantity());
                    existingItem.setItemPricePerUnit(updatedItem.getItemPricePerUnit());
                    return itemRepository.save(existingItem);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Item with ItemId " + id + " not found"));
    }
}
