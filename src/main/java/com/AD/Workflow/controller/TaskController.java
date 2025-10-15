package com.AD.Workflow.controller;

import com.AD.Workflow.domain.enums.TaskStatus;
import com.AD.Workflow.domain.model.Task;
import com.AD.Workflow.domain.model.Workflow;
import com.AD.Workflow.repository.TaskRepository;
import com.AD.Workflow.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController("/tasks")
public class TaskController {

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    TaskService taskService;

    @GetMapping("/{taskId}")
    public ResponseEntity<Task> getTaskDetails(@RequestParam int taskId) {
        Optional<Task> task = taskRepository.findById(taskId);
        return task.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/tasks-in-workflow/{workflowId}")
    public ResponseEntity<List<Task>> getTasksByWorkflowId(@RequestParam int workflowId) {
        List<Task> tasks = taskRepository.findByWorkflowId(workflowId);
        return Optional.of(tasks)
                .filter(t -> !t.isEmpty())
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/createTask")
    public ResponseEntity<String> createTask(@RequestParam Task task) {
        Task t = taskRepository.save(task);
        return ResponseEntity.ok("Task added successfully.");
    }

    @PostMapping("/{taskId}/cancel")
    public ResponseEntity<String> cancelTask(@RequestParam int taskId) {
        Optional<Task> taskOpt = taskRepository.findById(taskId);
        return taskOpt.map(task -> {
            task.setStatus(TaskStatus.CANCELLED);
            taskService.terminateTask(task);
            taskRepository.save(task);
            return ResponseEntity.ok("Task cancelled successfully.");
        })
        .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/{taskId}/retry")
    public ResponseEntity<String> retryTask(@RequestParam int taskId) {
        Optional<Task> taskOpt = taskRepository.findById(taskId);
        return taskOpt.map(task -> {
            if (!"Failed".equals(task.getStatus())) {
                return ResponseEntity.badRequest().body("Only failed tasks can be retried.");
            }
            task.setStatus(TaskStatus.PENDING);
            taskService.retryTask(task);
            taskRepository.save(task);
            return ResponseEntity.ok("Task retry initiated successfully.");
        })
        .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/{taskId}/pause")
    public ResponseEntity<String> pauseTask(@RequestParam int taskId) {
        Optional<Task> taskOpt = taskRepository.findById(taskId);
        return taskOpt.map(task -> {
            switch (task.getStatus()) {
                case FAILED, COMPLETED, CANCELLED:
                    return ResponseEntity.badRequest().body("Only pending tasks in execution tasks can be paused.");
            }
            task.setStatus(TaskStatus.PAUSED);
            taskService.pauseTask(task);
            taskRepository.save(task);
            return ResponseEntity.ok("Task retry initiated successfully.");
        })
        .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/{taskId}/resume")
    public ResponseEntity<String> resumeTask(@RequestParam int taskId) {
        Optional<Task> taskOpt = taskRepository.findById(taskId);
        return taskOpt.map(task -> {
            if (!"Paused".equals(task.getStatus())) {
                return ResponseEntity.badRequest().body("Only paused tasks can be resumed.");
            }
            task.setStatus(TaskStatus.PENDING);
            taskService.resumeTask(task);
            taskRepository.save(task);
            return ResponseEntity.ok("Task retry initiated successfully.");
        })
        .orElseGet(() -> ResponseEntity.notFound().build());
    }

}
