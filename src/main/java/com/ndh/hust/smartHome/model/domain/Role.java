package com.ndh.hust.smartHome.model.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
public class Role {
    @Id
    private String id;

    private String role;
}
