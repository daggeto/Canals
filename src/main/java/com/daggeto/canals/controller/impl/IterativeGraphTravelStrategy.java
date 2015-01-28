package com.daggeto.canals.controller.impl;

import com.daggeto.canals.controller.GraphTravelStrategy;
import com.daggeto.canals.controller.adapter.CollectionAdapter;
import com.daggeto.canals.controller.dto.GraphNodeDto;
import com.daggeto.canals.domain.GraphNode;
import com.daggeto.canals.visitor.NodeVisitor;
import com.daggeto.canals.visitor.NodeVisitor.VisitStatus;

import java.util.*;

/**
 * Implementation of iterative graph traversal strategy.
 */
public class IterativeGraphTravelStrategy implements GraphTravelStrategy {

    CollectionAdapter<GraphNodeDto> collectionAdapter;

    Set<GraphNode> visited = new HashSet<>();

    public IterativeGraphTravelStrategy(CollectionAdapter<GraphNodeDto> collectionAdapter) {
        this.collectionAdapter = collectionAdapter;
    }

    @Override
    public void travel(GraphNode root, NodeVisitor visitor) {

        if(collectionAdapter == null){
            throw new IllegalArgumentException("CollectionAdapter must be defined with constructor");
        }

        collectionAdapter.add(new GraphNodeDto(root, null, 0));

        while( collectionAdapter.hasNext() ){

            GraphNodeDto currentNodeDto = collectionAdapter.takeNext();

            VisitStatus status = visitor.visit(
                    currentNodeDto.node,
                    currentNodeDto.parent,
                    currentNodeDto.depth,
                    visited.contains(currentNodeDto.node)
            );

            if(VisitStatus.SKIP.equals(status)){
                continue;
            }

            if(VisitStatus.STOP.equals(status)){
                break;
            }

            visited.add(currentNodeDto.node);

            Set<GraphNode> adjacentNodes = currentNodeDto.node.getAdjacentNodes().keySet();
            List<GraphNode> nodeList = new ArrayList<>(adjacentNodes);

            visitor.orderAdjacentNodes(nodeList, currentNodeDto.node.getAdjacentNodes());

            for( GraphNode adjacentNode : nodeList){
                Integer weight = currentNodeDto.node.getAdjacentNodes().get(adjacentNode);

                collectionAdapter.add(new GraphNodeDto(adjacentNode, currentNodeDto.node, currentNodeDto.depth + 1));
            }

        }
    }
}
