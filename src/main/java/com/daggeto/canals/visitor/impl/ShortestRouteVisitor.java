package com.daggeto.canals.visitor.impl;


import com.daggeto.canals.domain.GraphNode;
import com.daggeto.canals.visitor.NodeVisitor;

import java.util.*;

/**
 * Visitor calculates shortest distances from start node to all other nodes in graph.
 * Return shortest distance from start node to stop node
 */
public class ShortestRouteVisitor implements NodeVisitor<Integer> {

    private Map<GraphNode, Integer> distances;

    private boolean firstFounded = false;

    private String startNode;
    private String stopNode;

    private GraphNode targetNode;

    public ShortestRouteVisitor(String startNode, String stopNode) {
        this.startNode = startNode;
        this.stopNode = stopNode;

        this.distances = new HashMap<>();
    }

    @Override
    public VisitStatus visit(GraphNode node, GraphNode parent, Integer depth, boolean visited) {

        //try to find start node
        if(!firstFounded && startNode.equals(node.getName())){
            firstFounded = true;
            return VisitStatus.CONTINUE;
        }

        //continue if initial node passed to travel strategy isn't start node
        if(!firstFounded){
            return VisitStatus.CONTINUE;
        }


        Integer distanceFromStart = getDistance(node, parent);
        updateDistance(node, distanceFromStart);

        //try to find stop node
        if(targetNode == null && stopNode.equals(node.getName())){
            targetNode = node;
        }

        //skip visited nodes
        if(visited){
            return VisitStatus.SKIP;
        }

        return VisitStatus.CONTINUE;
    }

    private void updateDistance(GraphNode node, Integer distanceFromStart) {
        Integer currentDistance = distances.get(node);

        if(currentDistance == null){
            distances.put(node, distanceFromStart);
            return;
        }

        if(currentDistance > distanceFromStart){
            distances.put(node, distanceFromStart);
        }
    }

    private Integer getDistance(GraphNode node, GraphNode parent) {

        int distanceFromParent = parent.getAdjacentNodes().get(node);

        if(parent != targetNode && distances.get(parent) != null){
            distanceFromParent+= distances.get(parent);
        }

        return distanceFromParent;
    }

    @Override
    public VisitStatus visit(GraphNode node, GraphNode parent, Integer weight) {
        return visit(node, parent, 0, false);
    }

    /**
     * @return Shortest distance from start to stop nodes
     */
    @Override
    public Integer getResult() {
        return distances.get(targetNode);
    }

    /**
     * Order nodes by ascending edges weight. Less weighing edge will be visited first
     */
    public void orderAdjacentNodes(List<GraphNode> nodes, final Map<GraphNode, Integer> weightMap){
        Comparator<GraphNode> comparator = new Comparator<GraphNode>() {
            @Override
            public int compare(GraphNode node1, GraphNode node2) {
                return weightMap.get(node2) - weightMap.get(node1);
            }
        };

        Collections.sort(nodes, comparator);
    }
}
