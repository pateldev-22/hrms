package com.hrms.hrms_backend.repository;

import com.hrms.hrms_backend.constants.GameType;
import com.hrms.hrms_backend.entity.GameConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GameConfigurationRepository extends JpaRepository<GameConfiguration, Long> {

    Optional<GameConfiguration> findByGameType(GameType gameType);

    List<GameConfiguration> findByIsActiveTrue();
}

