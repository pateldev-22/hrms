package com.hrms.hrms_backend.service;


import com.hrms.hrms_backend.constants.JobStatus;
import com.hrms.hrms_backend.dto.job.*;
import com.hrms.hrms_backend.entity.*;
import com.hrms.hrms_backend.exception.CustomException;
import com.hrms.hrms_backend.repository.JobPostingRepository;
import com.hrms.hrms_backend.repository.JobShareRepository;
import com.hrms.hrms_backend.repository.ReferralRepository;
import com.hrms.hrms_backend.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class JobPostingService {

    private final JobPostingRepository jobRepository;
    private final JobShareRepository shareRepository;
    private final ReferralRepository referralRepository;
    private final UserRepository userRepository;
    private final DocumentService documentService;

    @Autowired
    public JobPostingService(
            JobPostingRepository jobRepository,
            JobShareRepository shareRepository,
            ReferralRepository referralRepository,
            UserRepository userRepository,
            DocumentService documentService) {
        this.jobRepository = jobRepository;
        this.shareRepository = shareRepository;
        this.referralRepository = referralRepository;
        this.userRepository = userRepository;
        this.documentService = documentService;
    }

    public JobPostingResponse createJobPosting(User user, JobPostingRequest dto){
        JobPosting jobPosting = new JobPosting();

        jobPosting.setJobTitle(dto.getJobTitle());
        jobPosting.setDepartment(dto.getDepartment());
        jobPosting.setLocation(dto.getLocation());
        jobPosting.setExperienceRequired(dto.getExperienceRequired());
        jobPosting.setJobSummary(dto.getJobSummary());
        jobPosting.setJobDescription(dto.getJobDescription());
        jobPosting.setHrOwnerEmail(dto.getHrOwnerEmail());
        jobPosting.setCvReviewerEmails(dto.getCvReviewerEmails());
        jobPosting.setStatus(JobStatus.ACTIVE);
        jobPosting.setClosingDate(dto.getClosingDate());
        jobPosting.setCreatedBy(user);

        JobPosting jobPosted =  jobRepository.save((jobPosting));

        return toJobResponse(jobPosted);
    }

    public List<JobPostingResponse> getActiveJobs() {
        List<JobPosting> jobs = jobRepository.findActiveJobs(LocalDate.now());
        return jobs.stream()
                .map(this::toJobResponse)
                .toList();
    }

    public JobPostingResponse getJobById(Long jobId) {
        JobPosting job = jobRepository.findById(jobId)
                .orElseThrow(() -> new CustomException("Job not found"));
        return toJobResponse(job);
    }

    @Transactional
    public void shareJob(Long jobId, JobShareRequest request,User user) {
        JobPosting job = jobRepository.findById(jobId)
                .orElseThrow(() -> new CustomException("Job not found"));

        JobShare share = JobShare.builder()
                .jobPosting(job)
                .sharedBy(user)
                .recipientEmail(request.getRecipientEmail())
                .build();

        shareRepository.save(share);
    }

    @Transactional
    public ReferralResponse createReferral(
            Long jobId,
            ReferralRequest request,
            MultipartFile cvFile,
            Long userId) {

        JobPosting job = jobRepository.findById(jobId)
                .orElseThrow(() -> new CustomException("Job not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException("User not found"));

        Document cvDocument = documentService.uploadDocument(cvFile, "CV", user);

        Referral referral = Referral.builder()
                .jobPosting(job)
                .referredBy(user)
                .friendName(request.getFriendName())
                .friendEmail(request.getFriendEmail())
                .cvFilePath(cvDocument.getFilePath())
                .referralNote(request.getReferralNote())
                .build();

        Referral savedReferral = referralRepository.save(referral);


        return toReferralResponse(savedReferral);
    }



    private JobPostingResponse toJobResponse(JobPosting job) {
        JobPostingResponse dto = new JobPostingResponse();
        dto.setJobId(job.getJobId());
        dto.setJobTitle(job.getJobTitle());
        dto.setDepartment(job.getDepartment());
        dto.setLocation(job.getLocation());
        dto.setExperienceRequired(job.getExperienceRequired());
        dto.setJobSummary(job.getJobSummary());
        dto.setJobDescription(job.getJobDescription());
        dto.setClosingDate(job.getClosingDate());
        dto.setTotalReferrals(referralRepository.countByJobPosting(job).intValue());
        dto.setTotalShares(shareRepository.countByJobPosting(job).intValue());
        dto.setCreatedAt(job.getCreatedAt());
        return dto;
    }

    private ReferralResponse toReferralResponse(Referral referral) {
        ReferralResponse dto = new ReferralResponse();
        dto.setReferralId(referral.getReferralId());
        dto.setJobId(referral.getJobPosting().getJobId());
        dto.setJobTitle(referral.getJobPosting().getJobTitle());
        dto.setFriendName(referral.getFriendName());
        dto.setFriendEmail(referral.getFriendEmail());
        dto.setCvFilePath(referral.getCvFilePath());
        dto.setReferralNote(referral.getReferralNote());
        dto.setReferredByName(referral.getReferredBy().getFirstName());
        dto.setCreatedAt(referral.getCreatedAt());
        return dto;
    }

}
