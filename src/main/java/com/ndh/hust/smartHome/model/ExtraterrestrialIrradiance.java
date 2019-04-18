package com.ndh.hust.smartHome.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "extraterrestrial_irradiance")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExtraterrestrialIrradiance {
    @Id
    private String id;

    private String timeStamp;

    private int dayOfYear;

    private double irradiance;

    private double maxTemp;

    private double minTemp;

    private double evapo;
}
