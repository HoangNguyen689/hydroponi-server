package com.ndh.hust.smartHome.controller;

import com.ndh.hust.smartHome.Repository.RecordRepository;
import com.ndh.hust.smartHome.model.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ChartController {
    @Autowired
    private RecordRepository recordRepository;

    @GetMapping(value = "/chart")
    @ResponseBody
    public String chart(Model model) {
        double humid = recordRepository.findTopByOrderByTimeStampDesc().getHumidity();
        model.addAttribute("humid", humid);
        return "chart";
    }

//    @GetMapping("/chart")
//    public ResponseEntity<?> chart() {
//        double humid = recordRepository.findTopByOrderByTimeStampDesc().getHumidity();
//        return ResponseEntity.ok(humid);
//    }
}
