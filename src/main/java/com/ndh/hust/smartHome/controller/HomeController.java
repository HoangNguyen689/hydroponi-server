package com.ndh.hust.smartHome.controller;

import com.ndh.hust.smartHome.model.domain.User;
import com.ndh.hust.smartHome.service.userService.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Autowired
    private CustomUserDetailsService userService;

    @GetMapping(value = "/")
    public String index(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User userNDH = userService.findByUsername(auth.getName());
        model.addAttribute("currentUser", userNDH);
        return "index";
    }
}
