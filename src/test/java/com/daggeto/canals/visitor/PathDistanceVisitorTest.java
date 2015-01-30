package com.daggeto.canals.visitor;

import com.daggeto.canals.domain.GraphNode;
import com.daggeto.canals.utils.GraphRelationsBuilder;
import com.daggeto.canals.visitor.impl.PathDistanceVisitor;
import junit.framework.TestCase;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

import static com.daggeto.canals.visitor.NodeVisitor.VisitStatus;

public class PathDistanceVisitorTest extends TestCase {

  PathDistanceVisitor testObject;

  @Override
  protected void setUp() throws Exception {
    super.setUp();
  }


  public void testPath() {
    GraphNode nodeA = new GraphNode("A");
    GraphNode nodeB = new GraphNode("B");
    GraphNode nodeC = new GraphNode("C");

    GraphRelationsBuilder.forNode(nodeA).withAdjacent(nodeB, 2);
    GraphRelationsBuilder.forNode(nodeB).withAdjacent(nodeC, 3);

    Queue<String> pathStack = new LinkedList<>();
    pathStack.addAll(Arrays.asList("A", "B", "C"));

    testObject = new PathDistanceVisitor(pathStack);

    VisitStatus result = testObject.visit(nodeB, null, 0, false);
    assertEquals(VisitStatus.SKIP, result);

    result = testObject.visit(nodeA, null, 0, false);
    assertEquals(VisitStatus.CONTINUE, result);

    result = testObject.visit(nodeB, nodeA, 1, false);
    assertEquals(VisitStatus.CONTINUE, result);

    result = testObject.visit(nodeC, nodeB, 2, false);
    assertEquals(VisitStatus.CONTINUE, result);

    result = testObject.visit(nodeC, null, 0, false);
    assertEquals(VisitStatus.STOP, result);

    Integer expectedDistance = 5;
    assertEquals(expectedDistance, testObject.getResult());
  }

  public void testNonExistingPath() {
    GraphNode nodeA = new GraphNode("A");
    GraphNode nodeB = new GraphNode("B");
    GraphNode nodeC = new GraphNode("C");

    nodeA.addAdjacentNode(nodeB, 10);

    nodeB.addAdjacentNode(nodeC, 3);

    Queue<String> pathStack = new LinkedList<>();
    pathStack.addAll(Arrays.asList("A", "B", "E"));

    testObject = new PathDistanceVisitor(pathStack);

    VisitStatus result = testObject.visit(nodeA, null, 0, false);
    assertEquals(VisitStatus.CONTINUE, result);

    result = testObject.visit(nodeB, nodeA, 1, false);
    assertEquals(VisitStatus.CONTINUE, result);

    result = testObject.visit(nodeC, null, 2, false);
    assertEquals(VisitStatus.SKIP, result);

    assertEquals(new Integer(0), testObject.getResult());
  }

}
