package com.ndh.hust.smartHome.service;

import com.ndh.hust.smartHome.Repository.CropRepository;
import com.ndh.hust.smartHome.model.Crop;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EvapotranspirationService {

    private String cropName = "broccoli";

    @Autowired
    private CropRepository cropRepository;

    private Crop crop;

    private double soilWaterHoldingCapicity = 0.1;

    private double cropCoeficientInit = 0.15;
    private double cropCoeficientMiddle = 0.9;
    private double cropCoeficientLast = 0.75;

    private double effectiveDepthRoot = 0.13;

    private List<Double> ET0s = new ArrayList<Double>();
    private List<Double> ETc = new ArrayList<Double>();
    private List<Double> precipitations = new ArrayList<Double>();
    private List<Double> effectivePrecipitations = new ArrayList<>();

    private double irrigationEffectiveMethod = 0.8;
    private double effectiveWetDiameter = 118;
    private double floawRate = 1.2;

    private double MAD = 0.5;
    private double TAW = 0.5;

    private double netIrrigation;
    private double grossIrrigation;

    private double precipitationRate;

    private double irrigationRate;
    private double irrigationFrequent;

    private double computeET0() {
        return 0.0;
    }

    void testPump() {
        crop = cropRepository.findByName(cropName);


        ET0s.add(1.1);
        ET0s.add(1.2);
        ET0s.add(1.3);
        ET0s.add(1.4);

        precipitations.add(20.0);
        precipitations.add(20.0);
        precipitations.add(20.0);
        precipitations.add(20.0);

        for(int i = 0; i < ET0s.size(); i++) {
            ETc.add(ET0s.get(i) * cropCoeficientInit);
        }

        for(int i = 0; i < precipitations.size(); i++) {
            effectivePrecipitations.add(precipitations.get(i) * 0.8);
        }

        netIrrigation = ETc.get(0) - effectivePrecipitations.get(0);
        grossIrrigation = netIrrigation / irrigationEffectiveMethod;


        precipitationRate = floawRate / (Math.PI * Math.pow(effectiveWetDiameter/2, 2));

        irrigationRate = MAD * TAW / precipitationRate;

        irrigationFrequent = MAD * TAW / grossIrrigation;

    }

}
