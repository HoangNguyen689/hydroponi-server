package com.ndh.hust.smartHome.Repository;

import com.ndh.hust.smartHome.model.Precipitation;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PrecipitationRepository extends MongoRepository<Precipitation, String> {
    Precipitation findByYear(String year);
}
