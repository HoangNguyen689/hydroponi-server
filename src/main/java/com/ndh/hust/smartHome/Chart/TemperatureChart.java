package com.ndh.hust.smartHome.Chart;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import org.bson.Document;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Getter
public class TemperatureChart {
    private Long xAxis;
    private Double yAxis;

    public TemperatureChart() {

        MongoClient client = new MongoClient("localhost", 27017);
        MongoDatabase database = client.getDatabase("smartHome");
        MongoCollection<Document> record = database.getCollection("record");
        Document document = record.find().sort(new Document("_id", -1)).first();


        String timeStamp = document.getString("timeStamp");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        try {
            date = sdf.parse(timeStamp);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        xAxis = date.getTime();
        yAxis = document.getDouble("temperature");
    }

    public TemperatureChart(Long xAxis, Double yAxis) {
        this.xAxis = xAxis;
        this.yAxis = yAxis;

    }
}
