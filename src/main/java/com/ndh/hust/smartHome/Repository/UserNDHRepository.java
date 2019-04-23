package com.ndh.hust.smartHome.Repository;

import com.ndh.hust.smartHome.model.domain.UserNDH;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserNDHRepository extends MongoRepository<UserNDH, String> {
    UserNDH findByUsername(String username);
}
