package com.hrms.hrms_backend.repository;

import com.hrms.hrms_backend.constants.JobStatus;
import com.hrms.hrms_backend.entity.JobPosting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface JobPostingRepository extends JpaRepository<JobPosting,Long> {
    List<JobPosting> findByStatusOrderByCreatedAtDesc(JobStatus status);

    @Query(value = "SELECT * from job_postings jp where " +
            "jp.status = 0 and jp.closing_date >= :today ", nativeQuery = true)
    List<JobPosting> findActiveJobs(LocalDate today);

}
