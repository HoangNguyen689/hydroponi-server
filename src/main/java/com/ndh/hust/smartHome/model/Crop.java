package com.ndh.hust.smartHome.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;

@Document
@Data
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
