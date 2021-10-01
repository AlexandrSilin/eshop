package ru.eshop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.eshop.database.persist.model.Order;
import ru.eshop.database.persist.model.OrderStatus;
import ru.eshop.service.OrderService;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {
    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping()
    public List<Order> findAll(Authentication auth) {
        return orderService.findAllByUsername(auth.getName());
    }

    @PostMapping(produces = "application/json", consumes = "application/json")
    public void createOrder(Authentication auth) {
        orderService.createOrder(auth.getName());
    }

    @PostMapping("/{orderId}")
    public void changeOrderStatus(Authentication auth, @PathVariable Long orderId, OrderStatus orderStatus) {
        if (auth.isAuthenticated()) {
            orderService.changeOrderStatus(orderId, orderStatus);
        }
    }
}