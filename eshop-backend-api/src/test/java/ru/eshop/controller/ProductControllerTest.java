package ru.eshop.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.eshop.database.persist.BrandRepository;
import ru.eshop.database.persist.CategoryRepository;
import ru.eshop.database.persist.ProductRepository;
import ru.eshop.database.persist.model.Brand;
import ru.eshop.database.persist.model.Category;
import ru.eshop.database.persist.model.Product;

import javax.ws.rs.core.MediaType;
import java.math.BigDecimal;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
@AutoConfigureMockMvc
@SpringBootTest
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private BrandRepository brandRepository;

    @MockBean
    private RabbitTemplate rabbitTemplate;

    @MockBean
    private SimpMessagingTemplate simpMessagingTemplate;

    @BeforeEach
    public void init() {
        categoryRepository.save(new Category(1L, "testCategory"));
        brandRepository.save(new Brand(1L, "testBrand"));
        productRepository.save(new Product(1L, "testProduct", BigDecimal.valueOf(1234L), "testDesc",
                categoryRepository.getById(1L), brandRepository.getById(1L)));
    }

    @Test
    public void testProductDetails() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/products/all")
                        .param("categoryId", String.valueOf(1L))
                        .param("namePattern", "test")
                        .param("minPrice", String.valueOf(0))
                        .param("maxPrice", String.valueOf(99999))
                        .param("page", String.valueOf(1))
                        .param("size", String.valueOf(5))
                        .param("sortField", "id")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].title", is("testProduct")));
    }
}
