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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.hrms.hrms_backend.constants.EmailType;
import com.hrms.hrms_backend.event.EmailEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;

@Service
@Transactional
public class JobPostingService {

    private final JobPostingRepository jobRepository;
    private final JobShareRepository shareRepository;
    private final ReferralRepository referralRepository;
    private final UserRepository userRepository;
    private final DocumentService documentService;
    private final ApplicationEventPublisher eventPublisher;

    @Value("${app.email.default-hr}")
    private String defaultHrEmail;


    @Autowired
    public JobPostingService(
            JobPostingRepository jobRepository,
            JobShareRepository shareRepository,
            ReferralRepository referralRepository,
            UserRepository userRepository,
            DocumentService documentService, ApplicationEventPublisher eventPublisher) {
        this.jobRepository = jobRepository;
        this.shareRepository = shareRepository;
        this.referralRepository = referralRepository;
        this.userRepository = userRepository;
        this.documentService = documentService;
        this.eventPublisher = eventPublisher;
    }

    public JobPostingResponse createJobPosting(User user, JobPostingRequest dto, MultipartFile jdFile){
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

        Document document = documentService.uploadDocument(jdFile,"JD_DOCUMENT",user);
        jobPosting.setJdFilePath(document.getFilePath());

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

        eventPublisher.publishEvent(new EmailEvent(
                this,
                List.of(request.getRecipientEmail()),
                EmailType.JOB_SHARED,
                Map.of(
                        "jobTitle", job.getJobTitle(),
                        "department", job.getDepartment(),
                        "location", job.getLocation(),
                        "experience", job.getExperienceRequired(),
                        "jobSummary", job.getJobSummary(),
                        "closingDate", job.getClosingDate().toString(),
                        "jdFilePath", job.getJdFilePath() != null ? job.getJdFilePath() : ""
                ),
                null
        ));
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

        List<String> recipients = new ArrayList<>();
        recipients.add(defaultHrEmail);

        recipients.add(job.getHrOwnerEmail());

        for (String email : job.getCvReviewerEmails().split(",")) {
            recipients.add(email.trim());
        }

        List<String> uniqueRecipients = recipients.stream().distinct().toList();

        eventPublisher.publishEvent(new EmailEvent(
                this,
                uniqueRecipients,
                EmailType.JOB_REFERRAL_HR,
                Map.of(
                        "jobTitle", job.getJobTitle(),
                        "jobSummary", job.getJobSummary(),
                        "referrerName", user.getFirstName() + " " + user.getLastName(),
                        "referrerEmail", user.getEmail(),
                        "friendName", request.getFriendName(),
                        "friendEmail", request.getFriendEmail() != null ? request.getFriendEmail() : "Not provided",
                        "note", request.getReferralNote() != null ? request.getReferralNote() : ""
                ),
                cvDocument.getFilePath()
        ));


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
        dto.setStatus(job.getStatus().toString());
        dto.setJobDescription(job.getJobDescription());
        dto.setClosingDate(job.getClosingDate());
        dto.setTotalReferrals(referralRepository.countByJobPosting(job).intValue());
        dto.setTotalShares(shareRepository.countByJobPosting(job).intValue());
        dto.setCreatedAt(job.getCreatedAt());
        dto.setJdFilePath(job.getJdFilePath());
        dto.setHasJD(job.getJdFilePath() != null);
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
