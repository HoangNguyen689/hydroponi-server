package com.ndh.hust.smartHome.Repository;

import com.ndh.hust.smartHome.model.Command;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CommandRepository extends MongoRepository<Command, String> {

}
