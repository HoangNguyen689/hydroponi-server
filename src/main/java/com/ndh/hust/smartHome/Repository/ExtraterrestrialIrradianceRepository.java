package com.ndh.hust.smartHome.Repository;

import com.ndh.hust.smartHome.model.ExtraterrestrialIrradiance;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ExtraterrestrialIrradianceRepository extends MongoRepository<ExtraterrestrialIrradiance, String> {
    ExtraterrestrialIrradiance findByDayInYear(String dayInYear);
}
