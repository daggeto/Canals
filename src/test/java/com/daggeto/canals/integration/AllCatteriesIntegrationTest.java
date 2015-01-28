package com.daggeto.canals.integration;

import com.daggeto.AbstractGraphTest;
import com.daggeto.canals.controller.impl.RecursiveGraphTravelStrategy;
import com.daggeto.canals.domain.GraphNode;
import com.daggeto.canals.visitor.NodeVisitor;
import com.daggeto.canals.visitor.impl.AllPathsVisitor;
import com.google.common.base.Predicate;

/**
 * Created by daggeto on 15.1.27.
 */
public class AllCatteriesIntegrationTest extends AbstractGraphTest{

    private RecursiveGraphTravelStrategy strategy = new RecursiveGraphTravelStrategy();
    private NodeVisitor<Integer> visitor;

    GraphNode nodeA = new GraphNode("A");
    GraphNode nodeB = new GraphNode("B");
    GraphNode nodeC = new GraphNode("C");
    GraphNode nodeD = new GraphNode("D");
    GraphNode nodeE = new GraphNode("E");

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        addAdjacentNodes(nodeA, nodeD, 5, nodeE, 7, nodeB, 5);
        addAdjacentNodes(nodeD, nodeE, 6, nodeC, 8);
        addAdjacentNodes(nodeE, nodeB, 3);
        addAdjacentNodes(nodeB, nodeC, 4);
        addAdjacentNodes(nodeC, nodeD, 8, nodeE, 2);

        visitor = new AllPathsVisitor("C", "C", new Predicate<Integer>() {
            @Override
            public boolean apply(Integer weight) {
                return weight < 30;
            }
        });
    }

    public void testCountOfPathsWithLessThen(){
        strategy.travel(nodeC, visitor);

        Integer result = visitor.getResult();

        assertEquals(Integer.valueOf(7), result);

    }
}
