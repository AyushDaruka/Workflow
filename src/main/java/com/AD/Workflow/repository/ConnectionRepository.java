package com.AD.Workflow.repository;

import com.AD.Workflow.domain.model.Connection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConnectionRepository extends JpaRepository<Connection, Integer> {
    @Query
    List<Connection> findBySourceNodeId(Long sourceNodeId);

    @Query
    List<Connection> findByIdAndWorkflow_WorkflowId(int connectionId, int workflowId);

    @Query("DELETE FROM Connection c WHERE c.id = ?1 AND c.workflow.workflowId = ?2")
    List<Connection> deleteByIdAndWorkflowId(int connectionId, int workflowId);
}
