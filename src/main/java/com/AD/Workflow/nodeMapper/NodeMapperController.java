package com.AD.Workflow.nodeMapper;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/mapper")
@Tag(name = "Node Mapper", description = "Node Mapper endpoints")

public class NodeMapperController {

    @Autowired
    private MapperRepository mapperRepository;

    @GetMapping("/ping")
    public String ping() {
        return "Node Mapper Service is up and running!";
    }

    @GetMapping(
        value = "/responseToNextRequest/{workflowId}/{connectionId}",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public String test(
            @PathVariable Integer workflowId,
            @PathVariable Integer connectionId
    ) {
        return "Mapping completed successfully.";
    }

    @PostMapping(
        value = "/responseToNextRequest",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public String mapResponseToNextRequest(
            @Valid @RequestBody ResponseToNextRequestMapper responseToNextRequestMapper
    ) {
        List<ResponseToNextRequestMapper> existingMappers = mapperRepository.findByWorkflowIdAndConnectionId(
                responseToNextRequestMapper.getWorkflowId(),
                responseToNextRequestMapper.getConnectionId()
        );
        if (!existingMappers.isEmpty()) {
            throw new RuntimeException("Mapping already exists for the given workflowId and connectionId.");
        }
        responseToNextRequestMapper.setCreatedAt(java.time.Instant.now());
        mapperRepository.save(responseToNextRequestMapper);
        return "Mapping completed successfully.";
    }


    @PutMapping(
            value = "/responseToNextRequest",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public String updateMapping(
            @Valid @RequestBody ResponseToNextRequestMapper responseToNextRequestMapper
    ) {
        List<ResponseToNextRequestMapper> existingMappers = mapperRepository.findByWorkflowIdAndConnectionId(
                responseToNextRequestMapper.getWorkflowId(),
                responseToNextRequestMapper.getConnectionId()
        );

        if (existingMappers.isEmpty()) {
            throw new RuntimeException("Mapping not found for the given workflowId and connectionId.");
        } else if(existingMappers.size() > 1) {
            throw new RuntimeException("Multiple mappings found for the given workflowId and connectionId.");
        }
        existingMappers.get(0).setTransformationMappings(responseToNextRequestMapper.getTransformationMappings());
        existingMappers.get(0).setUpdatedAt(java.time.Instant.now());
        mapperRepository.save(existingMappers.get(0));
        return "Mapping updated successfully.";
    }

}
