package com.hrms.hrms_backend.constants;

public enum AssignmentStatus {
    ASSIGNED,       // Initial state when HR assigns
    ACKNOWLEDGED,   // Employee has seen the assignment
    COMPLETED,      // Travel is completed
    CANCELLED       // Assignment cancelled
}   