package com.daggeto.canals.visitor.impl;

import com.daggeto.canals.domain.GraphNode;
import com.daggeto.canals.visitor.NodeVisitor;

import java.util.List;
import java.util.Map;
import java.util.Queue;

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

  public PathDistanceVisitor(Queue<String> stringQueue) {
    targetPath = stringQueue;
  }

  @Override
  public VisitStatus visit(GraphNode node, GraphNode parent, Integer depth, boolean visited) {

    //if visited node depth is same as observable depth of target and this node from target path
    if (depth.equals(currentDepth) && node.getName().equals(targetPath.peek())) {
      currentDepth++;

      Integer weight = getWeight(node, parent);
      distance += weight;

      targetPath.remove();
      return VisitStatus.CONTINUE;
    }

    //stop travel when in target path is no more nodes to search
    if (targetPath.isEmpty()) {
      return VisitStatus.STOP;
    }

    return VisitStatus.SKIP;
  }

  private Integer getWeight(GraphNode node, GraphNode parent) {
    if (parent == null) {
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
    //if there still target nodes then then path not exists
    if (!targetPath.isEmpty()) {
      return 0;
    }
    return distance;
  }

  @Override
  public void orderAdjacentNodes(List<GraphNode> nodes, Map<GraphNode, Integer> weightMap) {

  }
}
