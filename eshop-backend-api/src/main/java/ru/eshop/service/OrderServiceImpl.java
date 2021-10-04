package ru.eshop.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.eshop.database.persist.OrderRepository;
import ru.eshop.database.persist.ProductRepository;
import ru.eshop.database.persist.UserRepository;
import ru.eshop.database.persist.model.Order;
import ru.eshop.database.persist.model.OrderLineItem;
import ru.eshop.database.persist.model.OrderStatus;
import ru.eshop.database.persist.model.User;
import ru.eshop.dto.OrderDto;
import ru.eshop.dto.OrderLineItemDto;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final CartService cartService;

    @Autowired
    public OrderServiceImpl(UserRepository userRepository, OrderRepository orderRepository,
                            ProductRepository productRepository, CartService cartService) {
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.cartService = cartService;
    }

    @Override
    public List<OrderDto> findAllByUsername(String username) {
        logger.info("Get all by username = {}", username);
        return orderRepository.findAllByUsername(username).stream().map(order -> new OrderDto(
                order.getId(),
                order.getTime(),
                order.getStatus().toString(),
                order.getUser().getUsername(),
                order.getOrderLineItems().stream().map(lineItem -> new OrderLineItemDto(lineItem.getId(),
                                lineItem.getOrder().getId(), lineItem.getProduct().getId(), lineItem.getProduct().getTitle(),
                                lineItem.getPrice(), lineItem.getQty(), lineItem.getColor(), lineItem.getMaterial()))
                        .collect(Collectors.toList()),
                order.getPrice()
        )).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void createOrder(String username, BigDecimal subtotal) {
        logger.info("Create order for username = {}", username);
        if (cartService.getLineItems().isEmpty()) {
            logger.info("Cart is empty");
            return;
        }
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        Order order = new Order(null, LocalDateTime.now(), user, OrderStatus.CREATED, subtotal);
        order.setOrderLineItems(cartService.getLineItems().stream().map(lineItem -> new OrderLineItem(null,
                order,
                productRepository.findById(lineItem.getProductId())
                        .orElseThrow(() -> new RuntimeException("Product not found")),
                lineItem.getProductDto().getPrice(),
                lineItem.getQty(),
                lineItem.getColor(),
                lineItem.getMaterial())).collect(Collectors.toList()));
        orderRepository.save(order);
    }

    @Override
    public void changeOrderStatus(Long orderId, OrderStatus orderStatus) {
        logger.info("Change order status (order id = {}) to status = {}", orderId, orderStatus);
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus(orderStatus);
        orderRepository.save(order);
    }
}
