package com.hrms.hrms_backend.repository;

import com.hrms.hrms_backend.entity.GameBookingParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameBookingParticipantRepository extends JpaRepository<GameBookingParticipant, Long> {
}
