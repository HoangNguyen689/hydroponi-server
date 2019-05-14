package com.ndh.hust.smartHome.Repository;

import com.ndh.hust.smartHome.model.Device;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DeviceRepository extends MongoRepository<Device, String> {
    Device findByUsername(String username);
}
