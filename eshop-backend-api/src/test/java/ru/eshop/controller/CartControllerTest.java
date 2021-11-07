package ru.eshop.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.eshop.database.persist.BrandRepository;
import ru.eshop.database.persist.CategoryRepository;
import ru.eshop.database.persist.ProductRepository;
import ru.eshop.database.persist.model.Brand;
import ru.eshop.database.persist.model.Category;
import ru.eshop.database.persist.model.Product;
import ru.eshop.dto.LineItemDto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
@AutoConfigureMockMvc
@SpringBootTest
public class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private RabbitTemplate rabbitTemplate;

    @MockBean
    private SimpMessagingTemplate simpMessagingTemplate;

    @BeforeEach
    public void init() {
        categoryRepository.save(new Category(1L, "testCategory"));
        brandRepository.save(new Brand(1L, "testBrand"));
        productRepository.save(new Product(1L, "testTitle", BigDecimal.valueOf(123L), "testDesc",
                categoryRepository.getById(1L), brandRepository.getById(1L)));
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void testAddToCart() throws Exception {
        Product product = productRepository.findById(1L).orElseThrow();
        LineItemDto item = new LineItemDto(product.getId(), 1, "color", "material");
        String response = mockMvc.perform(MockMvcRequestBuilders.post("/cart")
                .content(new ObjectMapper().writeValueAsString(item))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();
        List<Map<String, Object>> myObjects = mapper.readValue(response, new TypeReference<>() {
        });
        assertNotNull(myObjects);
        assertEquals(1, myObjects.size());
        assertEquals(1, myObjects.get(0).get("productId"));
    }
}
