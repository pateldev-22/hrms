package com.hrms.hrms_backend.controller;

import com.hrms.hrms_backend.dto.job.*;
import com.hrms.hrms_backend.entity.User;
import com.hrms.hrms_backend.service.JobPostingService;
import com.hrms.hrms_backend.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
public class JobPostingController {
    private final JobPostingService jobPostingService;
    private final UserService userService;

    public JobPostingController(JobPostingService jobPostingService, UserService userService) {
        this.jobPostingService = jobPostingService;
        this.userService = userService;
    }

    @GetMapping
    public List<JobPostingResponse> getAllActiveJobs(){
        return jobPostingService.getActiveJobs();
    }

    @PostMapping
    public JobPostingResponse createJobPosting(@RequestBody JobPostingRequest dto){
        User user = getCurrentUser();
        return jobPostingService.createJobPosting(user,dto);
    }

    @PostMapping("/{jobId}/share")
    public String shareJob(@PathVariable Long jobId,
                           @RequestBody JobShareRequest dto){
        User user = getCurrentUser();
        try{
        jobPostingService.shareJob(jobId,dto,user);
        }catch (Exception e){
            return "Error occured while sharing this job";
        }
        return String.format("Job Shared Successfully to %s ! ", dto.getRecipientEmail());
    }

    @PostMapping("/{jobId}/refer")
    public ResponseEntity<ReferralResponse> referJob(@PathVariable Long jobId,
                                                     @RequestPart("dto") ReferralRequest dto,
                                                     @RequestPart("file") MultipartFile file){
        User user = getCurrentUser();
        ReferralResponse response = jobPostingService.createReferral(jobId,dto,file,user.getUserId());
        return ResponseEntity.ok().body(response);
    }

    public User getCurrentUser(){
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        return userService.findUserByEmail(userEmail);
    }
}
