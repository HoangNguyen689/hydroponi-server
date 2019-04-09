package com.ndh.hust.smartHome.Repository;

import com.ndh.hust.smartHome.model.Temperature;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TemperatureRepository extends MongoRepository<Temperature, String> {
}
