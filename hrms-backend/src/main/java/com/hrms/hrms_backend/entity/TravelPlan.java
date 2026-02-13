package com.hrms.hrms_backend.entity;

import com.hrms.hrms_backend.constants.AssignmentStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
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

    @Column(name = "is_deleted")
    private boolean isDeleted = false;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

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

    @OneToMany(mappedBy = "travelPlan", cascade = CascadeType.ALL)
    private List<Expense> expenses = new ArrayList<>();

    public void addAssignment(TravelAssignment assignment){
        travelAssignments.add(assignment);
        assignment.setTravelPlan(this);
    }

    public List<Long> getAssignedEmployeeIds() {
        return travelAssignments.stream()
                .filter(assignment -> assignment.getAssignmentStatus() != AssignmentStatus.CANCELLED)
                .map(assignment -> assignment.getUser().getUserId())
                .toList();
    }

}
