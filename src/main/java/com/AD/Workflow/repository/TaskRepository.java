package com.AD.Workflow.repository;

import com.AD.Workflow.domain.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {

    @Query("SELECT t FROM Task t WHERE t.workflow.id = ?1")
    List<Task> findByWorkflowId(int workflowId);

}
