package com.ndh.hust.smartHome.fileProcess.beans;

import com.opencsv.bean.CsvBindByPosition;

public class Temperature extends CSVBean {
    @CsvBindByPosition(position = 0)
    private String time;
    @CsvBindByPosition(position = 1)
    private double temperature;

    @Override
    public String toString() {
        return time + "\t" + temperature;
    }
}
