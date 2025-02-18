package com.pilarmichal.orderscasestudy.controller;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @PostMapping
    public String createOrder(@RequestBody String order) {
        return null;
    }

    @GetMapping
    public List<String> getOrders() {
        return null;
    }

    @GetMapping("/{index}")
    public String getOrder(@PathVariable int index) {
        return null;
    }

    @PutMapping("/{index}")
    public String updateOrder(@PathVariable int index, @RequestBody String newOrder) {
        return null;
    }

    @DeleteMapping("/{index}")
    public String deleteOrder(@PathVariable int index) {
        return null;
    }
}
