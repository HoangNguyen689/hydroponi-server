package com.ndh.hust.smartHome.service;

import com.ndh.hust.smartHome.Repository.CropRepository;
import com.ndh.hust.smartHome.Repository.ExtraterrestrialIrradianceRepository;
import com.ndh.hust.smartHome.Repository.PrecipitationRepository;
import com.ndh.hust.smartHome.Repository.TemperatureRepository;
import com.ndh.hust.smartHome.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EvapotranspirationService {

    private String cropName = "broccoli";

    @Autowired
    private MqttControlService mqttControlService;

    @Autowired
    private CropRepository cropRepository;

    @Autowired
    private TemperatureRepository temperatureRepository;

    @Autowired
    private ExtraterrestrialIrradianceRepository extraterrestrialIrradianceRepository;

    @Autowired
    private PrecipitationRepository precipitationRepository;

    private Crop crop;

    private List<Double> ET0s = new ArrayList<>();
    private List<Double> Pm = new ArrayList<>();
    private List<Double> Pe = new ArrayList<>();

    private double soilWaterHoldingCapicity = 0.1;

    private double cropCoeficientInit = 0.15;

    private double effectiveDepthRoot = 3;

    private List<Double> ETc = new ArrayList<Double>();

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


    private void testPump() {
        crop = cropRepository.findByName(cropName);

        for(int i = 1; i < 366; i++) {
            ET0s.add(computeET0(Integer.toString(i)));
        }

        Pm.add(Double.valueOf(precipitationRepository.findByYear("2018").getJan()));
        Pm.add(Double.valueOf(precipitationRepository.findByYear("2018").getFeb()));
        Pm.add(Double.valueOf(precipitationRepository.findByYear("2018").getMar()));
        Pm.add(Double.valueOf(precipitationRepository.findByYear("2018").getApr()));
        Pm.add(Double.valueOf(precipitationRepository.findByYear("2018").getMay()));
        Pm.add(Double.valueOf(precipitationRepository.findByYear("2018").getJun()));
        Pm.add(Double.valueOf(precipitationRepository.findByYear("2018").getJul()));
        Pm.add(Double.valueOf(precipitationRepository.findByYear("2018").getAug()));
        Pm.add(Double.valueOf(precipitationRepository.findByYear("2018").getSep()));
        Pm.add(Double.valueOf(precipitationRepository.findByYear("2018").getOct()));
        Pm.add(Double.valueOf(precipitationRepository.findByYear("2018").getNov()));
        Pm.add(Double.valueOf(precipitationRepository.findByYear("2018").getDec()));


        double SF = (0.531747 + 0.295164 * effectiveDepthRoot - 0.057697 * Math.pow(effectiveDepthRoot, 2) +
                0.003804 * Math.pow(effectiveDepthRoot, 3));

        for(int i = 0 ; i < Pm.size(); i++) {
            double temp;
            double ET0ofamonth = 0.0;
            switch (i) {
                case 0:
                    for (int j = 1; j <= 31; j++) {
                        ET0ofamonth += ET0s.get(j-1);
                    }
                    System.out.println("ETofaMonth: " + ET0ofamonth);
                    break;
                case 1:
                    for (int j = 32; j <= 59; j++) {
                        ET0ofamonth += ET0s.get(j-1);
                    }
                    break;
                case 2:
                    for (int j = 60; j <= 90; j++) {
                        ET0ofamonth += ET0s.get(j-1);
                    }
                    break;
                case 3:
                    for (int j = 91; j <= 120; j++) {
                        ET0ofamonth += ET0s.get(j-1);
                    } break;
                case 4:
                    for (int j = 121; j <= 151; j++) {
                        ET0ofamonth += ET0s.get(j-1);
                    } break;
                case 5:
                    for (int j = 152; j <= 181; j++) {
                        ET0ofamonth += ET0s.get(j-1);
                    } break;
                case 6:
                    for (int j = 182; j <= 212; j++) {
                        ET0ofamonth += ET0s.get(j-1);
                    } break;
                case 7:
                    for (int j = 213; j <= 243; j++) {
                        ET0ofamonth += ET0s.get(j-1);
                    } break;
                case 8:
                    for (int j = 244; j <= 273; j++) {
                        ET0ofamonth += ET0s.get(j-1);
                    } break;
                case 9:
                    for (int j = 274; j <= 304; j++) {
                        ET0ofamonth += ET0s.get(j-1);
                    } break;
                case 10:
                    for (int j = 305; j <= 334; j++) {
                        ET0ofamonth += ET0s.get(j-1);
                    } break;
                case 11:
                    for (int j = 335; j <= 365; j++) {
                        ET0ofamonth += ET0s.get(j-1);
                    } break;

            }

            temp = SF * (0.70917 * Math.pow(Pm.get(i) * 0.0393701, 0.82416) - 0.11556) *
                    Math.pow(10, 0.2426 * ET0ofamonth * 0.0393701);
            Pe.add(temp / 0.0393701);
        }

        double ETcAvg = 0;

        for(int i = 91; i <= 120; i++) {
            ETc.add(ET0s.get(i) * crop.getKcinit());
            ETcAvg += ET0s.get(i) * crop.getKcinit();
        }

        ETcAvg /= 30;

        System.out.println("ETc: " + ETcAvg);
        System.out.println("Pm: " + Pm.get(0));
        System.out.println("Pe: " + Pe.get(0));

        netIrrigation = ETcAvg - Pe.get(0) / 30;

        System.out.println("net irrigation:" + netIrrigation);

        grossIrrigation = netIrrigation / irrigationEffectiveMethod;

        //TODO: hoi cac bac xem cong suat may bom va dien tich canh dong la bao nhieu

        precipitationRate = floawRate / (Math.PI * Math.pow(effectiveWetDiameter/2, 2));

        irrigationRate = MAD * TAW / precipitationRate;

        irrigationFrequent = MAD * TAW / grossIrrigation;

    }

//    @Scheduled(cron = "0 0 16 * * ?")
//    @Scheduled(cron = "0 * * * * ?")
    public void ETDecision() {
        testPump();
        Command command = new Command("dev1", "PUMP", "ON", Double.toString(irrigationRate));
        mqttControlService.publishCommand(command);
    }

}
