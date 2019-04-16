package com.ndh.hust.smartHome.docfile;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class docfile4 {
    public static void main(String[] args) {
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter("/home/hoang/abv.csv", true));
            bw.write("lalal");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
