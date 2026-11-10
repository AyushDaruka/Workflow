package com.AD.Workflow.nodeMapper;

import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

//@Document(collection = "response_to_request_mapper")
//public class ResponseToNextRequestMapper {
//
//    int workflowId;
//    int nodeId;
//
//
//}

@Data
@Document(collection = "response_to_request_mapper")
public class ResponseToNextRequestMapper {

    @Id
    private String id;

    @NotNull(message = "Workflow ID cannot be null")
    private Integer workflowId;

    @OneToOne
    @NotNull(message = "Connection ID cannot be null")
    private Integer connectionId;
    /**
     * Version of the transformation configuration format.
     */
//    private int version = 1;

    /**
     * AST describing how source output becomes target input.
     */

    @NotNull(message = "Transformation mappings cannot be null")
    private List<FieldMapping> transformationMappings;

    private Instant createdAt;

    private Instant updatedAt;
}
