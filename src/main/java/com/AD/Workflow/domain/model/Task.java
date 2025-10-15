package com.AD.Workflow.domain.model;


import com.AD.Workflow.domain.enums.TaskStatus;
import jakarta.persistence.*;

import java.time.LocalTime;

@Entity
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String name;

    String type;

    @ManyToOne
    @JoinColumn(name = "workflow_id", referencedColumnName = "workflowId")
    Workflow workflow;

    Long currentNodeId;

    TaskStatus status;

    @Lob
    String configuration;

    @Lob
    String result;

    LocalTime initiationTime;

    LocalTime completionTime;

    public Task(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Workflow getWorkflowId() {
        return workflow;
    }

    public void setWorkflowId(Workflow workflowId) {
        this.workflow = workflowId;
    }

    public Long getCurrentNodeId() {
        return currentNodeId;
    }

    public void setCurrentNodeId(Long currentNodeId) {
        this.currentNodeId = currentNodeId;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public String getConfiguration() {
        return configuration;
    }

    public void setConfiguration(String configuration) {
        this.configuration = configuration;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public LocalTime getInitiationTime() {
        return initiationTime;
    }

    public void setInitiationTime(LocalTime initiationTime) {
        this.initiationTime = initiationTime;
    }

    public LocalTime getCompletionTime() {
        return completionTime;
    }

    public void setCompletionTime(LocalTime completionTime) {
        this.completionTime = completionTime;
    }


}
