package com.AD.Workflow.domain.model;

import com.AD.Workflow.domain.enums.ConnectionType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
public class Connection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String label;
    private String condition;
    private ConnectionType connectionType;

//    @Version
//    private Long version;

    @ManyToOne
//    @JoinColumn(name = "source_node_id")
    @JsonIgnore
    private BaseNode sourceNode;

    @ManyToOne
//    @JoinColumn(name = "target_node_id")
    @JsonIgnore
    private BaseNode targetNode;

    @ManyToOne
    @JoinColumn(name = "workflowId")
    @JsonIgnore
    private Workflow workflow;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public BaseNode getSourceNode() {
        return sourceNode;
    }

    public void setSourceNode(BaseNode source) {
        this.sourceNode = source;
    }

    public BaseNode getTargetNode() {
        return targetNode;
    }

    public void setTargetNode(BaseNode target) {
        this.targetNode = target;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public ConnectionType getConnectionType() {
        return connectionType;
    }

    public void setConnectionType(ConnectionType connectionType) {
        this.connectionType = connectionType;
    }

    public Workflow getWorkflow() {
        return workflow;
    }

    public void setWorkflow(Workflow workflow) {
        this.workflow = workflow;
    }

}