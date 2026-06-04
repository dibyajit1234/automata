package com.dibyajit.automata_backend.modules.integrations.repository;

import com.dibyajit.automata_backend.modules.integrations.entity.AppConnection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AppConnectionRepository extends JpaRepository<AppConnection, UUID> {
}
