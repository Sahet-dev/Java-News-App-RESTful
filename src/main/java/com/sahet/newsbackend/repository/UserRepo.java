package com.sahet.newsbackend.repository;

import com.sahet.newsbackend.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<Users, Integer> {

    Users findByUsername(String username);
    Users findByEmail(String email);



}
