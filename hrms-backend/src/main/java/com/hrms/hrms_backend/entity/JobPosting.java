package com.hrms.hrms_backend.entity;
import com.hrms.hrms_backend.constants.JobStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "job_postings")
public class JobPosting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long jobId;

    @Column(name = "job_title")
    private String jobTitle;

    @Column(name = "department")
    private String department;

    @Column(name = "location")
    private String location;

    @Column(name = "experience_required")
    private String experienceRequired;

    @Column(name = "job_summary")
    private String jobSummary;

    @Column(name = "job_description")
    private String jobDescription;

    @Column(name = "hr_owner_email")
    private String hrOwnerEmail;

    @Column(name = "cv_reviewer_emails")
    private String cvReviewerEmails;

    @Column(name = "status")
    private JobStatus status;

    @Column(name = "jd_file_path")
    private String jdFilePath;

    @Column(name = "closing_date")
    private LocalDate closingDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User createdBy;

    @OneToMany(mappedBy = "jobPosting",cascade = CascadeType.ALL)
    private List<JobShare> jobShares = new ArrayList<>();

    @OneToMany(mappedBy = "jobPosting",cascade = CascadeType.ALL)
    private List<Referral> referrals = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;


    public boolean isActive() {
        return status == JobStatus.ACTIVE && !closingDate.isBefore(LocalDate.now());
    }


}
