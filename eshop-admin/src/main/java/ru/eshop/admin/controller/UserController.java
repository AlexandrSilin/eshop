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
import ru.eshop.admin.dto.RoleDto;
import ru.eshop.admin.dto.UserDto;
import ru.eshop.admin.exceptions.NotFoundException;
import ru.eshop.admin.service.UserService;
import ru.eshop.database.persist.RoleRepository;

import javax.validation.Valid;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/users")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;
    private final RoleRepository roleRepository;

    @Autowired
    public UserController(UserService userService, RoleRepository roleRepository) {
        this.userService = userService;
        this.roleRepository = roleRepository;
    }

    @GetMapping
    public String usersPage(Model model, UserListParams params) {
        logger.info("User list page requested");
        model.addAttribute("users", userService.findWithFilter(params));
        return "users";
    }

    @GetMapping("/new")
    public String newUser(Model model) {
        logger.info("New user page requested");
        model.addAttribute("user", new UserDto());
        model.addAttribute("roles", roleRepository.findAll());
        return "user_info";
    }

    @GetMapping("/{id}")
    public String editUser(Model model, @PathVariable("id") Long id) {
        logger.info("Edit user page requested");
        model.addAttribute("user", userService.findById(id).orElseThrow(() -> new NotFoundException("User not found")));
        model.addAttribute("roles", roleRepository.findAll().stream()
                .map(role -> new RoleDto(role.getId(), role.getName())).collect(Collectors.toList()));
        return "user_info";
    }

    @PostMapping
    public String update(@Valid @ModelAttribute("user") UserDto user, BindingResult result, Model model) {
        logger.info("Users update page requested");
        if (result.hasErrors()) {
            logger.error(String.valueOf(result.getAllErrors()));
            model.addAttribute("roles", roleRepository.findAll().stream()
                    .map(role -> new RoleDto(role.getId(), role.getName())).collect(Collectors.toList()));
            return "user_info";
        }
        if (!user.getPassword().equals(user.getRepeatPassword())) {
            logger.error("Repeated password is not correct");
            model.addAttribute("roles", roleRepository.findAll().stream()
                    .map(role -> new RoleDto(role.getId(), role.getName())).collect(Collectors.toList()));
            result.rejectValue("password", "", "Repeated password is not correct");
            return "user_info";
        }
        userService.save(user);
        return "redirect:/users";
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        logger.info("Deleting user with id {}", id);
        userService.deleteById(id);
        return "redirect:/users";
    }

    @ExceptionHandler
    public ModelAndView notFoundException(NotFoundException exception) {
        ModelAndView modelAndView = new ModelAndView("not_found_form");
        modelAndView.addObject("message", exception.getMessage());
        modelAndView.setStatus(HttpStatus.NOT_FOUND);
        return modelAndView;
    }
}
