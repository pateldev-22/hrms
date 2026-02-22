package com.hrms.hrms_backend.repository;


import com.hrms.hrms_backend.entity.GameBooking;
import com.hrms.hrms_backend.entity.GameSlot;
import com.hrms.hrms_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface GameBookingRepository extends JpaRepository<GameBooking, Long> {

    List<GameBooking> findBySlot(GameSlot slot);

    List<GameBooking> findByUser(User user);

    @Query("SELECT gb FROM GameBooking gb WHERE gb.user = :user AND gb.slot.slotDate = :date AND gb.bookingStatus != 'CANCELLED'")
    List<GameBooking> findActiveBookingsByUserAndDate(@Param("user") User user, @Param("date") LocalDate date);
}
