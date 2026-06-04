package com.dibyajit.automata_backend.modules.integrations.repository;

import com.dibyajit.automata_backend.modules.integrations.entity.App;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AppRepository extends JpaRepository<App, UUID> {
}
