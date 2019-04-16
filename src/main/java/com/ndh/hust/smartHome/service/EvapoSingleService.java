package com.ndh.hust.smartHome.service;

import com.ndh.hust.smartHome.Repository.CropRepository;
import com.ndh.hust.smartHome.Repository.HarvestRepository;
import com.ndh.hust.smartHome.base.Constant;
import com.ndh.hust.smartHome.model.Crop;
import com.ndh.hust.smartHome.model.Harvest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class EvapoSingleService {

    @Autowired
    private HarvestRepository harvestRepository;

    @Autowired
    private CropRepository cropRepository;

    @Autowired
    private HelpService helpService;

    @Autowired
    private TimeService timeService;

    /*
     * compute evaptranspiration in mm
     */
    public double computeET() {
        double ET = 0.0;
        double maxTemp;
        double minTemp;
        double radian;

        maxTemp = helpService.getMaxTemperature(timeService.getTimeYesterday(new Date()));
        minTemp = helpService.getMinTemperature(timeService.getTimeYesterday(new Date()));
        radian = helpService.getAverageRadian(timeService.getTimeYesterday(new Date()));

        ET = 0.0023 * radian * 0.0864 / 2.45 * ((maxTemp + minTemp)/2 + 17.8) * Math.sqrt(maxTemp - minTemp);

        return ET;
    }

    public int comuteTimeToPump() {
        Harvest harvest = harvestRepository.findTopByActive(true);
        Crop crop = cropRepository.findByName(harvest.getCrop());
        double kc = .0;

        switch (helpService.getStateOfCrop(new Date())) {
            case Constant.STATE_INIT: kc = crop.getKcinit(); break;
            case Constant.STATE_MID:  kc = crop.getKcmid(); break;
            case Constant.STATE_LATE: kc = crop.getKcend(); break;
            case Constant.STATE_NONE: break;
        }

        double ETc = kc * computeET();

        double amount = ETc / 1000 * harvest.getFieldArea();
        int timeToPump = (int) amount / harvest.getPumpCapacity();
        return timeToPump;
    }
}
