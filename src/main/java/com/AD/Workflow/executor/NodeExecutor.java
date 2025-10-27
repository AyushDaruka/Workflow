package com.AD.Workflow.executor;

import com.AD.Workflow.domain.enums.NodeStatus;
import com.AD.Workflow.domain.model.BaseNode;
import com.AD.Workflow.domain.model.Workflow;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class NodeExecutor {

    private WebClient webClient;

    NodeExecutor(WebClient webClient) {
        this.webClient = webClient;
    }

//    public Map<String, Object> executeNode(Workflow workflow, BaseNode node, Map<String, Object> input) {
//        // Logic to execute the node with the given node
//        Map<String, Object> result = new HashMap<>();
//        Mono<Object> response;
//
//        switch(node.getType()) {
//            case "START":
//                response = this.executeNodeTask(workflow, node, input);
//                response.subscribe(obj -> {
//                    System.out.println("Received: " + obj);
//                });
//                result.put("status", "Node started successfully.");
//                break;
//            case "END":
//                this.endNodeTask(workflow, node, input);
//                result.put("status", "Node ended successfully.");
//                break;
//            default:
//                result.put("status", "Node executed successfully.");
//                break;
//        }
//
//        result.put("nodeId", node.getId());
//        result.put("nodeType", node.getType());
//        result.put("timestamp", System.currentTimeMillis());
//        return result;
//    }

    public Mono<Map<String, Object>> executeNodeTask(Workflow workflow, BaseNode node, Map<String, Object> input) {
        // Logic to execute the task with the given ID

        Map<String, Object> result = new HashMap<>();
        result.put("status", NodeStatus.PROCESSING);
        result.put("message", "Trigger node executed");
        result.put("triggerData", input != null ? input : new HashMap<>());

        if (input != null && input.containsKey("httpURI") && input.containsKey("httpHeaders")) {
            if (input.containsKey("requestPayload")) {
                return webClient.post()
                        .uri(input.get("httpURI").toString())
                        .retrieve()
                        .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {});
            } else {
                return webClient.get()
                        .uri("/data")
                        .retrieve()
                        .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {});
            }
        } else {
            throw new IllegalArgumentException();
        }

    }

    public void endNodeTask(Workflow workflow, BaseNode node, Map<String, Object> input) {
        // Logic to execute the task with the given ID
    }

    public void pauseNodeTask(Workflow workflow, BaseNode node, Map<String, Object> input) {
        // Logic to execute the task with the given ID
    }

    public void failNodeTask(Workflow workflow, BaseNode node, Map<String, Object> input) {
        // Logic to execute the task with the given ID

    }

}