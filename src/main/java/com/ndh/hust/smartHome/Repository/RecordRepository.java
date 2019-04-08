package com.ndh.hust.smartHome.Repository;

import com.ndh.hust.smartHome.model.Record;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface RecordRepository extends MongoRepository<Record, String> {
    List<Record> findTop2ByOrderByTimeStampDesc();
    Record findTopByOrderByTimeStampDesc();
}
