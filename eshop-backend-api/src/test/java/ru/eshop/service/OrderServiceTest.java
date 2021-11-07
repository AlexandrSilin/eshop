package ru.eshop.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import ru.eshop.database.persist.*;
import ru.eshop.database.persist.model.*;
import ru.eshop.dto.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
@SpringBootTest
public class OrderServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CartService cartService;

    @Autowired
    private OrderService orderService;

    @MockBean
    private RabbitTemplate rabbitTemplate;

    @MockBean
    private SimpMessagingTemplate simpMessagingTemplate;

    private static Product getExpectedProduct() {
        Category expectedCategory = new Category(1L, "testCategory");
        Brand expectedBrand = new Brand(1L, "testBrand");
        Product expectedProduct = new Product();
        expectedProduct.setId(1L);
        expectedProduct.setCategory(expectedCategory);
        expectedProduct.setBrand(expectedBrand);
        expectedProduct.setPrice(BigDecimal.valueOf(123L));
        expectedProduct.setDescription("testDesc");
        expectedProduct.setTitle("testProduct");
        List<Picture> pictureList = new ArrayList<>();
        pictureList.add(new Picture(1L, "testPic", "noContent", "testFilename", expectedProduct));
        expectedProduct.setPicture(pictureList);
        return expectedProduct;
    }

    @BeforeEach
    public void init() {
        userRepository.save(new User(1L, "admin", "admin", 20, new HashSet<>()));
        categoryRepository.save(new Category(1L, "testCategory"));
        brandRepository.save(new Brand(1L, "testBrand"));
        productRepository.save(getExpectedProduct());
        cartService.addProductQty(new ProductDto(1L, "testProduct", BigDecimal.valueOf(123L), "testDesc",
                new CategoryDto(1L, "testCategory"), new BrandDto(1L, "testBrand"),
                Collections.singletonList(1L)), "color", "material", 1);
    }

    @Test
    public void testOrderService() {
        orderService.createOrder("admin", BigDecimal.valueOf(123L));
        List<OrderDto> orders = orderService.findAllByUsername("admin");
        assertNotNull(orders);
        OrderDto order = orders.get(0);
        assertEquals(1L, order.getId());
        assertEquals("admin", order.getUsername());
        assertEquals(OrderStatus.CREATED.toString(), orders.get(0).getStatus());
        assertEquals(BigDecimal.valueOf(123L).setScale(2, RoundingMode.HALF_UP),
                order.getPrice().setScale(2, RoundingMode.HALF_UP));
        List<OrderLineItemDto> orderLineItemDtos = order.getOrderLineItems();
        assertEquals(1, orderLineItemDtos.size());
        OrderLineItemDto orderLineItemDto = orderLineItemDtos.get(0);
        assertEquals("color", orderLineItemDto.getColor());
        assertEquals("material", orderLineItemDto.getMaterial());
        assertEquals("testProduct", orderLineItemDto.getProductName());
        assertEquals(1L, orderLineItemDto.getProductId());
        orderService.changeOrderStatus(order.getId(), OrderStatus.CLOSED);
        assertEquals(OrderStatus.CLOSED.toString(), orderService.findAllByUsername("admin").get(0).getStatus());
    }
}
