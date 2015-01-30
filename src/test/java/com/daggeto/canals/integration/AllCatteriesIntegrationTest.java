package com.daggeto.canals.integration;

import com.daggeto.canals.controller.impl.RecursiveGraphTravelStrategy;
import com.daggeto.canals.domain.GraphNode;
import com.daggeto.canals.utils.GraphRelationsBuilder;
import com.daggeto.canals.visitor.NodeVisitor;
import com.daggeto.canals.visitor.impl.AllPathsVisitor;
import com.google.common.base.Predicate;
import junit.framework.TestCase;

public class AllCatteriesIntegrationTest extends TestCase {

  GraphNode nodeA = new GraphNode("A");
  GraphNode nodeB = new GraphNode("B");
  GraphNode nodeC = new GraphNode("C");
  GraphNode nodeD = new GraphNode("D");
  GraphNode nodeE = new GraphNode("E");
  private RecursiveGraphTravelStrategy strategy = new RecursiveGraphTravelStrategy();
  private NodeVisitor<Integer> visitor;

  @Override
  protected void setUp() throws Exception {
    super.setUp();

    GraphRelationsBuilder.forNode(nodeA).
      withAdjacent(nodeD, 5).
      withAdjacent(nodeE, 7).
      withAdjacent(nodeB, 5);

    GraphRelationsBuilder.forNode(nodeD).
      withAdjacent(nodeE, 6).
      withAdjacent(nodeC, 8);

    GraphRelationsBuilder.forNode(nodeE).
      withAdjacent(nodeB, 3);

    GraphRelationsBuilder.forNode(nodeB).
      withAdjacent(nodeC, 4);

    GraphRelationsBuilder.forNode(nodeC).
      withAdjacent(nodeD, 8).
      withAdjacent(nodeE, 2);

    visitor = new AllPathsVisitor(
      "C", "C", new Predicate<Integer>() {
      @Override
      public boolean apply(Integer weight) {
        return weight < 30;
      }
    }
    );
  }

  public void testCountOfPathsWithLessThen() {
    strategy.travel(nodeC, visitor);

    Integer result = visitor.getResult();

    assertEquals(Integer.valueOf(7), result);

  }
}
