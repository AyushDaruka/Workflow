package com.AD.Workflow.execution.resolver;

import com.AD.Workflow.domain.enums.ConnectionType;
import com.AD.Workflow.domain.model.BaseNode;
import com.AD.Workflow.domain.model.Connection;
import com.AD.Workflow.repository.ConnectionRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TransitionResolver {

    ConnectionRepository connectionRepository;

    TransitionResolver(
            ConnectionRepository connectionRepository
    ) {
        // Constructor implementation
        this.connectionRepository = connectionRepository;
    }

    public List<BaseNode> resolveNextNodes(BaseNode currentNode) {
        // Use the connectionRepository to find the next nodes based on the current node
        List<Connection> connections = connectionRepository.findBySourceNodeId(currentNode.getId());
//        List<BaseNode> nextNodes = new ArrayList<>();
//        for (Connection connection : connections) {
//            nextNodes.add(connection.getTargetNode());
//        }
//        List<BaseNode> nextnodes = connections.stream()
//                                    .map(Connection::getTargetNode)
//                                    .toList();
//        return nextNodes;
        return connections.stream()
                .map(Connection::getTargetNode)
                .toList();
    }

    public List<BaseNode> resolveNextNodesByConnectionType(BaseNode currentNode, List<ConnectionType> connectionTypes) {
        // Use the connectionRepository to find the next nodes based on the current node
        List<Connection> connections = connectionRepository.findBySourceNodeId(currentNode.getId());
        return connections.stream()
                .filter(c -> connectionTypes.contains(c.getConnectionType()))
                .map(Connection::getTargetNode)
                .toList();
    }
}
