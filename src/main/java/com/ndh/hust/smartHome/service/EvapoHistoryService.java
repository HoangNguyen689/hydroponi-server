package com.ndh.hust.smartHome.service;

import com.ndh.hust.smartHome.Repository.HarvestRepository;
import com.ndh.hust.smartHome.Repository.PrecipitationRepository;
import com.ndh.hust.smartHome.model.Harvest;
import com.ndh.hust.smartHome.model.Precipitation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.util.HashMap;
import java.util.Map;

@Service
public class EvapoHistoryService {

    @Autowired
    private PrecipitationRepository precipitationRepository;

    @Autowired
    private HarvestRepository harvestRepository;

    @Autowired
    private HelpService helpService;

    Map<Integer, Double> preEffectiveList = new HashMap<>();

    Map<Integer, Double> evapoAvgDailyList = new HashMap<>();

    Map<Integer, Double> netIrrigationList = new HashMap<>();

    Map<Integer, Double> grossIrrigationList = new HashMap<>();

    Map<Integer, Integer> irrigationFrequentList = new HashMap<>();

    private double irrigationEffectiveMethod = 1;

    private double MAD = 0.5;

    private double TAW = 0.5;

    public void init() {
        for(int i = 1; i <= 12; i++) {
            evapoAvgDailyList.put(i, helpService.getEvapoAvgDaily(i));
        }

        Precipitation precipitation = precipitationRepository.findByYear(2018);
        double kChangePreci = 0.75;
        int year = 2018;

        preEffectiveList.put(1, kChangePreci * precipitation.getJan() / YearMonth.of(year,1).lengthOfMonth());
        preEffectiveList.put(2, kChangePreci * precipitation.getFeb() / YearMonth.of(year,2).lengthOfMonth());
        preEffectiveList.put(3, kChangePreci * precipitation.getMar() / YearMonth.of(year,3).lengthOfMonth());
        preEffectiveList.put(4, kChangePreci * precipitation.getApr() / YearMonth.of(year,4).lengthOfMonth());
        preEffectiveList.put(5, kChangePreci * precipitation.getMay() / YearMonth.of(year,5).lengthOfMonth());
        preEffectiveList.put(6, kChangePreci * precipitation.getJun() / YearMonth.of(year,6).lengthOfMonth());
        preEffectiveList.put(7, kChangePreci * precipitation.getJul() / YearMonth.of(year,7).lengthOfMonth());
        preEffectiveList.put(8, kChangePreci * precipitation.getAug() / YearMonth.of(year,8).lengthOfMonth());
        preEffectiveList.put(9, kChangePreci * precipitation.getSep() / YearMonth.of(year,9).lengthOfMonth());
        preEffectiveList.put(10,kChangePreci * precipitation.getOct() / YearMonth.of(year,10).lengthOfMonth());
        preEffectiveList.put(11,kChangePreci * precipitation.getNov() / YearMonth.of(year,11).lengthOfMonth());
        preEffectiveList.put(12,kChangePreci * precipitation.getOct() / YearMonth.of(year,12).lengthOfMonth());

        for(int i = 1; i <= 12; i++) {
            netIrrigationList.put(i, evapoAvgDailyList.get(i) - preEffectiveList.get(i));
            grossIrrigationList.put(i, netIrrigationList.get(i) / irrigationEffectiveMethod);
            irrigationFrequentList.put(i, (int) (MAD * TAW / grossIrrigationList.get(i)));
        }

    }

    public int computeTimeToPump() {

        Harvest harvest = harvestRepository.findTopByActive(true);

        double flowRate = harvest.getPumpCapacity();

        double effectiveWetDiameter = harvest.getFieldArea();

        double precipitationRate = flowRate / effectiveWetDiameter;

        int irrigationRate = (int) (MAD * TAW / precipitationRate);

        return irrigationRate;

    }
    
}
