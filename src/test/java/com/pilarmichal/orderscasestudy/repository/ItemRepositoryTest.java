package com.pilarmichal.orderscasestudy.repository;

import com.pilarmichal.orderscasestudy.model.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class ItemRepositoryTest {
    private final ItemRepository itemRepository;

    @Autowired
    public ItemRepositoryTest(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    private Item item;

    @BeforeEach
    public void setUp() {
        this.item = new Item();
        this.item.setItemName("Brod roll");
        this.item.setItemPricePerUnit(2.8);
        this.item.setItemQuantity(1258);
    }

    @Test
    public void testStoreItem() {
        Item savedItem = this.itemRepository.save(this.item);
        assertNotNull(savedItem);
        assertTrue(savedItem.getItemId() > 0);
        assertEquals(savedItem.getItemName(), "Brod roll");
    }

    @Test
    public void testFindItemById() {
        Item savedItem = this.itemRepository.save(this.item);
        Item foundItem = itemRepository.findById(savedItem.getItemId()).orElse(null);

        assertNotNull(foundItem);
        assertEquals(foundItem.getItemId(), savedItem.getItemId());
        assertEquals(foundItem.getItemName(), savedItem.getItemName());
    }

    @Test
    public void testFindByNonExistentId() {
        Item foundItem = itemRepository.findById(999L).orElse(null);
        assertNull(foundItem);
    }

    @Test
    public void testDeleteItem() {
        Item savedItem = this.itemRepository.save(this.item);
        Item foundItem = this.itemRepository.findById(savedItem.getItemId()).orElse(null);

        assertNotNull(foundItem);

        this.itemRepository.delete(foundItem);
        Item deletedItem = this.itemRepository.findById(savedItem.getItemId()).orElse(null);

        assertNull(deletedItem);
    }

    @Test void testUpdateItem() {
        Item savedItem = this.itemRepository.save(this.item);
        savedItem.setItemName("Croissant");

        Item updatedItem = this.itemRepository.save(savedItem);
        assertEquals(updatedItem.getItemName(), "Croissant");
    }
}
