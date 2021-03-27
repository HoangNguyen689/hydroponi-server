package com.ndh.hust.smartHome.controller;

import com.ndh.hust.smartHome.Chart.TemperatureChart;
import com.ndh.hust.smartHome.model.domain.User;
import com.ndh.hust.smartHome.service.userService.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ChartController {
//    @Autowired
//    private RecordRepository recordRepository;
//
//    @GetMapping(value = "/chart")
//    @ResponseBody
//    public String chart(Model model) {
//        double humid = recordRepository.findTopByOrderByTimeStampDesc().getHumidity();
//        model.addAttribute("humid", humid);
//        return "chart";
//    }

//    @GetMapping("/chart")
//    public ResponseEntity<?> chart() {
//        double humid = recordRepository.findTopByOrderByTimeStampDesc().getHumidity();
//        return ResponseEntity.ok(humid);
//    }

    @Autowired
    private CustomUserDetailsService userService;

    @GetMapping(value = "/data", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getData() {
        return ResponseEntity.ok(new TemperatureChart());
    }

    @GetMapping("chart")
    public String showChart(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByUsername(auth.getName());
        model.addAttribute("currentUser", user);

        return "chart";
    }
}
