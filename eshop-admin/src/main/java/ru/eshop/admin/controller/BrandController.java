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
import ru.eshop.admin.dto.BrandDto;
import ru.eshop.admin.exceptions.NotFoundException;
import ru.eshop.admin.service.BrandService;

import javax.validation.Valid;

@Controller
@RequestMapping("/brands")
public class BrandController {
    private final Logger logger = LoggerFactory.getLogger(BrandController.class);
    private final BrandService brandService;

    @Autowired
    public BrandController(BrandService brandService) {
        this.brandService = brandService;
    }

    @GetMapping
    public String brandsPage(Model model, BrandListParams params) {
        logger.info("Brands page requested");
        model.addAttribute("brands", brandService.findWithFilter(params));
        return "brands";
    }

    @GetMapping("/new")
    public String newBrand(Model model) {
        model.addAttribute("brand", new BrandDto());
        return "brand_info";
    }

    @GetMapping("/{id}")
    public String editBrand(Model model, @PathVariable("id") Long id) {
        logger.info("Edit brand page requested");
        model.addAttribute("brand", brandService.findById(id)
                .orElseThrow(() -> new NotFoundException("Brand not found")));
        return "brand_info";
    }

    @PostMapping
    public String update(@Valid @ModelAttribute("brand") BrandDto brand, BindingResult result, Model model) {
        logger.info("Brands update page requested");
        if (result.hasErrors()) {
            logger.error(String.valueOf(result.getAllErrors()));
            return "brand_info";
        }
        brandService.save(brand);
        return "redirect:/brands";
    }

    @DeleteMapping("/{id}")
    public String deleteCategory(@PathVariable("id") Long id) {
        logger.info("Deleting category with id {}", id);
        brandService.deleteById(id);
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
