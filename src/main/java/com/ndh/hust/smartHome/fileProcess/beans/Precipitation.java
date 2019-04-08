package com.ndh.hust.smartHome.fileProcess.beans;

import com.opencsv.bean.CsvBindByPosition;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Precipitation extends CSVBean {
    @CsvBindByPosition(position = 0)
    private String year;
    @CsvBindByPosition(position = 1)
    private double jan;
    @CsvBindByPosition(position = 2)
    private double feb;
    @CsvBindByPosition(position = 3)
    private double mar;
    @CsvBindByPosition(position = 4)
    private double apr;
    @CsvBindByPosition(position = 5)
    private double may;
    @CsvBindByPosition(position = 6)
    private double jun;
    @CsvBindByPosition(position = 7)
    private double jul;
    @CsvBindByPosition(position = 8)
    private double aug;
    @CsvBindByPosition(position = 9)
    private double sep;
    @CsvBindByPosition(position = 10)
    private double oct;
    @CsvBindByPosition(position = 11)
    private double nov;
    @CsvBindByPosition(position = 12)
    private double dec;

    @Override
    public String toString() {
        return year + "\t" + jan;
    }
}
