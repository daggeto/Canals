package com.daggeto.canals.visitor.impl;


import com.daggeto.canals.domain.GraphNode;
import com.daggeto.canals.visitor.NodeVisitor;
import com.google.common.base.Predicate;

import java.util.List;
import java.util.Map;

/**
 * Counts number of trips between start and stop nodes where number of stops that met targetCriteria
 */
public class TripCountVisitor implements NodeVisitor<Integer> {

    /**
     * Depth of travel when start node was found
     */
    private int startDepth = 0;

    /**
     * Total count of trips between start and stop nodes
     */
    private int count = 0;

    private boolean firstFounded = false;

    private String startNode;
    private String stopNode;

    private Predicate<Integer> targetCriteria;
    private Predicate<Integer> skipCriteria;

    /**
     *
     * @param startNode Node from which counting starts
     * @param stopNode Node where counting must be stopped
     * @param targetCriteria Criteria that must be met when travel reach target
     * @param skipCriteria Criteria that skips further travel
     */
    public TripCountVisitor(String startNode, String stopNode, Predicate<Integer> targetCriteria, Predicate<Integer> skipCriteria) {
        this.startNode = startNode;
        this.stopNode = stopNode;
        this.targetCriteria = targetCriteria;
        this.skipCriteria = skipCriteria;
    }

    @Override
    public VisitStatus visit(GraphNode node, GraphNode parent, Integer depth, boolean visited) {

        //stop if travel strategy starts visiting other nodes in same level as start node
        if(firstFounded && depth == startDepth){
            return VisitStatus.STOP;
        }

        //try to find start node
        if (startDepth == 0 && startNode.equals(node.getName())){
            startDepth = depth;

            firstFounded = true;
        }

        //continue if initial node passed to travel strategy isn't start node
        if(!firstFounded){
            return VisitStatus.CONTINUE;
        }

        int currentDepth = getCurrentDepth(depth);

        //skip further travel in this path
        if(skipCriteria.apply(currentDepth)){
            return VisitStatus.SKIP;
        }

        //inc number of founded trips
        //if visited node is in higher depth from start node and equals to stop node
        if(targetCriteria.apply(currentDepth) && currentDepth > 0 && stopNode.equals(node.getName())){
            count++;
        }

        return VisitStatus.CONTINUE;
    }

    @Override
    public VisitStatus visit(GraphNode node, GraphNode parent, Integer weight) {
        return visit(node, parent, 0, false);
    }

    /**
     * Number of founded trip between start and stop nodes that met special targetCriteria
     * @return Number of founded trip between start and stop nodes that met special targetCriteria
     */
    @Override
    public Integer getResult() {
        return count;
    }

    @Override
    public void orderAdjacentNodes(List<GraphNode> nodes, Map<GraphNode, Integer> weightMap) {

    }

    private int getCurrentDepth(int depth){
        return depth - startDepth;
    }
}
