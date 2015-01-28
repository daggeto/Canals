package com.daggeto;

import com.daggeto.canals.domain.GraphNode;
import junit.framework.TestCase;
//TODO: extend other test with this
public abstract class AbstractGraphTest extends TestCase {

    public void addAdjacentNodes(GraphNode node, Object ... adjacentNodes){
        for( int i = 0; i < adjacentNodes.length; i+=2){
            GraphNode adjNode = (GraphNode) adjacentNodes[i];
            int weight = (Integer) adjacentNodes[i +1];

            node.addAdjacentNode(adjNode, weight);
        }
    }
}
