package com.ndh.hust.smartHome.database;

import com.mongodb.MongoClient;
import com.ndh.hust.smartHome.Repository.RecordRepository;
import com.ndh.hust.smartHome.model.Device;
import com.ndh.hust.smartHome.model.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;


public class testMongo {
    static Record temperature = new Record();

    static MongoTemplate mongoTemplate = new MongoTemplate(new MongoClient(), "smartHome");

    @Autowired
    public static RecordRepository repository;


    public static void main(String[] args) {
//        mongoTemplate.insert(temperature, "record");
//        repository.save(temperature);

//        Record r = null;
//        try {
//            r = repository.findTopByOrderByIdDesc();
//        } catch (NullPointerException e) {
//            e.printStackTrace();
//        }
//        extraterrestrialIrradianceRepository.save(new ExtraterrestrialIrradiance());
//        repository.save(new Record());
        Device dev = new Device();
        dev.setUsername("dev1");
        dev.setPassword("dev1");
        mongoTemplate.insert(dev,"device");
    }
}
