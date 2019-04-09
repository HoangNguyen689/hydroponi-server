package com.ndh.hust.smartHome.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Crop {

    @Id
    private String id;

    private String name;

    private int init;

    private int dev;

    private int mid;

    private int late;

    private int total;

    private double kcinit;

    private double kcmid;

    private double kcend;
}
