package com.pilarmichal.orderscasestudy.repository;

import com.pilarmichal.orderscasestudy.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
}
