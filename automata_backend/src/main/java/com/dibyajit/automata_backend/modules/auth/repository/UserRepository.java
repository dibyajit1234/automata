package com.dibyajit.automata_backend.modules.auth.repository;

import com.dibyajit.automata_backend.modules.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
}
