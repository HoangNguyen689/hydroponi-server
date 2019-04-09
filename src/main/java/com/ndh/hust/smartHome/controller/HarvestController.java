package com.ndh.hust.smartHome.controller;

import com.ndh.hust.smartHome.Repository.CropRepository;
import com.ndh.hust.smartHome.Repository.HarvestRepository;
import com.ndh.hust.smartHome.model.Harvest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HarvestController {

    @Autowired
    private HarvestRepository harvestRepository;

    @Autowired
    private CropRepository cropRepository;

    @RequestMapping("harvest-save")
    public String saveHarvest(Model model) {
        model.addAttribute("harvest", new Harvest());
        model.addAttribute("cropModels", cropRepository.findAll() );
        return "harvest-save";
    }

    @RequestMapping("/saveHarvest")
    public String insertHarvest(@ModelAttribute("Harvest") Harvest harvest) {

        harvestRepository.save(harvest);
        return "index";
    }
}
