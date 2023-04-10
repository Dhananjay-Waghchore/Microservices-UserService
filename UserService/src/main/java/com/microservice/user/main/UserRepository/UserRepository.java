package com.microservice.user.main.UserRepository;

import com.microservice.user.main.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {

}
