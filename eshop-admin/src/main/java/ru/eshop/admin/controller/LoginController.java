package ru.eshop.admin.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.eshop.admin.dto.RoleDto;
import ru.eshop.admin.dto.UserDto;
import ru.eshop.admin.persist.RoleRepository;
import ru.eshop.admin.service.UserService;

import javax.validation.Valid;
import java.util.stream.Collectors;

@Controller
public class LoginController {
    private final Logger logger = LoggerFactory.getLogger(LoginController.class);
    private RoleRepository roleRepository;
    private UserService userService;

    public LoginController() {

    }

    @Autowired
    public LoginController(RoleRepository roleRepository, UserService userService) {
        this.roleRepository = roleRepository;
        this.userService = userService;
    }

    @GetMapping("/login")
    public String loginPage() {
        logger.info("Login page requested");
        return "login_page";
    }

    @GetMapping("new_guest")
    public String newGuest(Model model) {
        logger.info("Register page requested");
        model.addAttribute("user", new UserDto());
        model.addAttribute("roles", roleRepository.findByName("ROLE_GUEST"));
        return "new_guest";
    }

    @PostMapping
    public String update(@Valid @ModelAttribute("user") UserDto user, BindingResult result, Model model) {
        if (result.hasErrors()) {
            logger.error(String.valueOf(result.getAllErrors()));
            model.addAttribute("roles", roleRepository.findAll().stream()
                    .map(role -> new RoleDto(role.getId(), role.getName())).collect(Collectors.toList()));
            return "new_guest";
        }
        if (!user.getPassword().equals(user.getRepeatPassword())) {
            logger.error("Repeated password is not correct");
            model.addAttribute("roles", roleRepository.findAll().stream()
                    .map(role -> new RoleDto(role.getId(), role.getName())).collect(Collectors.toList()));
            result.rejectValue("password", "", "Repeated password is not correct");
            return "new_guest";
        }
        userService.save(user);
        return "redirect:/login";
    }

    @GetMapping("/access_denied")
    public String accessDenied() {
        logger.info("Access denied");
        return "access_denied";
    }
}
