package com.ndh.hust.smartHome.controller;

import com.ndh.hust.smartHome.model.domain.User;
import com.ndh.hust.smartHome.service.userService.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
public class LoginController {

    @Autowired
    private CustomUserDetailsService userService;

    @GetMapping(value = "/login")
    public ModelAndView login() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByUsername(auth.getName());
        modelAndView.addObject("currentUser", user);

        return modelAndView;
    }

    @GetMapping(value = "/register")
    public ModelAndView register() {
        ModelAndView modelAndView = new ModelAndView();
        User user = new User();
        modelAndView.addObject("userNDH", user);
        modelAndView.setViewName("register");

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User registeredUser = userService.findByUsername(auth.getName());
        modelAndView.addObject("currentUser", registeredUser);

        return modelAndView;
    }

    @PostMapping(value = "/register")
    public ModelAndView createNewUser(@Valid User user, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView();
        User userExist = userService.findByUsername(user.getUsername());

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User registeredUser = userService.findByUsername(auth.getName());
        modelAndView.addObject("currentUser", registeredUser);

        if (userExist != null) {
            bindingResult
                    .rejectValue("username", "error.user",
                            "There is already a user registered with the username provided");
        }

        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("register");
        } else {
            userService.saveUser(user);
            modelAndView.addObject("successMessage", "User has been registered successfully");
            modelAndView.addObject("userNDH", new User());
            modelAndView.setViewName("login");
        }

        return modelAndView;
    }
}
