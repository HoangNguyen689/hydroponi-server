package com.ndh.hust.smartHome.Repository;

import com.ndh.hust.smartHome.model.Method;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MethodRepository extends MongoRepository<Method, String> {
}
