package com.ndh.hust.smartHome.Repository;

import com.ndh.hust.smartHome.model.Harvest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HarvestRepository extends MongoRepository<Harvest, String> {
}
