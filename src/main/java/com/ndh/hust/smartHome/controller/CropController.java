package com.ndh.hust.smartHome.controller;

import com.ndh.hust.smartHome.Repository.CropRepository;
import com.ndh.hust.smartHome.model.Crop;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
public class CropController {
    @Autowired
    private CropRepository cropRepository;

    @RequestMapping("/crop/all-crop")
    public String allCrop(Model model) {
        model.addAttribute("crops", cropRepository.findAll());
        return "crop/all-crop";
    }

    @RequestMapping("/crop/add-crop")
    public String addCrop(Model model) {
        model.addAttribute("crop", new Crop());
        return "crop/add-crop";
    }

    @RequestMapping("/crop/saveCrop")
    public String insertCrop(@ModelAttribute("Crop") Crop crop, Model model) {
        crop.setTotal(crop.getInit() + crop.getDev() + crop.getMid() + crop.getLate());
        cropRepository.save(crop);
        model.addAttribute("crops", cropRepository.findAll());
        return "crop/all-crop";
    }

    @GetMapping("/crop/edit/{id}")
    public String showUpdateForm(@PathVariable("id") String id, Model model ) {
        Crop crop = cropRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid crop Id" + id));
        model.addAttribute("crop", crop);
        return "crop/update-crop";
    }

    @PostMapping("/crop/update/{id}")
    public String updateCrop(@PathVariable("id") String id, @Valid Crop crop, Model model) {
        cropRepository.save(crop);
        model.addAttribute("crops", cropRepository.findAll());
        return "crop/all-crop";
    }

    @GetMapping("/crop/delete/{id}")
    public String deleteCrop(@PathVariable("id") String id, Model model) {
        Crop crop = cropRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid crop Id" + id));
        cropRepository.delete(crop);
        model.addAttribute("crops", cropRepository.findAll());
        return "crop/all-crop";
    }
}
