package ru.eshop.service;

import ru.eshop.database.persist.model.OrderStatus;
import ru.eshop.dto.OrderDto;

import java.math.BigDecimal;
import java.util.List;

public interface OrderService {
    List<OrderDto> findAllByUsername(String username);

    void createOrder(String username, BigDecimal subtotal);

    void changeOrderStatus(Long orderId, OrderStatus orderStatus);
}
