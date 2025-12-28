package com.AD.Workflow.service;


import com.AD.Workflow.domain.enums.WorkflowStatus;
import com.AD.Workflow.domain.model.Workflow;
import com.AD.Workflow.exception.WorkflowNotFoundException;
import com.AD.Workflow.repository.WorkflowRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class WorkflowService {


    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private WorkflowRepository workflowRepository;

    @Autowired
    public WorkflowService(WorkflowRepository workflowRepository) {
        this.workflowRepository = workflowRepository;
    }

    public List<Workflow> getAllWorkflows() {
        return workflowRepository.findAll()
                .stream()
                .toList();
    }


    public List<Workflow> findPage(String sortBy, int size, int page) {
        String sql = "SELECT w FROM Workflow w ORDER BY w." + sortBy;
        return entityManager.createQuery(sql, Workflow.class)
                .setFirstResult(page * size)   // OFFSET
                .setMaxResults(size)           // LIMIT
                .getResultList();
    }

    public Workflow getWorkflowById(int id) {
        return workflowRepository.findById(id)
                .orElseThrow(() -> new WorkflowNotFoundException("No such workflow exists."));
    }

    @Transactional
    public Optional<Workflow> addWorkflow(Workflow workflow) {
            return Optional.of(workflowRepository.save(workflow));
    }

    @Transactional
    public Workflow updateWorkflow(int id, @NonNull Workflow updatedWorkflow) {
        if(id != updatedWorkflow.getWorkflowId())
            throw new IllegalArgumentException("Workflow ID in the path and request body do not match.");

        Workflow existingWorkflow = workflowRepository.findById(id)
                .orElseThrow(() -> new WorkflowNotFoundException("No such workflow exists that can be updated."));

        existingWorkflow.setName(updatedWorkflow.getName());
        existingWorkflow.setStatus(updatedWorkflow.getStatus());
        existingWorkflow.setWorkflowNodes(updatedWorkflow.getWorkflowNodes());
        existingWorkflow.setConnections(updatedWorkflow.getConnections());
//        return workflowRepository.save(existingWorkflow);
//        return workflowRepository.saveAndFlush(existingWorkflow);
        return entityManager.merge(existingWorkflow);
    }


    public String deleteWorkflowById(int id) {
        Workflow workflow = workflowRepository.findById(id)
                .orElseThrow(() -> new WorkflowNotFoundException("No such workflow exists that can be deleted."));
        try {
            workflowRepository.delete(workflow);
            return workflow.getName();
        } catch (Exception e) {
            throw new WorkflowNotFoundException("Workflow couldn't be deleted.");
        }
    }

    public String activateWorkflow(int id) {
        Workflow workflow = workflowRepository.findById(id)
                .orElseThrow(() -> new WorkflowNotFoundException("No such workflow exists that can be deactivated."));
        try {
            workflow.setStatus(WorkflowStatus.ACTIVE);
            workflowRepository.save(workflow);
            return workflow.getName();
        } catch (Exception e) {
            throw new WorkflowNotFoundException("Workflow couldn't be deactivated.");
        }
    }

    public String deactivateWorkflow(int id) {
        Workflow workflow = workflowRepository.findById(id)
                .orElseThrow(() -> new WorkflowNotFoundException("No such workflow exists that can be deactivated."));
        try {
            workflow.setStatus(WorkflowStatus.INACTIVE);
            workflowRepository.save(workflow);
            return workflow.getName();
        } catch (Exception e) {
            throw new WorkflowNotFoundException("Workflow couldn't be deactivated.");
        }
    }

}
