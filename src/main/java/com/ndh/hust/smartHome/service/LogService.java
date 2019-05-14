package com.ndh.hust.smartHome.service;

import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.FileWriter;

@Service
public class LogService {

    public void wirteLine(String text) throws Exception{
        FileWriter fw = new FileWriter("/home/hoang/workspace/smartHome/src/main/resources/log.txt",true);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(text+"\n");
        bw.close();

    }
}
