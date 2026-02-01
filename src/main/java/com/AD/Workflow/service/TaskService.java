package com.AD.Workflow.service;

import ch.qos.logback.core.pattern.parser.OptionTokenizer;
import com.AD.Workflow.domain.enums.TaskStatus;
import com.AD.Workflow.domain.model.Task;
import com.AD.Workflow.domain.model.Workflow;
import com.AD.Workflow.executor.NodeExecutor;
import com.AD.Workflow.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
//import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class TaskService {

    @Autowired
    private NodeExecutor nodeExecutor;

    @Autowired
    private JsonMapper mapper;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    EmailNotificationService emailNotificationService;

    public void terminateTask(Task task) {
        switch(task.getStatus()) {
            case PENDING:
            case IN_PROGRESS:
            case PAUSED:
                Workflow wf = task.getWorkflow();
                nodeExecutor.endNodeTask(
                        wf,
                        wf.getWorkflowNodeById(task.getCurrentNodeId()),
                        mapper.convertJsonStringToObject(task.getProperties())
                );
                task.setStatus(TaskStatus.CANCELLED);
        }
        return;
    }

    public void executeTask(Task task) {
//        switch(task.getStatus()) {
//            case PENDING:
//            case PAUSED:
//                Workflow wf = task.getWorkflow();
//                Mono<Map<String, Object>> mono = nodeExecutor.executeNodeTask(
//                        wf,
//                        wf.getWorkflowNodeById(task.getCurrentNodeId()),
//                        mapper.convertJsonStringToObject(task.getProperties())
//                );
//
//                mono.subscribe(
//                        value -> {
//                            emailNotificationService.sendEmail(
//                                    "ayushdaruka@outlook.com",
//                                    "Execution Update",
//                                    "The following response was received: \n" + mapper.convertObjectToJson(value)
//                            );
//                            // basis the value status, get the next node from the
//                            task.setStatus(TaskStatus.COMPLETED);
//                        },
//                        error -> {
//                            task.setStatus(TaskStatus.FAILED);
//                        }
//                );
//                task.setStatus(TaskStatus.IN_PROGRESS);
//        }
        return;
    }

    public void pauseTask(Task task) {
        switch(task.getStatus()) {
            case PENDING:
            case IN_PROGRESS:
                Workflow wf = task.getWorkflow();
                nodeExecutor.pauseNodeTask(
                        wf,
                        wf.getWorkflowNodeById(task.getCurrentNodeId()),
                        mapper.convertJsonStringToObject(task.getProperties())
                );
                task.setStatus(TaskStatus.PAUSED);
        }
        return;
    }

    public void resumeTask(Task task) {
//        switch(task.getStatus()) {
//            case PAUSED:
//                Workflow wf = task.getWorkflow();
//                nodeExecutor.executeNodeTask(
//                        wf,
//                        wf.getWorkflowNodeById(task.getCurrentNodeId()),
//                        mapper.convertJsonStringToObject(task.getProperties())
//                );
//                task.setStatus(TaskStatus.IN_PROGRESS);
//        }
        return;
    }

    public void completeTask(Task task) {
        switch (task.getStatus()) {
            case PENDING:
            case IN_PROGRESS:
            case PAUSED:
                Workflow wf = task.getWorkflow();
                nodeExecutor.endNodeTask(
                        wf,
                        wf.getWorkflowNodeById(task.getCurrentNodeId()),
                        mapper.convertJsonStringToObject(task.getProperties())
                );
                task.setStatus(TaskStatus.COMPLETED);
        }
        return;
    }

    public void retryTask(Task task) {
//        switch (task.getStatus()) {
//            case FAILED:
//            case CANCELLED:
//                Workflow wf = task.getWorkflow();
//                nodeExecutor.executeNodeTask(
//                        wf,
//                        wf.getWorkflowNodeById(task.getCurrentNodeId()),
//                        mapper.convertJsonStringToObject(task.getProperties())
//                );
//                task.setStatus(TaskStatus.PENDING);
//        }
        return;
    }

    public Optional<Task> findTaskById(int taskId) {
        return taskRepository.findById(taskId);
    }

//    public void failTask(Task task) {
//        switch (task.getStatus()) {
//            case PENDING:
//            case IN_PROGRESS:
//            case PAUSED:
//                Workflow wf = task.getWorkflow();
//                nodeExecutor.failNodeTask(
//                        wf,
//                        wf.getWorkflowNodeById(task.getCurrentNodeId()),
//                        mapper.convertJsonToObject(task.getConfiguration())
//                );
//                task.setStatus(TaskStatus.FAILED);
//        }
//        return;
//    }

//    public void scheduleTask(Task task) {
//        switch (task.getStatus()) {
//            case PAUSED:
//            case IN_PROGRESS:
//                Workflow wf = task.getWorkflow();
//                nodeExecutor.scheduleNodeExecution(
//                        wf,
//                        wf.getWorkflowNodeById(task.getCurrentNodeId()),
//                        mapper.convertJsonToObject(task.getConfiguration())
//                );
//                task.setStatus(TaskStatus.PENDING);
//                break;
//            case CANCELLED:
//            case COMPLETED:
//            case FAILED:
//                throw new IllegalStateException("Cannot schedule a task that is " + task.getStatus());
//        }
//        return;
//    }
}
