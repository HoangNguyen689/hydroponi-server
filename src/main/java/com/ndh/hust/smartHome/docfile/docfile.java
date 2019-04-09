package com.ndh.hust.smartHome.docfile;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class docfile {

    @Setter
    @Getter
    @AllArgsConstructor
    public static class Tripple {
        public int year;
        public int month;
        public int day;
    }

    static List<Tripple> tripples = new ArrayList<>();

    public static Tripple findTripple(int a, int b, int c) {
        for (Tripple t : tripples ) {
            if (t.getYear() == a && t.getMonth() == b && t.getDay() == c) {
                return t;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        Path path = Paths.get(ClassLoader.getSystemResource("sample/temperature.csv").getPath());

        HashMap<Tripple, String> hashMap = new HashMap<>();
        try {
            BufferedReader br0 = new BufferedReader(new FileReader("/home/hoang/workspace/smartHome/src/main/resources/sample/extraterrestrial_irradiance.csv"));
            br0.readLine();
            String text1;
            String[] ele;

            while ((text1 = br0.readLine()) != null) {
//                System.out.println(text1);
                ele = text1.split(",");
                tripples.add(new Tripple(Integer.valueOf(ele[0]),Integer.valueOf(ele[1]),Integer.valueOf(ele[2])));
                hashMap.put(tripples.get(tripples.size() - 1 ), ele[3]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Tripple find = findTripple(2017,9,24);
        System.out.println(hashMap.get(find));

        BufferedReader br = null;
        BufferedWriter bw = null;

        try {
            br = new BufferedReader(new FileReader("/home/hoang/workspace/smartHome/src/main/resources/sample/temperature.csv"));
            bw = new BufferedWriter(new FileWriter("/home/hoang/workspace/smartHome/src/main/resources/sample/temperature1.csv", true));

            String text;
            String temp;
            String[] element;
            String result;

            int i = 0;
            br.readLine();
            while ((text = br.readLine()) != null) {
                if ((i % 10) == 0 ) {

                        temp = new String(text);
                        element = temp.split(",");

                        String[] time = element[0].split(" ");
                        String[] date = time[0].split("-");

                        result = date[0] + "," + date[1] + "," + date[2] + "," +
                                hashMap.get(findTripple(Integer.valueOf(date[0]),
                                        Integer.valueOf(date[1]),
                                        Integer.valueOf(date[2]))) + "," +
                                time[1] + "," + element[1];
//                        System.out.println(result);
                        bw.write(result + "\n");

                }
                i++;
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
