package com.hrms.hrms_backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "travel_plans")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TravelPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "travel_id")
    private Long travelId;

    @Column(name = "travel_name", nullable = false)
    private String travelName;

    @Column(name = "destination", nullable = false)
    private String destination;

    @Column(name = "purpose")
    private String purpose;

    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate;

    @ManyToOne
    @JoinColumn(name = "created_by_hr", nullable = false)
    private User createdByHr;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "travelPlan" , cascade = CascadeType.ALL,orphanRemoval = true) //because if the travel assignment is removed from the travelplan then it should be deleted and not padi rehvu joiee emnem in db thats why orphan removal = true
    private List<TravelAssignment> travelAssignments = new ArrayList<>();

    @OneToMany(mappedBy = "travelPlan", cascade = CascadeType.ALL)
    private List<TravelDocument> travelDocuments = new ArrayList<>();

}
