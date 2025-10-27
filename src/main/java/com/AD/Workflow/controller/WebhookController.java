package com.AD.Workflow.controller;

import com.AD.Workflow.domain.model.Task;
import com.AD.Workflow.service.EmailNotificationService;
import com.AD.Workflow.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController("/api/webhooks")
public class WebhookController {

    private EmailNotificationService emailNotificationService;

    private TaskService taskService;

    public WebhookController(
            EmailNotificationService emailNotificationService,
            TaskService taskService
    ) {
        this.emailNotificationService = emailNotificationService;
        this.taskService = taskService;
    }

    @PostMapping("/trigger/workflow/{workflowId}")
    public String triggerWorkflow(@PathVariable int workflowId) {
        // Logic to trigger the workflow with the given ID
        return "Workflow with ID " + workflowId + " has been triggered.";
    }

    @PostMapping("/trigger/task/{taskId}")
    public String triggerTask(@PathVariable int taskId) {
        // Logic to trigger the task with the given ID
        Optional<Task> task = taskService.findTaskById(taskId);
        return task.map(t -> {
            taskService.executeTask(t);
            String msg = "Task with ID " + taskId + " has been triggered.";
            emailNotificationService.sendEmail("ayushdaruka@outlook.com", "Task Triggered", msg);
            return msg;
        }).orElse("Task with ID " + taskId + " not found.");
    }

    @PostMapping("/notify/status/{entityId}")
    public String notifyStatusChange(@PathVariable int entityId) {
        // Logic to notify about the status change of the entity with the given ID
        return "Status change notification for entity with ID " + entityId + " has been sent.";
    }

}