package com.ndh.hust.smartHome.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
public class Method {
    @Id
    private String id;

    private String name;

    private double lowThreshole;

    private double highThreshole;
}
