package com.ndh.hust.smartHome.Repository;

import com.ndh.hust.smartHome.model.Temperature;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TemperatureRepository extends MongoRepository<Temperature, String> {
    List<Temperature> findByDayInYear(String dayInYear);
}
