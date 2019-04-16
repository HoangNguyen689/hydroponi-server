package com.ndh.hust.smartHome.docfile;

import java.io.*;

public class docfile2 {
    public static void main(String[] args) {
        BufferedReader br = null;
        BufferedWriter bw = null;

        try {
            br = new BufferedReader(new FileReader("/home/hoang/workspace/smartHome/src/main/resources/sample/extraterrestrial_irradiance.csv"));
            bw = new BufferedWriter(new FileWriter("/home/hoang/workspace/smartHome/src/main/resources/sample/extraterrestrial_irradiance1.csv", true));

            String text;
            String[] element;
            int i = 0;
            br.readLine();

            while ((text = br.readLine()) != null) {
                element = text.split(",");
                String result = element[0] + "-" + element[1] + "-" +element[2] + "," + element[3] + "," + element[4];
                System.out.println(result);
                bw.write(result + "\n");
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
