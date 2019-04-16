package com.ndh.hust.smartHome.docfile;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import static com.mongodb.client.model.Filters.eq;

public class docfile3 {
    public static void main(String[] args) {
        MongoClient client = new MongoClient("localhost", 27017);
        MongoDatabase database = client.getDatabase("smartHome");
        MongoCollection<Document> temp = database.getCollection("temperature");
        MongoCollection<Document> radian = database.getCollection("extraterrestrial_irradiance");

        BufferedWriter bw = null;
        String result;
        double maxTemp;
        double minTemp;
        for (Document cur : radian.find()) {
            String timeStamp = cur.getString("timeStamp");
            int dayOfYear = cur.getInteger("dayOfYear");
            double irradiance = cur.getDouble("irradiance");

            maxTemp = 0;
            minTemp = 80;
            for(Document c : temp.find(eq("dayOfYear", dayOfYear))) {
                double t = c.getDouble("temperature");
                if (maxTemp < t)
                    maxTemp = t;

                if (minTemp > t)
                    minTemp = t;
            }

            double ET = 0.0023 * irradiance * 0.0864 / 2.45 * ((maxTemp + minTemp)/2 + 17.8) * Math.sqrt(maxTemp - minTemp);

            result = timeStamp + "," + dayOfYear + "," + irradiance + "," + maxTemp + "," + minTemp + "," + ET;
            System.out.println(result);
            try {
                bw = new BufferedWriter(new FileWriter("/home/hoang/full.csv", true) );
                bw.write(result + "\n");
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
}
