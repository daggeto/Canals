package com.daggeto.canals.visitor;

import com.daggeto.AbstractGraphTest;
import com.daggeto.canals.domain.GraphNode;
import com.daggeto.canals.visitor.NodeVisitor.VisitStatus;
import com.daggeto.canals.visitor.impl.AllPathsVisitor;
import com.google.common.base.Predicate;

public class AllPathsVisitorTest extends AbstractGraphTest {
    private final int LIMIT_WEIGHT = 5;

    AllPathsVisitor testObject;

    GraphNode nodeA = new GraphNode("A");
    GraphNode nodeB = new GraphNode("B");

    public void testVisit(){

        testObject = new AllPathsVisitor("A", "A", new Predicate<Integer>() {
            @Override
            public boolean apply(Integer weight) {
                return weight <= LIMIT_WEIGHT;
            }
        });

        VisitStatus status = testObject.visit(nodeA, null, 6);
        assertEquals(VisitStatus.STOP, status);

        status = testObject.visit(nodeB, null, 0);
        assertEquals(VisitStatus.CONTINUE, status);

        testObject.visit(nodeA, null, 0);
        testObject.visit(nodeA, null, 0);
        testObject.visit(nodeA, null, 0);

        assertEquals(Integer.valueOf(2), testObject.getResult());

    }

}
