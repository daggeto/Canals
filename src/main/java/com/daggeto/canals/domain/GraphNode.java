package com.daggeto.canals.domain;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Graph Node entity
 */
public class GraphNode {

  /**
   * Unique name of node
   */
  private String name;

  /**
   * Adjacent nodes mapped with distances
   */
  private Map<GraphNode, Integer> adjacentNodes;

  public GraphNode(String name) {
    this.name = name;
    this.adjacentNodes = new LinkedHashMap<>();
  }

  public void addAdjacentNode(GraphNode node, Integer weight) {
    adjacentNodes.put(node, weight);
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Map<GraphNode, Integer> getAdjacentNodes() {
    return adjacentNodes;
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof GraphNode)) {
      return false;
    }

    GraphNode compNode = (GraphNode) obj;

    return name.equals(compNode.getName());
  }

  @Override
  public String toString() {
    return getName();
  }
}
