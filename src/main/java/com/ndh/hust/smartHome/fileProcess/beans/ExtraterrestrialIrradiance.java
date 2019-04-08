package com.ndh.hust.smartHome.fileProcess.beans;

import com.opencsv.bean.CsvBindByPosition;

public class ExtraterrestrialIrradiance extends CSVBean{
    @CsvBindByPosition(position = 0)
    private int year;
    @CsvBindByPosition(position = 1)
    private int mon;
    @CsvBindByPosition(position = 2)
    private int day;
    @CsvBindByPosition(position = 3)
    private int dayInYear;
    @CsvBindByPosition(position = 4)
    private double extraterrestrialIrradiance;
    @CsvBindByPosition(position = 5)
    private double irradiance;
    @CsvBindByPosition(position = 6)
    private double irradiationKJm2;
    @CsvBindByPosition(position = 7)
    private double irradiationWhm2;
}
