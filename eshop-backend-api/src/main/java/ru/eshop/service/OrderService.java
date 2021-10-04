package ru.eshop.service;

import ru.eshop.database.persist.model.Order;
import ru.eshop.database.persist.model.OrderStatus;

import java.util.List;

public interface OrderService {
    List<Order> findAllByUsername(String username);

    void createOrder(String username);

    void changeOrderStatus(Long orderId, OrderStatus orderStatus);
}
