package com.ndh.hust.smartHome.service;

import com.ndh.hust.smartHome.Repository.CropRepository;
import com.ndh.hust.smartHome.Repository.ExtraterrestrialIrradianceRepository;
import com.ndh.hust.smartHome.Repository.HarvestRepository;
import com.ndh.hust.smartHome.Repository.RecordRepository;
import com.ndh.hust.smartHome.base.Constant;
import com.ndh.hust.smartHome.model.ExtraterrestrialIrradiance;
import com.ndh.hust.smartHome.model.Harvest;
import com.ndh.hust.smartHome.model.Record;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class HelpService {

    @Autowired
    private RecordRepository recordRepository;

    @Autowired
    private TimeService timeService;

    @Autowired
    private HarvestRepository harvestRepository;

    @Autowired
    private CropRepository cropRepository;

    @Autowired
    private ExtraterrestrialIrradianceRepository extraterrestrialIrradianceRepository;

    @Autowired
    private EvapoHistoryService evapoHistoryService;

    public List<Record> getRecordsInDay(Date date) {
        return recordRepository.findByTimeStampBetween(timeService.getTimeString(timeService.startOfDay(date)),
                timeService.getTimeString(timeService.endOfDay(date)));

    }

    public double getMaxTemperature(Date date) {
        double maxTemp = 0.0;
        List<Record> records = getRecordsInDay(date);
        if (records == null)
            return 0.0;
        for(Record r : records) {
            if (maxTemp < r.getTemperature()) {
                maxTemp = r.getTemperature();
            }
        }
        return maxTemp;
    }

    public double getMinTemperature(Date date) {
        double minTemp = 80.0;
        List<Record> records = getRecordsInDay(date);
        if (records == null)
            return 0.0;
        for(Record r : records) {
            if (minTemp > r.getTemperature()) {
                minTemp = r.getTemperature();
            }
        }
        return minTemp;
    }

    public double getAverageRadian(Date date) {
        double radian = 0.0;
        List<Record> records = getRecordsInDay(date);
        if (records == null)
            return 0.0;
        for(Record r : records) {
            radian += r.getRadian();
        }
        radian /= records.size();
        return radian;
    }

    public boolean isInHarvest(Date date) {
        Harvest harvest = harvestRepository.findTopByActive(true);
        if (harvest == null)
            return false;

        try {
            Date startDate = new SimpleDateFormat("yyyy-MM-dd").parse(harvest.getTimeToStart());
            Date endDate = new SimpleDateFormat("yyyy-MM-dd").parse(harvest.getTimeToEnd());
            if (date.after(startDate) && date.before(endDate)) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public int getStateOfCrop(Date date) {
        Harvest harvest = harvestRepository.findTopByActive(true);
        int init = cropRepository.findByName(harvest.getCrop()).getInit();
        int mid = cropRepository.findByName(harvest.getCrop()).getDev() +
                cropRepository.findByName(harvest.getCrop()).getMid();

        if (isInHarvest(date)) {
            try {
                Date startDate = new SimpleDateFormat("yyyy-MM-dd").parse(harvest.getTimeToStart());
                DateTime startDateTime = new DateTime(startDate);
                DateTime initDateTime = startDateTime.plusDays(init);
                DateTime midDateTime = initDateTime.plusDays(mid);

                if (date.before(initDateTime.toDate())) {
                    return Constant.STATE_INIT;
                } else if (date.before(midDateTime.toDate())) {
                    return Constant.STATE_MID;
                } else {
                    return Constant.STATE_LATE;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return Constant.STATE_NONE;
    }

    public double getEvapoAvgDaily(int month) {
        List<ExtraterrestrialIrradiance> extraIrra = extraterrestrialIrradianceRepository.findByMonth(month);
        double evapoAvg = 0;
        for(ExtraterrestrialIrradiance e : extraIrra) {
            evapoAvg += e.getEvapo();
        }
        evapoAvg /= extraIrra.size();
        return evapoAvg;
    }

    public String getCronExpression() {
        Date date = new Date();
        LocalDateTime localDateTime = timeService.dateToLocalDateTime(date);

        String cronExp = null;
        int startMonth;
        int firstDay = 1;

        Harvest harvest = harvestRepository.findTopByActive(true);
        if (harvest == null) {
            return "";
        }

        try {
            Date start = new SimpleDateFormat("yyyyy-MM-dd").parse(harvest.getTimeToStart());
            LocalDateTime startLocal = timeService.dateToLocalDateTime(start);
            startMonth = startLocal.getMonthValue();
            if (localDateTime.getMonthValue() == startMonth) {
                firstDay = startLocal.getDayOfMonth();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        evapoHistoryService.init();
        Map<Integer, Integer> dayToPump = evapoHistoryService.irrigationFrequentList;

//        dayToPump.forEach((key, value) -> System.out.println(key + ":" + value));

//        evapoHistoryService.grossIrrigationList.forEach((key, value) -> System.out.println(key + ":" + value));

//        evapoHistoryService.preEffectiveList.forEach((key, value) -> System.out.println(key + ":" + value));

//        evapoHistoryService.evapoAvgDailyList.forEach((key, value) -> System.out.println(key + ":" + value));

//        Map<Integer, Integer> dayToPump = new HashMap<>();
//        for(int i = 1; i <= 12; i++) {
//            dayToPump.put(i, 1);
//        }


        switch (localDateTime.getMonthValue()) {
            case 1:
                cronExp = "0 0 16 " + firstDay + "/" + dayToPump.get(1) + " * ?";
                break;
            case 2:
                cronExp = "0 0 16 " + firstDay + "/" + dayToPump.get(2) + " * ?";
                break;
            case 3:
                cronExp = "0 0 16 " + firstDay + "/" + dayToPump.get(3) + " * ?";
                break;
            case 4:
                cronExp = "0 0 16 " + firstDay + "/" + dayToPump.get(4) + " * ?";
                break;
            case 5:
                cronExp = "0 0 16 " + firstDay + "/" + dayToPump.get(5) + " * ?";
//                cronExp = "0/1 * * * * ?";
                break;
            case 6:
                cronExp = "0 0 16 " + firstDay + "/" + dayToPump.get(6) + " * ?";
                break;
            case 7:
                cronExp = "0 0 16 " + firstDay + "/" + dayToPump.get(7) + " * ?";
                break;
            case 8:
                cronExp = "0 0 16 " + firstDay + "/" + dayToPump.get(8) + " * ?";
                break;
            case 9:
                cronExp = "0 0 16 " + firstDay + "/" + dayToPump.get(9) + " * ?";
                break;
            case 10:
                cronExp = "0 0 16 " + firstDay + "/" + dayToPump.get(10) + " * ?";
                break;
            case 11:
                cronExp = "0 0 16 " + firstDay + "/" + dayToPump.get(11) + " * ?";
                break;
            case 12:
                cronExp = "0 0 16 " + firstDay + "/" + dayToPump.get(12) + " * ?";
                break;
        }

        return cronExp;
    }
}
