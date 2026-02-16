package com.hrms.hrms_backend.repository;

import com.hrms.hrms_backend.entity.JobPosting;
import com.hrms.hrms_backend.entity.Referral;
import com.hrms.hrms_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReferralRepository extends JpaRepository<Referral,Long> {
    List<Referral> findByReferredByOrderByCreatedAtDesc(User user);

    Long countByJobPosting(JobPosting jobPosting);

}
