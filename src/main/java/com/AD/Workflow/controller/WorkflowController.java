package com.AD.Workflow.controller;

import com.AD.Workflow.domain.enums.WorkflowStatus;
import com.AD.Workflow.domain.model.BaseNode;
import com.AD.Workflow.domain.model.Workflow;
import com.AD.Workflow.dto.ErrorResponseDTO;
import com.AD.Workflow.dto.WorkflowDTO;
import com.AD.Workflow.repository.WorkflowRepository;
import com.AD.Workflow.service.EmailNotificationService;
import com.AD.Workflow.service.WorkflowService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
//import reactor.core.publisher.Flux;
//import reactor.core.scheduler.Schedulers;
//import reactor.core.publisher.Mono;

import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/")
@Tag(name = "Workflow", description = "Workflow management endpoints")
public class WorkflowController {
    @Autowired
    WorkflowRepository workflowRepository;

    @Autowired
    EmailNotificationService emailService;

    @Autowired
    WorkflowService workflowService;

    @GetMapping("/ping")
    @Operation(summary = "Verify if the application is up and running")
    public ResponseEntity<String> hello() {
//        emailService.sendEmail("ayushdaruka@outlook.com", "Test Message", "Test Message");
        return ResponseEntity.ok("Hello World!");
    }

    @PostMapping("/workflows")
    @Operation(summary = "Create a workflow")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Workflow created"),
            @ApiResponse(responseCode = "409", description = "Conflict - resource already exists with same name")
    })
    public ResponseEntity<String> createWorkflow(@Valid @RequestBody WorkflowDTO workflowRequest) {
        Workflow workflowReq = new Workflow(workflowRequest.getName());

        Optional<Workflow> createdWorkflowOptional = workflowService.addWorkflow(workflowReq);
        Workflow createdWorkflow = workflowService.addWorkflow(workflowReq)
                .orElseThrow(() -> new RuntimeException("Failed to create workflow"));
        return ResponseEntity.status(HttpStatus.CREATED).body(
            MessageFormat.format(
                    "Workflow {0} created successfully with reference Id {1}.",
                    createdWorkflow.getName(),
                    createdWorkflow.getWorkflowId()
            )
        );
    }

