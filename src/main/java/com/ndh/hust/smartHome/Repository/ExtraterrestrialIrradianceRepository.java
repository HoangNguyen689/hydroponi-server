package com.ndh.hust.smartHome.Repository;

import com.mongodb.lang.Nullable;
import com.ndh.hust.smartHome.model.ExtraterrestrialIrradiance;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface ExtraterrestrialIrradianceRepository extends MongoRepository<ExtraterrestrialIrradiance, String> {
    @Nullable
    ExtraterrestrialIrradiance findByDayOfYear(int dayOfYear);

    @Nullable
    List<ExtraterrestrialIrradiance> findByTimeStampBetween(String time1, String time2);

    @Query("{timeStamp: {$regex: '[0-9]{4}-?0-'}}")
    List<ExtraterrestrialIrradiance> findByMonth(int month);
}
