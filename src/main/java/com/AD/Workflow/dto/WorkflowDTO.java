package com.AD.Workflow.dto;

import com.AD.Workflow.domain.model.BaseNode;
import com.AD.Workflow.domain.model.Connection;

import java.util.List;

public class WorkflowDTO {

    private int workflowId;
    private String status;
    private String name;
    private List<BaseNode> workflowNodes;
    private List<Connection> connections;

    public WorkflowDTO() {}

    public WorkflowDTO(int workflowId, String name, String status) {
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<BaseNode> getWorkflowNodes() {
        return workflowNodes;
    }

    public void setWorkflowNodes(List<BaseNode> workflowNodes) {
        this.workflowNodes = workflowNodes;
    }

    public List<Connection> getConnections() {
        return connections;
    }

    public void setConnections(List<Connection> connections) {
        this.connections = connections;
    }
}
