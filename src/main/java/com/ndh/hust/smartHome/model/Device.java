package com.ndh.hust.smartHome.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
@NoArgsConstructor
public class Device {
    @Id
    private String id;

    private String username;

    private String password;
}
