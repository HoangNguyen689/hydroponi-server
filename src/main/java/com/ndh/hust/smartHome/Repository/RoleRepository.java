package com.ndh.hust.smartHome.Repository;

import com.ndh.hust.smartHome.model.domain.Role;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RoleRepository extends MongoRepository<Role, String> {
    Role findByRole(String role);
}
