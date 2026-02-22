package com.hrms.hrms_backend.repository;

import com.hrms.hrms_backend.constants.GameType;
import com.hrms.hrms_backend.entity.GameSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface GameSlotRepository extends JpaRepository<GameSlot, Long> {

    List<GameSlot> findByGameTypeAndSlotDate(GameType gameType, LocalDate slotDate);

    @Query("SELECT gs FROM GameSlot gs WHERE gs.gameType = :gameType AND gs.slotDate >= :date ORDER BY gs.slotDate, gs.startTime")
    List<GameSlot> findUpcomingSlots(@Param("gameType") GameType gameType, @Param("date") LocalDate date);

    boolean existsByGameTypeAndSlotDateAndStartTime(GameType gameType, LocalDate slotDate, java.time.LocalTime startTime);
}
