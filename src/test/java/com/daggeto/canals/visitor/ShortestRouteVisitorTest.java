package com.daggeto.canals.visitor;

import com.daggeto.AbstractGraphTest;
import com.daggeto.canals.domain.GraphNode;
import com.daggeto.canals.utils.GraphRelationsBuilder;
import com.daggeto.canals.visitor.impl.ShortestRouteVisitor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.daggeto.canals.visitor.NodeVisitor.VisitStatus;

public class ShortestRouteVisitorTest extends AbstractGraphTest {

    NodeVisitor<Integer> testObject;

    GraphNode nodeA = new GraphNode("A");
    GraphNode nodeB = new GraphNode("B");
    GraphNode nodeC = new GraphNode("C");
    GraphNode nodeD = new GraphNode("D");

    public void testRoutBetweenDifferentNodes(){
        addAdjacentNodes(nodeA, nodeB, 3, nodeC, 2, nodeD, 5);
        addAdjacentNodes(nodeB, nodeD, 3);
        addAdjacentNodes(nodeC, nodeD, 2);

        testObject = new ShortestRouteVisitor("A", "D");

        VisitStatus status = testObject.visit(nodeB, null, 0, false);
        assertEquals(VisitStatus.CONTINUE, status);

        status = testObject.visit(nodeA, null, 0, false);
        assertEquals(VisitStatus.CONTINUE, status);

        status = testObject.visit(nodeB, nodeA, 0, false);
        assertEquals(VisitStatus.CONTINUE, status);

        status = testObject.visit(nodeD, nodeA, 0, false);
        assertEquals(VisitStatus.CONTINUE, status);

        status = testObject.visit(nodeC, nodeA, 0, false);
        assertEquals(VisitStatus.CONTINUE, status);

        status = testObject.visit(nodeD, nodeB, 0, true);
        assertEquals(VisitStatus.SKIP, status);

        status = testObject.visit(nodeD, nodeC, 0, true);
        assertEquals(VisitStatus.SKIP, status);

        Integer result = testObject.getResult();
        assertEquals(Integer.valueOf(4), result);
    }

    public void testRouteBetweenSameNodes(){
        GraphRelationsBuilder.forNode(nodeA)
                .withAdjacent(nodeC, 2)
                .withAdjacent(nodeD, 5);

        GraphRelationsBuilder.forNode(nodeB)
                .withAdjacent(nodeA, 2);

        GraphRelationsBuilder.forNode(nodeC)
                .withAdjacent(nodeD, 2);

        GraphRelationsBuilder.forNode(nodeD)
                .withAdjacent(nodeB, 3);

        testObject = new ShortestRouteVisitor("A", "A");

        testObject.visit(nodeA, null, 0, false);
        testObject.visit(nodeC, nodeA, 0, false);
        testObject.visit(nodeD, nodeC, 0, false);
        testObject.visit(nodeB, nodeD, 0, false);
        testObject.visit(nodeA, nodeB, 0, true);
        testObject.visit(nodeD, nodeA, 0, true);

        Integer result = testObject.getResult();

        assertEquals(Integer.valueOf(9), result);
    }

    public void testOrderBy(){
        testObject = new ShortestRouteVisitor("A", "A");

        GraphRelationsBuilder.forNode(nodeA)
                .withAdjacent(nodeB, 5)
                .withAdjacent(nodeC,1)
                .withAdjacent(nodeD, 3);

        Set<GraphNode> adjacentNodes = nodeA.getAdjacentNodes().keySet();
        List<GraphNode> nodeList = new ArrayList<>(adjacentNodes);

        testObject.orderAdjacentNodes(nodeList, nodeA.getAdjacentNodes());
    }
}
