package com.daggeto.canals.visitor;

import com.daggeto.canals.domain.GraphNode;
import com.daggeto.canals.visitor.NodeVisitor.VisitStatus;
import com.daggeto.canals.visitor.impl.TripCountVisitor;
import com.google.common.base.Predicate;
import junit.framework.TestCase;

public class TripCountVisitorTest extends TestCase {

  private final int LIMIT_STEPS = 3;

  TripCountVisitor testObject;

  @Override
  protected void setUp() throws Exception {
    super.setUp();
  }

  public void testVisit() {
    GraphNode nodeA = new GraphNode("A");
    GraphNode nodeC = new GraphNode("C");

    Predicate<Integer> TARGET = new Predicate<Integer>() {
      @Override
      public boolean apply(Integer steps) {
        return steps <= LIMIT_STEPS;
      }
    };

    Predicate<Integer> LIMIT = new Predicate<Integer>() {
      @Override
      public boolean apply(Integer steps) {
        return steps > LIMIT_STEPS;
      }
    };

    testObject = new TripCountVisitor("C", "C", TARGET, LIMIT);

    VisitStatus status = testObject.visit(nodeA, null, 0, false);
    assertEquals(VisitStatus.CONTINUE, status);

    status = testObject.visit(nodeC, null, 1, false);
    assertEquals(VisitStatus.CONTINUE, status);

    status = testObject.visit(nodeC, null, 2, false);
    assertEquals(VisitStatus.CONTINUE, status);

    status = testObject.visit(nodeC, null, LIMIT_STEPS + 2, false);
    assertEquals(VisitStatus.SKIP, status);

    status = testObject.visit(nodeA, null, LIMIT_STEPS + 2, false);
    assertEquals(VisitStatus.SKIP, status);

    status = testObject.visit(nodeA, null, 1, false);
    assertEquals(VisitStatus.STOP, status);

    int resultStops = testObject.getResult();
    assertEquals(1, resultStops);
  }
}
