package com.hrms.hrms_backend.repository;

import com.hrms.hrms_backend.entity.TravelPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.RestController;

@RestController
public interface TravelPlanRepository extends JpaRepository<TravelPlan,Long> {
}
