package com.ndh.hust.smartHome.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
@NoArgsConstructor
public class Record {
    @Id
    public String id;

    public String deviceId;

    public double temperature;

    public double humidity;

    public double moisture;

    public double radian;

    public String timeStamp;

    @Override
    public String toString() {
        return String.format("Record[id=%s, deviceId = %s, temperature='%f', humidity='%f', moisture='%f', time stamp: '%s']",
                id, deviceId, temperature, humidity, moisture, timeStamp);
    }
}
