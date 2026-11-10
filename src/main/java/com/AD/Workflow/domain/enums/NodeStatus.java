package com.AD.Workflow.domain.enums;

public enum NodeStatus {
    CREATED,
    ACTIVE,
    PROCESSING,
    SUSPENDED,
    PENDING, // waiting to execute
    RETRYING,
    SKIPPED,
    COMPLETED,
    FAILED,
    CANCELLED,
    TERMINATED,
    PAUSED
}