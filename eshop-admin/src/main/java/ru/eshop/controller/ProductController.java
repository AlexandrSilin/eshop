package ru.eshop.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.eshop.database.persist.BrandRepository;
import ru.eshop.database.persist.CategoryRepository;
import ru.eshop.dto.BrandDto;
import ru.eshop.dto.CategoryDto;
import ru.eshop.dto.ProductDto;
import ru.eshop.exceptions.NotFoundException;
import ru.eshop.service.ProductService;

import javax.validation.Valid;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/products")
public class ProductController {
    private final Logger logger = LoggerFactory.getLogger(ProductController.class);
    private final ProductService productService;
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;

    @Autowired
    public ProductController(ProductService productService,
                             CategoryRepository categoryRepository,
                             BrandRepository brandRepository) {
        this.productService = productService;
        this.categoryRepository = categoryRepository;
        this.brandRepository = brandRepository;
    }

    @GetMapping
    public String productsPage(Model model, ProductListParams params) {
        logger.info("Products page requested");
        model.addAttribute("products", productService.findWithFilter(params));
        return "products";
    }

    @GetMapping("/new")
    public String newProduct(Model model) {
        logger.info("New product page requested");
        model.addAttribute("product", new ProductDto());
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("brands", brandRepository.findAll());
        return "product_info";
    }

    @GetMapping("/{id}")
    public String editProduct(Model model, @PathVariable("id") Long id) {
        logger.info("Edit product page requested");
        model.addAttribute("product", productService.findById(id).orElseThrow(() -> new NotFoundException("Product not found")));
        return addCategoriesAndBrands(model);
    }

    @PostMapping
    public String update(@Valid @ModelAttribute("product") ProductDto product, BindingResult result, Model model) {
        logger.info("Products update page requested");
        if (result.hasErrors()) {
            logger.error(String.valueOf(result.getAllErrors()));
            return addCategoriesAndBrands(model);
        }
        productService.save(product);
        return "redirect:/products";
    }

    private String addCategoriesAndBrands(Model model) {
        model.addAttribute("categories", categoryRepository.findAll().stream()
                .map(category -> new CategoryDto(category.getId(), category.getName())).collect(Collectors.toList()));
        model.addAttribute("brands", brandRepository.findAll().stream()
                .map(brand -> new BrandDto(brand.getId(), brand.getTitle())).collect(Collectors.toList()));
        return "product_info";
    }

    @DeleteMapping("/{id}")
    public String deleteProduct(@PathVariable("id") Long id) {
        logger.info("Deleting product with id {}", id);
        productService.deleteById(id);
        return "redirect:/products";
    }

    @ExceptionHandler
    public ModelAndView notFoundException(NotFoundException exception) {
        ModelAndView modelAndView = new ModelAndView("not_found_form");
        modelAndView.addObject("message", exception.getMessage());
        modelAndView.setStatus(HttpStatus.NOT_FOUND);
        return modelAndView;
    }
}
