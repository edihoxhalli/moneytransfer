package com.edi.moneytransfer.persistence.repository;

import com.edi.moneytransfer.persistence.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
