package com.ndh.hust.smartHome.docfile;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

public class docfile {
    public static void main(String[] args) {
        Path path = Paths.get(ClassLoader.getSystemResource("sample/temperature.csv").getPath());

        BufferedReader br = null;
        BufferedWriter bw = null;

        try {
            br = new BufferedReader(new FileReader("/home/hoang/workspace/smartHome/src/main/resources/sample/temperature.csv"));
            bw = new BufferedWriter(new FileWriter("/home/hoang/workspace/smartHome/src/main/resources/sample/temperature1.csv", true));

            String text;
            String temp;
            String year,month,day;
            String[] element;
            String result;

            int i = 0;
            while ((text = br.readLine()) != null) {
                if ((i % 10) == 0) {
                    if (i > 0) {
                        temp = new String(text);
                        element = temp.split(",");

                        String[] time = element[0].split(" ");
                        String[] date = time[0].split("-");

                        result = date[0] + "," + date[1] + "," + date[2] + "," + time[1] + "," + element[1];
                        System.out.println(result);
                        bw.write(result + "\n");
                    }
                }
                i++;
                text  = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
