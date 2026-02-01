package com.AD.Workflow.domain.model;

import com.AD.Workflow.domain.enums.WorkflowStatus;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
public class Workflow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int workflowId;
    private WorkflowStatus status;
    @Column(unique = true)
    private String name;
    @OneToMany(
            mappedBy = "workflow",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<BaseNode> workflowNodes;
    @OneToMany(
            mappedBy = "workflow",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER)
    private List<Connection> connections;

    public Workflow(){
        workflowNodes = new ArrayList<>();
        connections = new ArrayList<>();
    }
    public Workflow(String name) {
        this.name = name;
        this.status = WorkflowStatus.CREATED;
        workflowNodes = new ArrayList<>();
        connections = new ArrayList<>();
    }

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

    public BaseNode getWorkflowNodeById(Long nodeId) {
        Optional<BaseNode> node  = workflowNodes.stream()
                .filter(n -> n.getId().equals(nodeId))
                .findFirst();
        return node.orElse(null);
    }

    public void setWorkflowNodes(List<BaseNode> workflowNodes) {
        this.workflowNodes = workflowNodes.stream()
                .map(node -> {
                    node.setWorkflow(this);
                    return node;
                }).toList();
    }

    public List<Connection> getConnections() {
        return connections;
    }

    public void setConnections(List<Connection> connections) {
        this.connections = connections.stream()
                    .map(connection -> {
                                connection.setWorkflow(this);
                                return connection;
                    }).toList();
    }

    public void addWorkflowNode(BaseNode node) {
        this.workflowNodes.add(node);
        node.setWorkflow(this);
    }

    public void addConnection(Connection connection) {

        this.connections.add(connection);
        connection.setWorkflow(this);
    }

    public void removeWorkflowNode(BaseNode node) {
        this.workflowNodes.remove(node);
//        -- The base nodes not linked to parent are auto removed.
//        node.setWorkflow(null);
    }

    public void removeConnection(Connection connection) {
        this.connections.remove(connection);
//        -- The connection links not linked to parent are auto removed.
//        connection.setWorkflow(null);
    }

}
