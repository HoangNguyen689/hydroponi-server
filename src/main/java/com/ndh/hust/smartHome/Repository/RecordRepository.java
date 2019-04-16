package com.ndh.hust.smartHome.Repository;

import com.mongodb.lang.Nullable;
import com.ndh.hust.smartHome.model.Record;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface RecordRepository extends MongoRepository<Record, String> {
    Record findTopByOrderByTimeStampDesc();

    @Nullable
    Record findTopByTimeStampBetween(String time1, String time2);

    @Nullable
    List<Record> findByTimeStampBetween(String time1, String time2);
}
