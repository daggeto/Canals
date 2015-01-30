package com.daggeto.canals.visitor;

import com.daggeto.canals.domain.GraphNode;

import java.util.List;
import java.util.Map;

/**
 * Visits nodes visited by {@link GraphTravelStrategy}
 * and after all provide result of defined type
 */
public interface NodeVisitor<R> {

  public enum VisitStatus {

    /**
     * Continue visiting other graph nodes
     */
    CONTINUE,

    /**
     * Skip of visiting adjacent nodes
     */
    SKIP,

    /**
     * Stop travel through graph
     */
    STOP
  }

  /**
   * Executes calculations and checks for node.
   *
   * @param node  Graph node that is visited
   * @param depth Depth of visited node starting from node first node
   * @return VisitStatus
   */
  public VisitStatus visit(GraphNode node, GraphNode parent, Integer depth, boolean visited);

  /**
   * Executes calculations and checks for node.
   *
   * @param node   Graph node that is visited
   * @param parent Parent node
   * @param weight Weight to this node. It can be total weight or just weight from parent
   * @return VisitStatus
   */
  public VisitStatus visit(GraphNode node, GraphNode parent, Integer weight);

  /**
   * @return Return result of calculations
   */
  public R getResult();

  /**
   * Order list of adjacent nodes in sequence they must be visited
   *
   * @param nodes     List that need to be ordered
   * @param weightMap Map that holds weights of edge from parent to nodes
   */
  public void orderAdjacentNodes(List<GraphNode> nodes, Map<GraphNode, Integer> weightMap);
}