package com.spring.controllers;

import com.spring.components.UserValidator;
import com.spring.models.Role;
import com.spring.models.User;
import com.spring.services.RoleService;
import com.spring.services.SecurityService;
import com.spring.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.stream.Collectors;

@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private UserValidator userValidator;

    @GetMapping("/registration")
    public String registration(Model model) {
        if (securityService.isAuthenticated()) {
            return "redirect:/";
        }

        model.addAttribute("allRoles", roleService.getRoles());
        model.addAttribute("userForm", new User());

        return "registration";
    }

    @PostMapping("/registration")
    public String registration(@ModelAttribute("userForm") User userForm, BindingResult bindingResult, Model model) {
        userValidator.validate(userForm, bindingResult);

        if (bindingResult.hasErrors()) {
            model.addAttribute("allRoles", roleService.getRoles());
            return "registration";
        }
        userService.save(userForm);

        securityService.autoLogin(userForm.getUsername(), userForm.getPasswordConfirm());

        return "redirect:/";
    }

    @GetMapping("/login")
    public String login(Model model, String error, String logout) {
        if (securityService.isAuthenticated()) {
            return "redirect:/";
        }

        if (error != null)
            model.addAttribute("error", "Ваш логин и/или пароль не верны.");

        if (logout != null)
            model.addAttribute("message", "Вы успешно вышли из аккаунта.");

        return "login";
    }

    @GetMapping("/user")
    public String getUser(Model model) {
        var user = userService.getUser();
        model.addAttribute("userRole", user.getRoles().stream().map(Role::getName).collect(Collectors.joining(", ")));
        model.addAttribute("allRoles", roleService.getRoles());
        model.addAttribute("userForm", user);
        return "user";
    }

    @PostMapping("/user")
    public String setUserRole(@ModelAttribute("userForm") User userForm, Model model) {
        var user = userService.getUser();
        user.setRoles(userForm.getRoles());
        userService.save(user);
        model.addAttribute("userRole", user.getRoles().stream().map(Role::getName).collect(Collectors.joining(", ")));
        model.addAttribute("allRoles", roleService.getRoles());
        model.addAttribute("userForm", userForm);
        return "user";
    }
}