//    public Mono<ResponseEntity<String>> createWorkflow(@Valid @RequestBody WorkflowDTO workflowRequest) {
//        Workflow workflowReq = new Workflow(workflowRequest.getName());
//
//        return Mono.justOrEmpty(workflowService.addWorkflow(workflowReq))
//                .map(workflow -> ResponseEntity.status(HttpStatus.CREATED).body(
//                        MessageFormat.format(
//                                "Workflow {0} created successfully with reference Id {1}.",
//                                workflow.getName(),
//                                workflow.getWorkflowId()
//                        )
//                ))
//                .onErrorMap(DataIntegrityViolationException.class,
//                        e -> {
//                            return new DataIntegrityViolationException("Workflow with the same name already exists.");
//                        }
//                )
//                .onErrorMap(IllegalArgumentException.class,
//                        e -> {
//                            return new IllegalArgumentException("Invalid workflow data provided.");
//                        }
//                );
//
//
//    }

    @PutMapping(value = "/workflows/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update a workflow")
    public ResponseEntity<Workflow> updateWorkflow(
            @PathVariable int id,
            @Valid @RequestBody WorkflowDTO workflowDTO
    ) {
        Workflow updatedWorkflow = new Workflow();
        updatedWorkflow.setWorkflowId(id);
        updatedWorkflow.setName(workflowDTO.getName());
        updatedWorkflow.setStatus(WorkflowStatus.CREATED); // Reset status to CREATED on update
        updatedWorkflow.setWorkflowNodes(workflowDTO.getWorkflowNodes());
        updatedWorkflow.setConnections(workflowDTO.getConnections());

        try {
            Workflow workflow = workflowService.updateWorkflow(id, updatedWorkflow);
            return ResponseEntity.ok(workflow);
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

//    public Mono<ResponseEntity<Workflow>> updateWorkflow(
//            @PathVariable int id,
//            @RequestBody Mono<WorkflowDTO> workflowDTOMono
//    ) {
//        Mono<Workflow> updatedWorkflowMono = workflowDTOMono.map(workflowDTO -> {
//            Workflow updatedWorkflow = new Workflow();
//            updatedWorkflow.setWorkflowId(id);
//            updatedWorkflow.setName(workflowDTO.getName());
//            updatedWorkflow.setStatus(WorkflowStatus.CREATED); // Reset status to CREATED on update
//            updatedWorkflow.setWorkflowNodes(workflowDTO.getWorkflowNodes());
//            updatedWorkflow.setConnections(workflowDTO.getConnections());
//            return updatedWorkflow;
//        });
//        return updatedWorkflowMono
//                .flatMap(updatedWorkflow -> Mono.fromCallable(() -> workflowService.updateWorkflow(id, updatedWorkflow))
//                        .subscribeOn(Schedulers.boundedElastic()))
//                .map(workflow -> ResponseEntity.ok(workflow))
//                .onErrorResume(DataIntegrityViolationException.class,
//                        e -> Mono.just(ResponseEntity.status(HttpStatus.CONFLICT).build()))
//                .onErrorResume(IllegalArgumentException.class,
//                        e -> Mono.just(ResponseEntity.badRequest().build()));
//    }
    
    @GetMapping("/workflows")
    @Operation(summary = "Get all workflows")
    @Transactional
    public List<Workflow> getAllWorkflows(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size,
        @RequestParam(defaultValue = "workflowId") String sortBy,
        @RequestParam(defaultValue = "asc") String sortDir
    ) {
        long offset = (long) page * size;

        return workflowService.findPage(sortBy, size, page);
    }

    @GetMapping("/workflows/{id}")
    @Operation(summary = "Get a workflow")
    public ResponseEntity<Workflow> getWorkflowById(@PathVariable int id) {
        Workflow workflow = workflowService.getWorkflowById(id);
        return ResponseEntity.ok(workflow);
    }

    @DeleteMapping("/workflows/{id}")
    @Operation(summary = "Delete a workflow")
    public ResponseEntity<String> deleteWorkflowById(@PathVariable int id) {
        String workflowName = workflowService.deleteWorkflowById(id);
        return ResponseEntity.ok(
            MessageFormat.format(
                "Workflow {0} deleted successfully.",
                    workflowName
            )
        );
    }

    @PostMapping("workflows/{id}/activate")
    @Operation(summary = "Activate a workflow")
    public ResponseEntity<String> activateWorkflow(@PathVariable int id) {
        return ResponseEntity.ok(workflowService.activateWorkflow(id));
    }

    @PostMapping("workflows/{id}/deactivate")
    @Operation(summary = "Deactivate a workflow")
    public ResponseEntity<String> deactivateWorkflow(@PathVariable int id) {
        return ResponseEntity.ok(workflowService.deactivateWorkflow(id));
    }

    @PutMapping("/workflows/{id}/addNode")
    @Operation(summary = "Add a node to a workflow")
    public ResponseEntity<Workflow> addNodeToWorkflow(
            @PathVariable int id,
            @Valid @RequestBody BaseNode nodeDTO
    ) {
        Workflow workflow = workflowService.addNodeToWorkflow(id, nodeDTO);
        return ResponseEntity.ok(workflow);
    }

    @DeleteMapping("/workflows/{id}/{deleteNodeId}")
    @Operation(summary = "Delete a node from a workflow")
    public ResponseEntity<Workflow> deleteNodeFromWorkflow(
            @PathVariable int id,
            @PathVariable Long deleteNodeId
    ) {
        Workflow workflow = workflowService.deleteNodeFromWorkflow(id, deleteNodeId);
        return ResponseEntity.ok(workflow);
    }


}
