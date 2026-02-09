package com.hrms.hrms_backend.repository;

import com.hrms.hrms_backend.entity.TravelAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TravelAssignmentRepository extends JpaRepository<TravelAssignment,Long> {
}
