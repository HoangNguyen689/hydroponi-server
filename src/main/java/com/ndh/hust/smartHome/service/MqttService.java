package com.ndh.hust.smartHome.service;

import org.apache.logging.log4j.Logger;
import org.eclipse.paho.client.mqttv3.*;

public abstract class MqttService implements MqttCallback {

    private String broker = "tcp://broker.hivemq.com:1883";
    private Logger log;

    MqttConnectOptions options = new MqttConnectOptions();
    IMqttClient mqttClient;
    String subcribeTopic;

    MqttService (String clientId, String subcribeTopic) {

        options.setCleanSession(true);

        this.subcribeTopic = subcribeTopic;

        try {
            mqttClient = new MqttClient(broker, clientId);
            //log.info("Connecting to broker: " + broker);
            mqttClient.setCallback(this);
            mqttClient.connect(options);
            mqttClient.subscribe(subcribeTopic);
        } catch (MqttException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void connectionLost(Throwable arg0) {
        log.warn("Connection lost!");
        while (!mqttClient.isConnected()) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            try {
                mqttClient.connect();
                mqttClient.setCallback(this);
                mqttClient.subscribe(subcribeTopic);
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
        log.info("Connected again!");
    }

    @Override
    public void messageArrived(String topic, MqttMessage message ) {

    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {

    }

}
