package com.AD.Workflow.nodeMapper;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MapperRepository extends MongoRepository<ResponseToNextRequestMapper, String> {
    List<ResponseToNextRequestMapper> findByWorkflowIdAndConnectionId(
            Integer workflowId,
            Integer connectionId
    );
}
