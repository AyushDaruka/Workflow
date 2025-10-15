package com.AD.Workflow.domain.model;

import com.AD.Workflow.domain.enums.WorkflowStatus;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class Workflow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int workflowId;
    private WorkflowStatus status;
    private String name;
    @OneToMany
    private List<BaseNode> workflowNodes;
    @OneToMany
    private List<Connection> connections;

    public Workflow() {}

    public Workflow(int workflowId, String name, WorkflowStatus status) {
        this.workflowId = workflowId;
        this.name = name;
        this.status = status;
    }

    public int getWorkflowId() {
        return workflowId;
    }

    public void setWorkflowId(int workflowId) {
        this.workflowId = workflowId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public WorkflowStatus getStatus() {
        return status;
    }

    public void setStatus(WorkflowStatus status) {
        this.status = status;
    }

    public List<BaseNode> getWorkflowNodes() {
        return workflowNodes;
    }

    public void setWorkflowNodes(List<BaseNode> workflowNodes) {
        this.workflowNodes = workflowNodes;
    }

}
