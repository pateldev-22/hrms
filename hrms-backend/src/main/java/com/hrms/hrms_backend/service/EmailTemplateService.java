package com.hrms.hrms_backend.service;
import com.hrms.hrms_backend.constants.EmailType;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class EmailTemplateService {

    public String getEmailTemplate(EmailType type, Map<String, Object> data) {
        return switch (type) {
            case JOB_REFERRAL_HR -> buildReferralEmail(data);
            case JOB_SHARED -> buildSharedJobEmail(data);
            case TRAVEL_ASSIGNMENT -> buildTravelAssignmentEmail(data);
            case EXPENSE_SUBMITTED -> buildExpenseSubmittedEmail(data);
            case EXPENSE_REVIEWED -> buildExpenseReviewedEmail(data);
        };
    }

    private String buildReferralEmail(Map<String, Object> data) {
        return String.format("JobTitle :%s%n," +
                        "Job Summary :%s%n," +
                        "refferedTo :%s%n," +
                        "referred by :%s%n," +
                        "Friend Name :%s%n," +
                        "Friend Eamil :%s%n",
                data.get("jobTitle"), data.get("jobSummary"),
                data.get("referrerName"), data.get("referrerEmail"),
                data.get("friendName"), data.get("friendEmail")
        );
    }

    private String buildSharedJobEmail(Map<String, Object> data) {
        return String.format("Job Title :%s%n," +
                        "Department :%s%n," +
                        "Location :%s%n," +
                        "Experience :%s%n," +
                        "Job Summary :%s%n," +
                        "Closing Date :%s%n," +
                        "Jd File Path :%s%n",
                data.get("jobTitle"), data.get("department"),
                data.get("location"), data.get("experience"),
                data.get("jobSummary"),
                data.get("closingDate"),
                data.get("jdFilePath") != null
                        ? "JD Link: " + data.get("jdFilePath")
                        : ""
        );
    }

    private String buildTravelAssignmentEmail(Map<String, Object> data) {
        return String.format("Employee Name :%s%n," +
                        "Travel Name :%s%n," +
                        "Destination :%s%n," +
                        "StartDate :%s%n," +
                        "End Date :%s%n," +
                        "Purpose :%s%n",
                data.get("employeeName"),
                data.get("travelName"), data.get("destination"),
                data.get("startDate"), data.get("endDate"),
                data.get("purpose")
        );
    }

    private String buildExpenseSubmittedEmail(Map<String, Object> data) {
        return String.format("Employee Name :%s%n," +
                        "Travel Name :%s%n," +
                        "Category :%s%n," +
                        "Amount :%s%n," +
                        "Expense Date :%s%n," +
                        "Description :%s%n",
                data.get("employeeName"), data.get("travelName"),
                data.get("category"), data.get("amount"),
                data.get("expenseDate"), data.get("description")
        );
    }

    private String buildExpenseReviewedEmail(Map<String, Object> data) {
        return String.format("EmployeeName :%s%n," +
                        "ReviewedBy :%s%n," +
                        "Status of Expense :%s%n," +
                        "Category :%s%n," +
                        "HR Remark :%s%n",
                data.get("employeeName"),data.get("reviewedBy"),
                data.get("status"),
                data.get("category"), data.get("hrRemark")
        );
//        "employeeName",expense.getUser().getFirstName(),
//                "reviewedBy",expense.getReviewedBy().getFirstName(),
//                "status",expense.getStatus(),
//                "category",expense.getExpenseCategory(),
//                "hrRemark",expense.getHrRemarks()
    }
}
