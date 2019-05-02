package com.ndh.hust.smartHome.controller;

import com.ndh.hust.smartHome.model.domain.UserNDH;
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
        UserNDH user = userService.findByUsername(auth.getName());
        modelAndView.addObject("currentUser", user);

        return modelAndView;
    }

    @GetMapping(value = "/signup")
    public ModelAndView signup() {
        ModelAndView modelAndView = new ModelAndView();
        UserNDH userNDH = new UserNDH();
        modelAndView.addObject("userNDH", userNDH);
        modelAndView.setViewName("signup");

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserNDH user = userService.findByUsername(auth.getName());
        modelAndView.addObject("currentUser", user);

        return modelAndView;
    }

    @PostMapping(value = "/signup")
    public ModelAndView createNewUser(@Valid UserNDH userNDH, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView();
        UserNDH userExist = userService.findByUsername(userNDH.getUsername());

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserNDH user = userService.findByUsername(auth.getName());
        modelAndView.addObject("currentUser", user);

        if (userExist != null) {
            bindingResult
                    .rejectValue("username", "error.user",
                            "There is already a user registered with the username provided");
        }

        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("signup");
        } else {
            userService.saveUser(userNDH);
            modelAndView.addObject("successMessage", "User has been registered successfully");
            modelAndView.addObject("userNDH", new UserNDH());
            modelAndView.setViewName("login");
        }

        return modelAndView;
    }
}
