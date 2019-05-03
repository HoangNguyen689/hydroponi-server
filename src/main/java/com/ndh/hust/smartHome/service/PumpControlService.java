package com.ndh.hust.smartHome.service;

import com.ndh.hust.smartHome.Repository.HarvestRepository;
import com.ndh.hust.smartHome.model.Command;
import com.ndh.hust.smartHome.model.Harvest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ScheduledFuture;

@Service
@EnableScheduling
public class PumpControlService implements SchedulingConfigurer {
    @Autowired
    private MarkovService markovService;

    @Autowired
    private EvapoSingleService evapoSingleService;

    @Autowired
    private EvapoHistoryService evapoHistoryService;

    @Autowired
    private MqttControlService mqttControlService;

    @Autowired
    private HarvestRepository harvestRepository;

    @Autowired
    private HelpService helpService;


    TaskScheduler taskScheduler;
    private ScheduledFuture<?> jobMarkov;
    private ScheduledFuture<?> jobWaitForHarvestStart;
    private ScheduledFuture<?> jobEvapoSingle;
    private ScheduledFuture<?> jobEvapoHistory;
    private volatile boolean flag = false;

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(50);
        threadPoolTaskScheduler.setThreadNamePrefix("schedule-thread");
        threadPoolTaskScheduler.initialize();
        Harvest harvest = harvestRepository.findTopByActive(true);
//        if (harvest.getMethod().equals("markov")) {
//            jobMarkov(threadPoolTaskScheduler);
//        }
//        else {
//            jobEvapoSingle(threadPoolTaskScheduler);
//        }
//        jobEvapoHistory(threadPoolTaskScheduler);
        jobWaitForHarvestStart(threadPoolTaskScheduler);
        this.taskScheduler = threadPoolTaskScheduler;
        taskRegistrar.setTaskScheduler(threadPoolTaskScheduler);
    }

    private void jobMarkov(TaskScheduler taskScheduler) {
        jobMarkov = taskScheduler.schedule(new Runnable() {
            @Override
            public void run() {
                synchronized (this)  {
                    while (!flag) {
                        System.out.println("Wait for harvest!");
                        try {
                            wait(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                System.out.println(Thread.currentThread().getName() + "The task1 excuted at" + new Date());
                int action = markovService.actionChoose();
                int time = 0;
                switch (action) {
                    case 0: time = 10; break;
                    case 1: time = 20; break;
                    case 2: time = 30; break;
                }

                Command command = new Command("dev1","PUMP","ON", Integer.toString(time));
                mqttControlService.publishCommand(command);

            }
        }, triggerContext -> {
            String cronExp="0/5 * * * * ?";
            return new CronTrigger(cronExp).nextExecutionTime(triggerContext);
        });
    }

    private void jobEvapoSingle(TaskScheduler taskScheduler) {
        jobEvapoSingle = taskScheduler.schedule(new Runnable() {
            @Override
            public void run() {
                synchronized (this)  {
                    while (!flag) {
                        try {
                            wait(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }

                System.out.println(Thread.currentThread().getName() + "The task2 excuted at" + new Date());
                int time = evapoSingleService.computeTimeToPump();
                Command command = new Command("dev1","PUMP","ON", Integer.toString(time));
                mqttControlService.publishCommand(command);

            }
        }, triggerContext -> {
            String cronExp="0/10 * * * * ?";
            return new CronTrigger(cronExp).nextExecutionTime(triggerContext);
        });
    }

    private void jobEvapoHistory(TaskScheduler taskScheduler) {
        jobEvapoHistory = taskScheduler.schedule(new Runnable() {
            @Override
            public void run() {
                synchronized (this)  {
                    while (!flag) {
                        try {
                            wait(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                System.out.println(Thread.currentThread().getName() + "The task3 excuted at" + new Date());
                int time = evapoHistoryService.computeTimeToPump();
                Command command = new Command("dev1","PUMP","ON", Integer.toString(time));
                mqttControlService.publishCommand(command);

            }
        }, triggerContext -> {
            String cronExp = helpService.getCronExpression();
            System.out.println(cronExp);
            return new CronTrigger(cronExp).nextExecutionTime(triggerContext);
        });
    }

    public void jobWaitForHarvestStart(TaskScheduler taskScheduler) {
        jobWaitForHarvestStart = taskScheduler.schedule(() -> {
            Date date = new Date();
            Harvest harvest = harvestRepository.findTopByActive(true);
            try {
                Date startDate = new SimpleDateFormat("yyyy-MM-dd").parse(harvest.getTimeToStart());
                Date endDate = new SimpleDateFormat("yyyy-MM-dd").parse(harvest.getTimeToEnd());
                if (date.after(startDate) && date.before(endDate)) {
                    flag = true;
                } else {
                    flag = false;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }, triggerContext -> {
            String cronExp="0/1 * * * * ?";
            return new CronTrigger(cronExp).nextExecutionTime(triggerContext);
        });
    }
}
