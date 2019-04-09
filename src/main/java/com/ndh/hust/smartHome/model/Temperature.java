package com.ndh.hust.smartHome.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Temperature {
    @Id
    private String id;

    private int year;

    private int month;

    private int day;

    private int dayInYear;

    private String time;

    private double temperature;
}
