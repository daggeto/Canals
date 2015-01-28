package com.daggeto.canals.visitor.impl;

import com.daggeto.canals.domain.GraphNode;
import com.daggeto.canals.visitor.NodeVisitor;

import java.util.*;

/**
 * Finds distance of path if it exists
 */
public class PathDistanceVisitor implements NodeVisitor<Integer> {

    /**
     * Path that need to be found
     */
    private Queue<String> targetPath;

    /**
     * Depth from target path that is observable
     */
    private int currentDepth = 0;

    /**
     * Total distance of targetPath
     */
    private int distance = 0;

    /**
     * Flag indicates that path is already founded
     */
    private Boolean exists = false;

    public PathDistanceVisitor(Queue<String> stringQueue){
        targetPath = stringQueue;
    }

    @Override
    public VisitStatus visit(GraphNode node, GraphNode parent, Integer depth, boolean visited) {
        //if there is no more target nodes then set exist flag true and stop graph processing
        if(targetPath.isEmpty()){
            exists = true;
            return VisitStatus.STOP;
        }

        //if visited node depth is same as observable depth of target and this node from target path
        if(depth.equals(currentDepth) && node.getName().equals(targetPath.peek())){
            currentDepth++;

            Integer weight = getWeight(node, parent);
            distance+=weight;

            targetPath.remove();
            return VisitStatus.CONTINUE;
        }

        return VisitStatus.SKIP;
    }

    private Integer getWeight(GraphNode node, GraphNode parent){
        if(parent == null){
            return 0;
        }

        return parent.getAdjacentNodes().get(node);
    }

    @Override
    public VisitStatus visit(GraphNode node, GraphNode parent, Integer weight) {
        return visit(node, parent, 0, false);
    }

    @Override
    public Integer getResult() {

        if(!exists){
            return 0;
        }

        return distance;
    }

    @Override
    public void orderAdjacentNodes(List<GraphNode> nodes, Map<GraphNode, Integer> weightMap) {

    }
}
