package com.daggeto.canals.utils;

import com.daggeto.canals.domain.GraphNode;

/**
 * Create graph by add adjacent nodes
 */
public class GraphRelationsBuilder {

  GraphNode forNode;

  public GraphRelationsBuilder(GraphNode forNode) {
    this.forNode = forNode;
  }

  public static GraphRelationsBuilder forNode(GraphNode forNode) {
    return new GraphRelationsBuilder(forNode);
  }

  public GraphRelationsBuilder withAdjacent(GraphNode node, int weight) {
    forNode.addAdjacentNode(node, weight);
    return this;
  }

  public GraphNode build() {
    return forNode;
  }
}
