package com.ndh.hust.smartHome.service;

import com.ndh.hust.smartHome.Repository.CropRepository;
import com.ndh.hust.smartHome.Repository.ExtraterrestrialIrradianceRepository;
import com.ndh.hust.smartHome.Repository.TemperatureRepository;
import com.ndh.hust.smartHome.model.Crop;
import com.ndh.hust.smartHome.model.ExtraterrestrialIrradiance;
import com.ndh.hust.smartHome.model.Temperature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EvapotranspirationService {

    private String cropName = "broccoli";

    @Autowired
    private CropRepository cropRepository;

    @Autowired
    private TemperatureRepository temperatureRepository;

    @Autowired
    private ExtraterrestrialIrradianceRepository extraterrestrialIrradianceRepository;

    private Crop crop;

    private List<Double> ET0s = new ArrayList<>();

    private double soilWaterHoldingCapicity = 0.1;

    private double cropCoeficientInit = 0.15;
    private double cropCoeficientMiddle = 0.9;
    private double cropCoeficientLast = 0.75;

    private double effectiveDepthRoot = 0.13;

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

    private double computeET0(String dayInYear) {
        ExtraterrestrialIrradiance ex = extraterrestrialIrradianceRepository.findByDayInYear(dayInYear);
        double radiance = Double.valueOf(ex.getIrradiance());

        List<Temperature> temps = temperatureRepository.findByDayInYear(dayInYear);

        double maxTemp = 0,minTemp = 40;
        for (Temperature t : temps) {
            if (Double.valueOf(t.getTemperature()) > maxTemp) {
                maxTemp = Double.valueOf(t.getTemperature());
            }

            if (Double.valueOf(t.getTemperature()) < minTemp) {
                minTemp = Double.valueOf(t.getTemperature());
            }
        }

        double avgTemp = (maxTemp + minTemp) / 2;

        double ET = 0.0;

        ET = 0.0023 * radiance * 0.0864 / 2.45 * (avgTemp + 17.8) * Math.sqrt(maxTemp - minTemp);

        return ET;
    }

    @Scheduled(cron = "0 * * * * ?")
    void testPump() {
        crop = cropRepository.findByName(cropName);

        for(int i = 1; i < 366; i++) {
            ET0s.add(computeET0(Integer.toString(i)));
        }

        for(Double d : ET0s) {
            System.out.println(d);
        }

        System.out.println(crop.getKcinit());

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
