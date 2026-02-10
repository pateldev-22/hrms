package com.hrms.hrms_backend.repository;

import com.hrms.hrms_backend.constants.AssignmentStatus;
import com.hrms.hrms_backend.entity.TravelAssignment;
import com.hrms.hrms_backend.entity.TravelPlan;
import com.hrms.hrms_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TravelAssignmentRepository extends JpaRepository<TravelAssignment,Long> {
    List<TravelAssignment> findByTravelPlan(TravelPlan travelPlan);

    List<TravelAssignment> findByUser(User user);

    Optional<TravelAssignment> findByTravelPlanAndUser(TravelPlan travelPlan, User user);

    boolean existsByTravelPlanAndUser(TravelPlan travelPlan, User user);

    List<TravelAssignment> findByAssignmentStatus(AssignmentStatus status);

}
