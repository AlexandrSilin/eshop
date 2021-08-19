package ru.eshop.admin.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.eshop.admin.dto.CategoryDto;
import ru.eshop.admin.exceptions.NotFoundException;
import ru.eshop.admin.service.CategoryService;

import javax.validation.Valid;

@Controller
@RequestMapping("/categories")
public class CategoryController {
    private final Logger logger = LoggerFactory.getLogger(CategoryController.class);
    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public String categoriesPage(Model model, CategoryListParams params) {
        logger.info("Categories page requested");
        model.addAttribute("categories", categoryService.findWithFilter(params));
        return "categories";
    }

    @GetMapping("/new")
    public String newCategory(Model model) {
        logger.info("New category page requested");
        model.addAttribute("category", new CategoryDto());
        return "category_info";
    }

    @GetMapping("/{id}")
    public String editCategory(Model model, @PathVariable("id") Long id) {
        logger.info("Edit category page requested");
        model.addAttribute("category", categoryService.findById(id)
                .orElseThrow(() -> new NotFoundException("Category not found")));
        return "category_info";
    }

    @PostMapping
    public String update(@Valid @ModelAttribute("category") CategoryDto category, BindingResult result, Model model) {
        logger.info("Categories update page requested");
        if (result.hasErrors()) {
            logger.error(String.valueOf(result.getAllErrors()));
            return "category_info";
        }
        categoryService.save(category);
        return "redirect:/categories";
    }

    @DeleteMapping("/{id}")
    public String deleteCategory(@PathVariable("id") Long id) {
        logger.info("Deleting category with id {}", id);
        categoryService.deleteById(id);
        return "redirect:/categories";
    }

    @ExceptionHandler
    public ModelAndView notFoundException(NotFoundException exception) {
        ModelAndView modelAndView = new ModelAndView("not_found_form");
        modelAndView.addObject("message", exception.getMessage());
        modelAndView.setStatus(HttpStatus.NOT_FOUND);
        return modelAndView;
    }
}
