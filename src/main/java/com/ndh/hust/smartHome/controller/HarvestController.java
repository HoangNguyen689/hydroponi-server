package com.ndh.hust.smartHome.controller;

import com.ndh.hust.smartHome.Repository.CropRepository;
import com.ndh.hust.smartHome.Repository.HarvestRepository;
import com.ndh.hust.smartHome.model.Harvest;
import com.ndh.hust.smartHome.service.TimeService;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HarvestController {

    @Autowired
    private HarvestRepository harvestRepository;

    @Autowired
    private CropRepository cropRepository;

    @Autowired
    private TimeService timeService;

    @RequestMapping("harvest-save")
    public String saveHarvest(Model model) {
        model.addAttribute("harvest", new Harvest());
        model.addAttribute("cropModels", cropRepository.findAll() );
        return "harvest-save";
    }

    @RequestMapping("/saveHarvest")
    public String insertHarvest(@ModelAttribute("Harvest") Harvest harvest) {
        DateTime dt = new DateTime(harvest.getTimeToStart());
        DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd");
        int dayOfYear = dt.getDayOfYear();
        harvest.setDayOfYear(dayOfYear);
        harvest.setTimeToEnd(dtf.print(dt.plusDays(cropRepository.findByName(harvest.getCrop()).getTotal())));
        harvest.setActive(true);
        harvestRepository.save(harvest);
        return "index";
    }

    @GetMapping("/harvest")
    public String showHarvest(Model model) {
        model.addAttribute("harvest", harvestRepository.findTopByActive(true));
        return "harvest";
    }
}
