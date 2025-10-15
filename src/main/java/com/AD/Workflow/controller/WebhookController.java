package com.AD.Workflow.controller;

import com.AD.Workflow.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api/webhooks")
public class WebhookController {

    @Autowired
    EmailService emailService;

    @PostMapping("/trigger/workflow/{workflowId}")
    public String triggerWorkflow(@PathVariable int workflowId) {
        // Logic to trigger the workflow with the given ID
        return "Workflow with ID " + workflowId + " has been triggered.";
    }

    @PostMapping("/trigger/task/{taskId}")
    public String triggerTask(@PathVariable int taskId) {
        // Logic to trigger the task with the given ID
        return "Task with ID " + taskId + " has been triggered.";
    }

    @PostMapping("/notify/status/{entityId}")
    public String notifyStatusChange(@PathVariable int entityId) {
        // Logic to notify about the status change of the entity with the given ID
        return "Status change notification for entity with ID " + entityId + " has been sent.";
    }

}
