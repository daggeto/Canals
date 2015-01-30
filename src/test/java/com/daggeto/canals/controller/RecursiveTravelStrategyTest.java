package com.daggeto.canals.controller;

import com.daggeto.AbstractGraphTest;
import com.daggeto.canals.controller.impl.RecursiveGraphTravelStrategy;
import com.daggeto.canals.domain.GraphNode;
import com.daggeto.canals.utils.GraphRelationsBuilder;
import com.daggeto.canals.visitor.NodeVisitor;

import java.util.List;
import java.util.Map;

/**
 * Created by daggeto on 15.1.27.
 */
public class RecursiveTravelStrategyTest extends AbstractGraphTest {

  GraphTravelStrategy testObject;

  GraphNode nodeA = new GraphNode("A");
  GraphNode nodeB = new GraphNode("B");
  GraphNode nodeE = new GraphNode("E");
  GraphNode nodeD = new GraphNode("D");

  public void testTravelTotalWeight() {
    GraphRelationsBuilder.forNode(nodeA).
      withAdjacent(nodeE, 7).
      withAdjacent(nodeD, 5);
    GraphRelationsBuilder.forNode(nodeE).
      withAdjacent(nodeB, 3);

    NodeVisitor<Integer> visitor = new NodeVisitor<Integer>() {

      Integer resultWeight = 0;

      @Override
      public VisitStatus visit(GraphNode node, GraphNode parent, Integer depth, boolean visited) {
        return null;
      }

      @Override
      public VisitStatus visit(GraphNode node, GraphNode parent, Integer weight) {

        if (node.equals(nodeD)) {
          return VisitStatus.SKIP;
        }

        if (node.equals(nodeB)) {
          resultWeight = weight;
          return VisitStatus.STOP;
        }

        return VisitStatus.CONTINUE;
      }

      @Override
      public Integer getResult() {
        return resultWeight;
      }

      @Override
      public void orderAdjacentNodes(List<GraphNode> nodes, Map<GraphNode, Integer> weightMap) {

      }
    };

    testObject = new RecursiveGraphTravelStrategy();

    testObject.travel(nodeA, visitor);

    assertEquals(Integer.valueOf(10), visitor.getResult());
  }
}
