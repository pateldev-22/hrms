package com.hrms.hrms_backend.entity;

import com.hrms.hrms_backend.constants.AssignmentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "travel_assignments")
@AllArgsConstructor
@NoArgsConstructor
public class TravelAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "assignment_id")
    private Long assignmentId;

    @ManyToOne
    @JoinColumn(name="travel_id",nullable = false)
    private TravelPlan travelPlan;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


    @Enumerated(EnumType.STRING)
    @Column(name = "assignment_status")
    private AssignmentStatus assignmentStatus = AssignmentStatus.ASSIGNED;

}
