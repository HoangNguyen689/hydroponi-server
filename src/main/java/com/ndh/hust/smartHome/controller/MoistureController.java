package com.ndh.hust.smartHome.controller;

import com.ndh.hust.smartHome.Repository.RecordRepository;
import com.ndh.hust.smartHome.model.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class MoistureController {
    @Autowired
    private RecordRepository recordRepository;

    @RequestMapping(value="/moisture", method = RequestMethod.GET)
    public ModelAndView moisture() {
        ModelAndView modelAndView = new ModelAndView();

        Record r = recordRepository.findTopByOrderByTimeStampDesc();
        modelAndView.addObject("moisture", r);
        modelAndView.setViewName("moisture");
        return modelAndView;
    }
}
