package com.hrms.hrms_backend.entity;

import com.hrms.hrms_backend.constants.GameType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalTime;

@Entity
@Table(name = "game_configurations")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GameConfiguration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "config_id")
    private Long configId;

    @Column(name = "game_type", unique = true)
    private GameType gameType;

    @Column(name = "operating_hours_start")
    private LocalTime operatingHoursStart;

    @Column(name = "operating_hours_end")
    private LocalTime operatingHoursEnd;

    @Column(name = "slot_duration_minutes")
    private Integer slotDurationMinutes;

    @Column(name = "max_players_per_slot")
    private Integer maxPlayersPerSlot;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "updated_at")
    @CreationTimestamp
    private java.time.LocalDateTime updatedAt;
}
