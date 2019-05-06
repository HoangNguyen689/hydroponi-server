package com.ndh.hust.smartHome.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.mongodb.MongoException;
import com.ndh.hust.smartHome.Repository.RecordRepository;
import com.ndh.hust.smartHome.model.Record;
import lombok.extern.log4j.Log4j2;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

@Service
@Log4j2(topic = "MQTT_IDENTIFY")
public class MqttIdentifyService extends MqttService {

    private String userName;
    private String passWord;

    MqttIdentifyService() {
        super("IDENTIFY_SERVICE_ID", "MQTT_IDENTIFY_NDH");
        if(mqttClient.isConnected()) {
            log.info("Ready to identify!");
        }
    }

    @Override
    public void messageArrived(String topic, MqttMessage message ) {
        if (!topic.equals(subscribeTopic)) {
            log.info("Message is not in this topic!");
        }
        else {
            String msg = new String(message.getPayload());
            log.info("Receive message: " + msg);

            ObjectMapper objectMapper = new ObjectMapper();
            try {
                JsonNode jsonNode = objectMapper.readTree(msg);
                userName = jsonNode.get("username").asText();
                passWord = jsonNode.get("password").asText();
            } catch (IOException e) {
                e.printStackTrace();
            }

            String reply = null;
            if (userName.equals("dev1")  && passWord.equals("dev1") ) {
                reply = "{\"message\" : \"OK\"}";
            }
            else {
                reply = "{\"message\" : \"NO\"}";
            }

            try {
                mqttClient.publish("MQTT_IDENTIFY_REPLY_NDH", reply.getBytes(), 2, true);
            }
            catch (MqttException e) {
                e.printStackTrace();
            }

            log.info("Publish identify reply message!");

        }

    }

}