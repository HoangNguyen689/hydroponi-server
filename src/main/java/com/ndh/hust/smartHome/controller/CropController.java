package com.ndh.hust.smartHome.controller;

import com.ndh.hust.smartHome.Repository.CropRepository;
import com.ndh.hust.smartHome.model.Crop;
import com.ndh.hust.smartHome.model.domain.User;
import com.ndh.hust.smartHome.service.userService.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/crop")
public class CropController {
    @Autowired
    private CropRepository cropRepository;

    @Autowired
    private CustomUserDetailsService userService;

    @GetMapping("/all-crop")
    public String allCrop(Model model) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByUsername(auth.getName());
        model.addAttribute("currentUser", user);

        model.addAttribute("crops", cropRepository.findAll());
        model.addAttribute("crop", new Crop());
        return "crop/all-crop";
    }

    @GetMapping(value = "/all-crop/page/{page}")
    public String listCropsPageByPage(@PathVariable("page") int page, Model model) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findByUsername(auth.getName());
        model.addAttribute("currentUser", user);

        PageRequest pageable = PageRequest.of(page - 1, 5);
        Page<Crop> cropPage = cropRepository.findAll(pageable);
        int totalPages = cropPage.getTotalPages();
        if(totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1,totalPages).boxed().collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        model.addAttribute("activeCropList", true);
        model.addAttribute("cropList", cropPage.getContent());
        return "crop/all-crop-2";
    }

    @CrossOrigin
    @PostMapping("/all-crop")
    @ResponseBody
    public Crop insertCrop(@Valid @RequestBody Crop crop) {
        return cropRepository.save(crop);
    }

    @CrossOrigin
    @GetMapping("/{id}")
    public ResponseEntity<Crop> getCropById(@PathVariable("id") String id) {
        Crop crop = cropRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid crop Id" + id));
        if(crop == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(crop);
    }

    @CrossOrigin
    @PostMapping("/{id}")
    public ResponseEntity<Crop> updateCrop(@PathVariable("id") String id, @RequestBody @Valid Crop cropTemp) {

        Crop crop = cropRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid crop Id" + id));
        if(crop == null) {
            return ResponseEntity.notFound().build();
        }

        crop.setName(cropTemp.getName());
        crop.setInit(cropTemp.getInit());
        crop.setDev(cropTemp.getDev());
        crop.setMid(cropTemp.getMid());
        crop.setLate(cropTemp.getLate());
        crop.setTotal(cropTemp.getInit() + cropTemp.getMid() + cropTemp.getDev() + cropTemp.getLate());
        crop.setKcinit(cropTemp.getKcinit());
        crop.setKcmid(cropTemp.getKcmid());
        crop.setKcend(cropTemp.getKcend());
        Crop updatedCrop = cropRepository.save(crop);

        return ResponseEntity.ok(updatedCrop);
    }

    @CrossOrigin
    @DeleteMapping("/{id}")
    public ResponseEntity<Crop> deleteCrop(@PathVariable(value = "id") String id) {

        Crop crop = cropRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid crop Id" + id));
        if(crop == null) {
            return ResponseEntity.notFound().build();
        }
        cropRepository.delete(crop);
        return ResponseEntity.ok().build();
    }
}
