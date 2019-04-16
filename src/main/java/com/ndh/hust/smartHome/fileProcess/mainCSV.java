package com.ndh.hust.smartHome.fileProcess;

import com.ndh.hust.smartHome.fileProcess.beans.CSVBean;
import com.ndh.hust.smartHome.fileProcess.beans.Temperature;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class mainCSV {
    public static void main(String[] args) throws Exception {

        CSVMappedToBean csvMappedToBean = new CSVMappedToBean();
        Path path = Paths.get(ClassLoader.getSystemResource("sample/temperature.csv").getPath());
        List<CSVBean> precipitations = csvMappedToBean.beanBuilder(path, Temperature.class);
//        for (CSVBean c : precipitations) {
//            System.out.println(c.toString());
//        }
        for (int i = 0 ; i < 100; i++) {
            System.out.println(precipitations.get(i).toString());
        }

    }

}
