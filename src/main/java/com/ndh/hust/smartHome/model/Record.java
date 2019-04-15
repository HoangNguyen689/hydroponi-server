package com.ndh.hust.smartHome.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.text.SimpleDateFormat;
import java.util.Calendar;


@NoArgsConstructor
@AllArgsConstructor
@Data
@Document
public class Record {
    @Id
    public String id;

    public String deviceId;

    public double temperature;

    public double humidity;

    public double moisture;

    public String timeStamp;


    public Record(double humidity) {
        this.humidity = humidity;
    }

    @Override
    public String toString() {
        return String.format("Record[id=%s, deviceId = %s, temperature='%f', humidity='%f', moisture='%f', time stamp: '%s']",
                id, deviceId, temperature, humidity, moisture, timeStamp);
    }
}
