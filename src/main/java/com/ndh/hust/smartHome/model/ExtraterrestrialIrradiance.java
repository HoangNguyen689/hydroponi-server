package com.ndh.hust.smartHome.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "extraterrestrial_irradiance")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ExtraterrestrialIrradiance {
    @Id
    private String id;

    private String year;

    private String month;

    private String day;

    private String dayInYear;

    private String irradiance;
}
