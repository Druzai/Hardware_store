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

/**
 * Контроллер для регистрации и входа пользователей.
 */
@Controller
public class UserController {
    /**
     * Служба для работы с пользователями.
     */
    @Autowired
    private UserService userService;

    /**
     * Служба для работы с ролями.
     */
    @Autowired
    private RoleService roleService;

    /**
     * Служба для работы с аутентификацией пользователей.
     */
    @Autowired
    private SecurityService securityService;

    /**
     * Служба для работы с проверкой логина и пароля от пользователей.
     */
    @Autowired
    private UserValidator userValidator;

    /**
     * Получение страницы регистрации.
     * @param model модель страницы
     * @return страница "registration"
     */
    @GetMapping("/registration")
    public String registration(Model model) {
        if (securityService.isAuthenticated()) {
            return "redirect:/";
        }

        model.addAttribute("allRoles", roleService.getRoles());
        model.addAttribute("userForm", new User());

        return "registration";
    }

    /**
     * Обработка регистрации пользователя.
     * @param userForm класс пользователя
     * @param bindingResult класс содержащий ошибки проверки при HTTP запросе
     * @param model модель страницы
     * @return перенаправление на адрес "/"
     */
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

    /**
     * Получение страницы входа.
     * @param model модель страницы
     * @param error строка ошибок
     * @param logout строка выхода из аккаунта
     * @return страница "login"
     */
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

    /**
     * Получение страницы личного кабинета пользователя.
     * @param model модель страницы
     * @return страница "user"
     */
    @GetMapping("/user")
    public String getUser(Model model) {
        var user = userService.getUser();
        model.addAttribute("userRole", user.getRoles().stream().map(Role::getName).collect(Collectors.joining(", ")));
        model.addAttribute("allRoles", roleService.getRoles());
        model.addAttribute("userForm", user);
        return "user";
    }

    /**
     * Обработка изменения роли пользователя.
     * @param userForm класс пользователя
     * @param model модель страницы
     * @return страница "user"
     */
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
