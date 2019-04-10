package com.ndh.hust.smartHome.service;

import com.ndh.hust.smartHome.Repository.RecordRepository;
import com.ndh.hust.smartHome.model.Command;
import com.ndh.hust.smartHome.model.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@EnableScheduling
@Service
public class MarkovService {

    @Autowired
    private MqttControlService mqttControlService;

    @Autowired
    private RecordRepository repository;

    private double humidPrevious = 0.0;

    private final double THRESHOLE1 = 0.15;
    private final double THRESHOLE2 = 0.59;

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

    public int actionChoose(double humidCurrent) {

        ArrayList<Record> listLast2Record = new ArrayList<>(repository.findTop2ByOrderByTimeStampDesc());

        humidPrevious = listLast2Record.get(1).getHumidity();

        double evarporationRate = 0.0;
        int stateCurrent = 0;
        int i;

        if(humidCurrent < THRESHOLE1) {
            stateCurrent = S_low;
        }
        else if(THRESHOLE1 <= humidCurrent && humidCurrent <= THRESHOLE2) {
            stateCurrent = S_ok;
        }
        else {
            stateCurrent = S_high;
        }

        /*
         * Caculate evarporation rate
         */
        evarporationRate = humidCurrent - humidPrevious;

        /*
         * Caculate Reward for 3 actions
         */
        double humidNext;
        double penalty;
        double alpha;
        double reward = -200;
        double r;
        int action = 0;
        for(i = 0; i <= 2; i++) {
            humidNext = humidCurrent + humidIncreaseByAction(i) - evarporationRate;

            if(THRESHOLE1 <= humidNext && humidNext <= THRESHOLE2) {
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
        humidPrevious = humidCurrent;
        return action;
    }

    //@Scheduled(cron = "0 45/5 12 * * ?")
    //@Scheduled(cron = "0 0/1 * * * ?")
    public void markovDecision() {
        Random rand = new Random();
        int actionXXX = actionChoose(0.1 + rand.nextDouble());
        System.out.println();

        Command command = new Command("dev1","PUMP","ON", Integer.toString(actionXXX));
        mqttControlService.publishCommand(command);
    }

}
