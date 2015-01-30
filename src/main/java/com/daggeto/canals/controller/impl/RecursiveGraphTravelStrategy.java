package com.daggeto.canals.controller.impl;

import com.daggeto.canals.controller.GraphTravelStrategy;
import com.daggeto.canals.domain.GraphNode;
import com.daggeto.canals.visitor.NodeVisitor;
import com.daggeto.canals.visitor.NodeVisitor.VisitStatus;

import java.util.Map;

/**
 * Strategy that recursively travel through graph from start ( root ) node.
 */
public class RecursiveGraphTravelStrategy implements GraphTravelStrategy {

  private NodeVisitor visitor;

  @Override
  public void travel(GraphNode nodeFrom, NodeVisitor visitListener) {
    this.visitor = visitListener;

    travelRecursively(nodeFrom, null, 0);
  }

  private void travelRecursively(GraphNode currentNode, GraphNode parent, int totalWeight) {

    VisitStatus status = visitor.visit(currentNode, parent, totalWeight);

    if (VisitStatus.SKIP.equals(status) || VisitStatus.STOP.equals(status)) {
      return;
    }

    Map<GraphNode, Integer> adjacents = currentNode.getAdjacentNodes();


    for (GraphNode adjacent : adjacents.keySet()) {
      int weight = adjacents.get(adjacent);

      travelRecursively(adjacent, currentNode, totalWeight + weight);
    }

  }


}
