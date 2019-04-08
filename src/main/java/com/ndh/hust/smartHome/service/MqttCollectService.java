package com.ndh.hust.smartHome.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.mongodb.MongoException;
import com.ndh.hust.smartHome.Repository.RecordRepository;
import com.ndh.hust.smartHome.model.Record;
import lombok.extern.log4j.Log4j2;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Service
@Log4j2(topic = "MQTT_COLLECT")
public class MqttCollectService extends MqttService {

    @Autowired
    private RecordRepository repository;

    private int recordCount = 0;
    private ArrayList<Record> listRecordTemp = new ArrayList<>();

    MqttCollectService() {
        super("COLLECT_SERVICE_ID", "MQTT_COLLECT_NDH");
        if(mqttClient.isConnected()) {
            log.info("Connected. Ready to collect data!");
        }
    }


    @Override
    public void messageArrived(String topic, MqttMessage message ) {
        if (!topic.equals(subcribeTopic)) {
            log.info("Message is not in this topic!");
        }
        else {
            String msg = new String(message.getPayload());
            log.info("Receive message: " + msg);

            ObjectMapper objectMapper = new ObjectMapper();
            Record r = null;
            try {
                r = objectMapper.readValue(msg,Record.class);
                listRecordTemp.add(r);
            } catch (UnrecognizedPropertyException ue) {
                System.out.println("Failed to excute message! Exiting ...");
                return;
            } catch (IOException e) {
                e.printStackTrace();
            }

            recordCount++;

            if (recordCount == 10) {
                Record rDAO = new Record();
                double humidDAO = 0.0;
                rDAO.setDeviceId(listRecordTemp.get(0).getDeviceId());
                for (Record recordTemp : listRecordTemp) {
                    humidDAO += recordTemp.getHumidity();
                }
                humidDAO /= listRecordTemp.size();
                rDAO.setHumidity(humidDAO);
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmm").format(Calendar.getInstance().getTime());
                rDAO.setTimeStamp(timeStamp);
                try {
                    repository.insert(rDAO);
                } catch (MongoException me) {
                    me.printStackTrace();
                    return;
                }

                recordCount = 0;
                listRecordTemp.clear();
            }
        }

    }

}