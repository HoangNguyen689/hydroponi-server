package com.ndh.hust.smartHome.model;


import com.ndh.hust.smartHome.service.HelpService;
import com.ndh.hust.smartHome.service.TimeService;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Document
public class Command {

    @Id
    private String id;

    private String deviceId;

    private String actuatorName;

    private String action;

    private String additional;

    private String timeStamp;

    public Command(String deviceId, String actuatorName, String action, String additional) {
        this.deviceId = deviceId;
        this.actuatorName = actuatorName;
        this.action = action;
        this.additional = additional;
        this.timeStamp = new Date().toString();
    }

}
