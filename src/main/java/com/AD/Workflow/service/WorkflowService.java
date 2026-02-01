package com.AD.Workflow.service;


import com.AD.Workflow.domain.enums.WorkflowStatus;
import com.AD.Workflow.domain.model.BaseNode;
import com.AD.Workflow.domain.model.Workflow;
import com.AD.Workflow.exception.WorkflowNotFoundException;
import com.AD.Workflow.repository.WorkflowRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
//import reactor.core.publisher.Mono;

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
//        updatedWorkflow.getConnections().forEach(existingWorkflow::addConnection);
//        updatedWorkflow.getWorkflowNodes().forEach(existingWorkflow::addWorkflowNode);
        return workflowRepository.save(updatedWorkflow);
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

    public Workflow addNodeToWorkflow(int workflowId, BaseNode node) {
        Optional<Workflow> workflow = workflowRepository.findById(workflowId);
        if (workflow.isPresent()) {
            Workflow existingWorkflow = workflow.get();
            existingWorkflow.getWorkflowNodes().add(node);
            node.setWorkflow(existingWorkflow);
            workflowRepository.save(existingWorkflow);
            return existingWorkflow;
        }
        throw new WorkflowNotFoundException("No such workflow exists.");
    }

    public Workflow deleteNodeFromWorkflow(int workflowId, Long nodeId) {
        Optional<Workflow> workflow = workflowRepository.findById(workflowId);
        if (workflow.isPresent()) {
            Workflow existingWorkflow = workflow.get();
            BaseNode nodeToRemove = existingWorkflow.getWorkflowNodeById(nodeId);
            if (nodeToRemove != null) {
                existingWorkflow.getWorkflowNodes().remove(nodeToRemove);
                workflowRepository.save(existingWorkflow);
                return existingWorkflow;
            } else {
                throw new WorkflowNotFoundException("No such node exists in the workflow.");
            }
        }
        throw new WorkflowNotFoundException("No such workflow exists.");
    }

}
