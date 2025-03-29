package com.example.api_base.repository;

import com.example.api_base.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProcessRepository extends JpaRepository<User, UUID> {
}
