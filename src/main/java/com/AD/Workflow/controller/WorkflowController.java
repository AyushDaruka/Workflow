package com.AD.Workflow.controller;

import com.AD.Workflow.domain.enums.WorkflowStatus;
import com.AD.Workflow.domain.model.Workflow;
import com.AD.Workflow.repository.WorkflowRepository;
import com.AD.Workflow.service.EmailService;
import com.AD.Workflow.service.WorkflowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.MessageFormat;

@RestController("/")
public class WorkflowController {
    @Autowired
    WorkflowRepository workflowRepository;

    @Autowired
    EmailService emailService;

    @Autowired
    WorkflowService workflowService;

    @GetMapping("/ping")
    public ResponseEntity<String> hello() {
//        emailService.sendEmail("ayushdaruka@outlook.com", "Test Message", "Test Message");
        return ResponseEntity.ok("Hello World!");
    }

    @PostMapping("/workflows")
    public ResponseEntity<String> createWorkflow(@RequestBody String name) {
        Workflow workflow = new Workflow();
        workflow.setName(name);
        workflow.setStatus(WorkflowStatus.CREATED);
        workflowRepository.save(workflow);
        return ResponseEntity.ok(
            MessageFormat.format(
                "Workflow {0} created successfully",
                name
            )
        );
    }

    @PutMapping("/workflows/{id}")
    public ResponseEntity<Workflow> updateWorkflow(@PathVariable int id, @RequestBody Workflow updatedWorkflow) {
        try {
            Workflow savedWorkflow = workflowService.updateWorkflow(id, updatedWorkflow);
            return ResponseEntity.ok(savedWorkflow);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
        // Workflow not found handled centrally

//        return MessageFormat.format(
//                "Workflow {0} added successfully.",
//                workflow.get().getName()
//        );
    }

    @GetMapping("/workflows")
    public ResponseEntity<Page<Workflow>> getAllWorkflows(
        @PageableDefault(size = 20, sort = "name", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<Workflow> workflows = workflowRepository.findAll(pageable);
        return ResponseEntity.ok(workflows);
    }

    @GetMapping("/workflows/{id}")
    public ResponseEntity<Workflow> getWorkflowById(@PathVariable int id) {
        Workflow workflow = workflowService.getWorkflowById(id);
        return ResponseEntity.ok(workflow);
    }

    @DeleteMapping("/workflows/{id}")
    public ResponseEntity<String> deleteWorkflowById(@PathVariable int id) {
        String workflowName = workflowService.deleteWorkflowById(id);
        return ResponseEntity.ok(
            MessageFormat.format(
                "Workflow {0} deleted successfully.",
                    workflowName
            )
        );
    }

    @PostMapping("workflows/{id}/activate")
    public ResponseEntity<String> activateWorkflow(@PathVariable int id) {
        return ResponseEntity.ok(workflowService.activateWorkflow(id));
    }

    @PostMapping("workflows/{id}/deactivate")
    public ResponseEntity<String> deactivateWorkflow(@PathVariable int id) {
        return ResponseEntity.ok(workflowService.deactivateWorkflow(id));
    }

}
