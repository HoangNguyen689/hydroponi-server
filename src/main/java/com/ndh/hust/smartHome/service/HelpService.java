package com.ndh.hust.smartHome.service;

import com.ndh.hust.smartHome.Repository.CropRepository;
import com.ndh.hust.smartHome.Repository.HarvestRepository;
import com.ndh.hust.smartHome.Repository.RecordRepository;
import com.ndh.hust.smartHome.base.Constant;
import com.ndh.hust.smartHome.model.Harvest;
import com.ndh.hust.smartHome.model.Record;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
            radian = r.getRadian();
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
                DateTime initDateTime = startDateTime.plus(init);
                DateTime midDateTime = initDateTime.plus(mid);
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
}
