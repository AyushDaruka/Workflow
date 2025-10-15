package com.AD.Workflow.service;


import com.AD.Workflow.domain.enums.WorkflowStatus;
import com.AD.Workflow.domain.model.Workflow;
import com.AD.Workflow.exception.WorkflowNotFoundException;
import com.AD.Workflow.repository.WorkflowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkflowService {

    @Autowired
    private final WorkflowRepository workflowRepository;

    @Autowired
    public WorkflowService(WorkflowRepository workflowRepository) {
        this.workflowRepository = workflowRepository;
    }

    public List<Workflow> getAllWorkflows() {
        return workflowRepository.findAll()
                .stream()
                .toList();
    }

    public Workflow getWorkflowById(int id) {
        return workflowRepository.findById(id)
                .orElseThrow(() -> new WorkflowNotFoundException("No such workflow exists."));
    }

    public Workflow addWorkflow(Workflow workflow) {
        return workflowRepository.save(workflow);
    }

    public Workflow updateWorkflow(int id, Workflow updatedWorkflow) {
        if(id != updatedWorkflow.getWorkflowId())
            throw new IllegalArgumentException("Workflow ID in the path and request body do not match.");

        Workflow existingWorkflow = workflowRepository.findById(id)
                .orElseThrow(() -> new WorkflowNotFoundException("No such workflow exists that can be updated."));

        existingWorkflow.setName(updatedWorkflow.getName());
        existingWorkflow.setStatus(updatedWorkflow.getStatus());
        return workflowRepository.save(existingWorkflow);
    }

    public String deleteWorkflowById(int id) {
        try {
            Workflow workflow = workflowRepository.findById(id)
                .orElseThrow(() -> new WorkflowNotFoundException("No such workflow exists that can be deleted."));
            workflowRepository.delete(workflow);
            return workflow.getName();
        } catch (Exception e) {
            throw new WorkflowNotFoundException("Workflow couldn't be deleted.");
        }
    }

    public String activateWorkflow(int id) {
        try {
            Workflow workflow = workflowRepository.findById(id)
                .orElseThrow(() -> new WorkflowNotFoundException("No such workflow exists that can be deactivated."));
            workflow.setStatus(WorkflowStatus.ACTIVE);
            workflowRepository.save(workflow);
            return workflow.getName();
        } catch (Exception e) {
            throw new WorkflowNotFoundException("Workflow couldn't be deactivated.");
        }
    }

    public String deactivateWorkflow(int id) {
        try {
            Workflow workflow = workflowRepository.findById(id)
                .orElseThrow(() -> new WorkflowNotFoundException("No such workflow exists that can be deactivated."));
            workflow.setStatus(WorkflowStatus.INACTIVE);
            workflowRepository.save(workflow);
            return workflow.getName();
        } catch (Exception e) {
            throw new WorkflowNotFoundException("Workflow couldn't be deactivated.");
        }
    }

}
