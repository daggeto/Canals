package com.daggeto.canals.visitor.impl;

import com.daggeto.canals.domain.GraphNode;
import com.daggeto.canals.visitor.NodeVisitor;
import com.google.common.base.Predicate;

import java.util.List;
import java.util.Map;

/**
 * Finds count of all paths from start node to stop node where total weight met criteria
 */
public class AllPathsVisitor implements NodeVisitor<Integer> {

  private String startNode;
  private String stopNode;

  private Predicate<Integer> criteria;

  private boolean firstFounded = false;

  private int count = 0;

  /**
   * @param startNode Node from which counting starts
   * @param stopNode  Node where counting must be stopped
   * @param criteria  Criteria to compare total weight in observable path
   */
  public AllPathsVisitor(String startNode, String stopNode, Predicate<Integer> criteria) {
    this.startNode = startNode;
    this.stopNode = stopNode;
    this.criteria = criteria;
  }

  @Override
  public VisitStatus visit(GraphNode node, GraphNode parent, Integer depth, boolean visited) {
    int weight = parent.getAdjacentNodes().get(node);
    return visit(node, parent, weight);
  }

  @Override
  public VisitStatus visit(GraphNode node, GraphNode parent, Integer weight) {

    //stop if criteria doesn't met
    if (!criteria.apply(weight)) {
      return VisitStatus.STOP;
    }

    //try to find start node
    if (!firstFounded == startNode.equals(node.getName())) {
      firstFounded = true;
      return VisitStatus.CONTINUE;
    }

    //continue if initial node passed to travel strategy isn't start node
    if (!firstFounded) {
      return VisitStatus.CONTINUE;
    }

    //increments count if visited node equals to stop node
    if (stopNode.equals(node.getName())) {
      count++;
    }

    return VisitStatus.CONTINUE;
  }

  /**
   * Number of trips from start node to stop node that met special criteria
   *
   * @return Number of trips from start node to stop node that met special criteria
   */
  @Override
  public Integer getResult() {
    return count;
  }

  @Override
  public void orderAdjacentNodes(List<GraphNode> nodes, Map<GraphNode, Integer> weightMap) {}
}
