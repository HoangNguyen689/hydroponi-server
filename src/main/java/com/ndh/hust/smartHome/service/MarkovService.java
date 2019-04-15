package com.ndh.hust.smartHome.service;

import com.ndh.hust.smartHome.Repository.RecordRepository;
import com.ndh.hust.smartHome.model.Command;
import com.ndh.hust.smartHome.model.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;


import java.util.*;

@EnableScheduling
@Service
public class MarkovService {

    @Autowired
    private MqttControlService mqttControlService;

    @Autowired
    private TimeService timeService;

    @Autowired
    private RecordRepository repository;

    private double moisPre = 0.0;
    private double moisCur = 0.0;

    private final double THRESHOLE1 = 0.6;
    private final double THRESHOLE2 = 0.8;

    private final int S_low = -1;
    private final int S_ok = 0;
    private final int S_high = 1;

    private final int a0 = 0;
    private final int a1 = 1;
    private final int a2 = 2;

    private double humidIncreaseByAction(int action) {
        double w = 0.0;
        switch(action) {
            case a0:
                w = 0;
                break;
            case a1:
                w = 0.05;
                break;
            case a2:
                w = 0.2;
                break;
        }
        return w;
    }

    public int actionChoose() {

        Date timeNow = timeService.getTimeNowInHour();
        String timeCur = timeService.getTimeString(timeNow);
        String timePre = timeService.getTimeString(timeService.getTimeInPreviousHour(timeNow));

        Record moisCurRecord = repository.findByTimeStampBetween(timePre, timeCur);

        if (moisCurRecord != null) {
            moisCur = moisCurRecord.getMoisture();
            System.out.println(moisCur);
        } else {
            moisCur = 0.6;
        }

        Date timeYesterday = timeService.getTimeYesterday(timeService.getTimeNowInHour());
        String timeCurYesterday = timeService.getTimeString(timeYesterday);
        String timePreYesterday = timeService.getTimeString(timeService.getTimeInPreviousHour(timeYesterday));

        Record moisPreRecord = repository.findByTimeStampBetween(timePreYesterday, timeCurYesterday);
        if (moisPreRecord != null) {
            moisPre = moisPreRecord.getMoisture();
        } else {
            moisPre = 0.58;
        }

        double evarporationRate = 0.0;
        int stateCurrent = 0;
        int i;

        if(moisCur < THRESHOLE1) {
            stateCurrent = S_low;
        }
        else if(THRESHOLE1 <= moisCur && moisCur <= THRESHOLE2) {
            stateCurrent = S_ok;
        }
        else {
            stateCurrent = S_high;
        }

        evarporationRate = moisCur - moisPre;

        /*
         * Caculate Reward for 3 actions
         */
        double moisNext;
        double penalty;
        double alpha;
        double reward = -200;
        double r;
        int action = 0;
        for(i = 0; i <= 2; i++) {
            moisNext = moisCur + humidIncreaseByAction(i) - evarporationRate;

            if(THRESHOLE1 <= moisNext && moisNext <= THRESHOLE2) {
                penalty = 0;
            }
            else {
                penalty = 100;
            }

            alpha = (humidIncreaseByAction(2) - humidIncreaseByAction(1)) / (0.1*(a2-a1));
            r = -alpha * stateCurrent * evarporationRate * i - humidIncreaseByAction(i) - penalty;

            if(r > reward) {
                reward = r;
                action = i;
            }
        }

        return action;
    }

    //@Scheduled(cron = "0 45/5 12 * * ?")
    @Scheduled(cron = "0 * * * * ?")
    public void markovDecision() {
        Random rand = new Random();
        int actionXXX = actionChoose();
        System.out.println();

        Command command = new Command("dev1","PUMP","ON", Integer.toString(actionXXX));
        mqttControlService.publishCommand(command);
    }

}
