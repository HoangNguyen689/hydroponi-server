package com.ndh.hust.smartHome.Repository;

import com.ndh.hust.smartHome.model.Crop;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CropRepository extends MongoRepository<Crop, String> {
    Crop findByName(String name);
}
