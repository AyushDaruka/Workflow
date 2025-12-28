package com.AD.Workflow.repository;

import com.AD.Workflow.domain.model.Workflow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkflowRepository extends JpaRepository<Workflow, Integer> {

    @Query(value = "SELECT * FROM workflow ORDER BY ?1 LIMIT ?2 OFFSET ?3", nativeQuery = true)
    List<Workflow> findPage(String sortBy, int size, long offset);

}
