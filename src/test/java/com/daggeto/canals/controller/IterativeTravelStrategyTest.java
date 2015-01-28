package com.daggeto.canals.controller;

import com.daggeto.canals.controller.adapter.impl.BreadthFirstAdapter;
import com.daggeto.canals.controller.adapter.impl.DepthFirstAdapter;
import com.daggeto.canals.controller.dto.GraphNodeDto;
import com.daggeto.canals.controller.impl.IterativeGraphTravelStrategy;
import com.daggeto.canals.domain.GraphNode;
import com.daggeto.canals.visitor.NodeVisitor;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class IterativeTravelStrategyTest extends TestCase {

    GraphTravelStrategy testObject;

    GraphNode nodeA = new GraphNode("A");
    GraphNode nodeB = new GraphNode("B");
    GraphNode nodeE = new GraphNode("E");
    GraphNode nodeD = new GraphNode("D");

    @Override
    protected void setUp() throws Exception {
        super.setUp();

    }

    public void testTravelPath(){
        nodeA.addAdjacentNode(nodeB, 5);
        nodeA.addAdjacentNode(nodeE, 7);
        nodeA.addAdjacentNode(nodeD, 5);

        nodeE.addAdjacentNode(nodeB, 3);

        nodeD.addAdjacentNode(nodeE, 6);

        nodeB.addAdjacentNode(nodeA, 5);

        NodeVisitor<List<String>> visitor = new NodeVisitor<List<String>>() {

            List<String> result = new ArrayList<>();

            @Override
            public VisitStatus visit(GraphNode node, GraphNode parent, Integer depth, boolean visited) {
                String nodeName = node.getName();

                if (visited) {
                    nodeName = "!" + nodeName;
                }
                result.add(nodeName);

                return doContinue(visited);

            }

            @Override
            public VisitStatus visit(GraphNode node, GraphNode parent, Integer weight) {
                return null;
            }

            @Override
            public List<String> getResult() {
                return result;
            }

            @Override
            public void orderAdjacentNodes(List<GraphNode> nodes, Map<GraphNode, Integer> weightMap) {

            }
        };

        testObject = new IterativeGraphTravelStrategy(new BreadthFirstAdapter<GraphNodeDto>());
        testObject.travel(nodeA, visitor);
        List<String> result = visitor.getResult();
        assertEquals("ABED!A!B!E", concatList(result));



        testObject = new IterativeGraphTravelStrategy(new DepthFirstAdapter<GraphNodeDto>());
        result.clear();
        testObject.travel(nodeA, visitor);
        result = visitor.getResult();
        assertEquals("ADEB!A!E!B", concatList(result));
    }

    public void testTravelDepth(){
        nodeA.addAdjacentNode(nodeE, 7);

        nodeE.addAdjacentNode(nodeB, 3);

        NodeVisitor visitor = new NodeVisitor<Object>() {
            @Override
            public VisitStatus visit(GraphNode node, GraphNode parent, Integer depth, boolean visited) {
                if (nodeB.equals(node)) {
                    assertEquals(new Integer(2), depth);
                }

                return doContinue(visited);
            }

            @Override
            public VisitStatus visit(GraphNode node, GraphNode parent, Integer weight) {
                return null;
            }

            @Override
            public Object getResult() {
                return null;
            }

            @Override
            public void orderAdjacentNodes(List<GraphNode> nodes, Map<GraphNode, Integer> weightMap) {

            }
        };
        testObject = new IterativeGraphTravelStrategy(new BreadthFirstAdapter<GraphNodeDto>());
        testObject.travel(nodeA, visitor);

        testObject = new IterativeGraphTravelStrategy(new DepthFirstAdapter<GraphNodeDto>());
        testObject.travel(nodeA, visitor);
    }

    public void testBreadthTravelWeight(){
        nodeA.addAdjacentNode(nodeE, 7);

        nodeE.addAdjacentNode(nodeB, 3);

        NodeVisitor listener = new NodeVisitor<Object>() {
            boolean firstNodeVisited = false;
            boolean secondNodeVisited = false;
            Integer tripWeight = 0;

            @Override
            public VisitStatus visit(GraphNode node, GraphNode parent, Integer depth, boolean visited) {

                if( !firstNodeVisited ) {
                    firstNodeVisited = nodeA.equals(node);
                }

                if( firstNodeVisited ){
                    tripWeight+=getWeight(node, parent);
                }

                if( !secondNodeVisited ) {
                    secondNodeVisited = nodeB.equals(node);
                }

                if( secondNodeVisited ){
                    assertEquals(new Integer(10), tripWeight);
                }

                return doContinue(visited);
            }

            @Override
            public VisitStatus visit(GraphNode node, GraphNode parent, Integer weight) {
                return null;
            }

            @Override
            public Object getResult() {
                return null;
            }

            @Override
            public void orderAdjacentNodes(List<GraphNode> nodes, Map<GraphNode, Integer> weightMap) {

            }

        };

        testObject = new IterativeGraphTravelStrategy(new BreadthFirstAdapter<GraphNodeDto>());
        testObject.travel( nodeA, listener );

    }


    private String concatList(List<String> list){
        String result = "";
        for(String s : list){
            result+=s;
        }

        return result;
    }

    private Integer getWeight(GraphNode node, GraphNode parent){
        if(parent == null){
            return 0;
        }

        return parent.getAdjacentNodes().get(node);
    }

    private NodeVisitor.VisitStatus doContinue(boolean visited){
        if(visited){
            return NodeVisitor.VisitStatus.SKIP;
        }

        return NodeVisitor.VisitStatus.CONTINUE;
    }

}
