package com.hrms.hrms_backend.repository;

import com.hrms.hrms_backend.entity.JobPosting;
import com.hrms.hrms_backend.entity.JobShare;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobShareRepository extends JpaRepository<JobShare,Long> {
    Long countByJobPosting(JobPosting jobPosting);
}
