package com.ndh.hust.smartHome.model;


import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
public class Command {

    private String deviceId;

    private String actuatorName;

    private String action;

    private String additional;

}
