package com.ndh.hust.smartHome.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="harvest")
@Data
public class Harvest {
    @Id
    private String id;

    private String crop;

    private String timeToStart;

    private String timeToEnd;

    private int dayOfYear;

    private double pumpCapacity;

    private double fieldArea;

    private String method;

    private boolean active;
}
