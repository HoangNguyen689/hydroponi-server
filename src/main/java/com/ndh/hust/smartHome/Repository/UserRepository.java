package com.ndh.hust.smartHome.Repository;

import com.ndh.hust.smartHome.model.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
    User findByUsername(String username);
}
