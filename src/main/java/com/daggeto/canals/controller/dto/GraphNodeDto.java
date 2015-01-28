package com.daggeto.canals.controller.dto;

import com.daggeto.canals.domain.GraphNode;

/**
 * Dto used to store nodes that need be to visited in {@link com.daggeto.canals.controller.impl.IterativeGraphTravelStrategy}
 */
public class GraphNodeDto{

    public GraphNode node;
    public GraphNode parent;

    /**
     * Level or depth in which node is visited
     */
    public Integer depth;

    public GraphNodeDto(GraphNode node, GraphNode parent, Integer depth) {
        this.node = node;
        this.depth = depth;
        this.parent = parent;
    }
}
