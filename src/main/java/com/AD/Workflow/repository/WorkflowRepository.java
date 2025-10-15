package com.AD.Workflow.repository;

import com.AD.Workflow.domain.model.Workflow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkflowRepository extends JpaRepository<Workflow, Integer> {

}
