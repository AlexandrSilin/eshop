package ru.eshop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.eshop.database.persist.model.OrderStatus;
import ru.eshop.dto.OrderDto;
import ru.eshop.service.OrderService;

import java.math.BigDecimal;
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
    public List<OrderDto> findAll(Authentication auth) {
        return orderService.findAllByUsername(auth.getName());
    }

    @PostMapping(produces = "application/json", consumes = "application/json")
    public void createOrder(Authentication auth, @RequestBody BigDecimal subtotal) {
        orderService.createOrder(auth.getName(), subtotal);
    }

    @PostMapping("/{orderId}")
    public void changeOrderStatus(Authentication auth, @PathVariable Long orderId, OrderStatus orderStatus) {
        if (auth.isAuthenticated()) {
            orderService.changeOrderStatus(orderId, orderStatus);
        }
    }
}