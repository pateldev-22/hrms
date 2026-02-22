package com.hrms.hrms_backend.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "game_booking_participants")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GameBookingParticipant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "participant_id")
    private Long participantId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id")
    private GameBooking booking;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "notification_sent")
    private Boolean notificationSent;

    @Column(name = "calendar_invite_sent")
    private Boolean calendarInviteSent;
}
