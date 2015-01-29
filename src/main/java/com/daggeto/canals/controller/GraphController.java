package com.daggeto.canals.controller;


import com.daggeto.canals.controller.adapter.CollectionAdapter;
import com.daggeto.canals.controller.adapter.impl.BreadthFirstAdapter;
import com.daggeto.canals.controller.adapter.impl.DepthFirstAdapter;
import com.daggeto.canals.controller.dto.GraphNodeDto;
import com.daggeto.canals.controller.impl.IterativeGraphTravelStrategy;
import com.daggeto.canals.controller.impl.RecursiveGraphTravelStrategy;
import com.daggeto.canals.domain.GraphNode;
import com.daggeto.canals.utils.GraphRelationsBuilder;
import com.daggeto.canals.visitor.NodeVisitor;
import com.daggeto.canals.visitor.impl.AllPathsVisitor;
import com.daggeto.canals.visitor.impl.PathDistanceVisitor;
import com.daggeto.canals.visitor.impl.ShortestRouteVisitor;
import com.daggeto.canals.visitor.impl.TripCountVisitor;
import com.google.common.base.Predicate;
import org.apache.commons.lang.StringUtils;

import java.util.*;
//TODO: implement all calculations

/**
 * Controls all action with graph. Holds all graph nodes and relations between them
 */
public class GraphController {

    private List<GraphNode> nodes;

    public GraphController(){
        nodes = new ArrayList<GraphNode>();
    }

    public void addRoute(String from, String to, int weight){
        assert from != null;
        assert to != null;

        GraphNode fromNode = findOrAdd(from);
        GraphNode toNode = findOrAdd(to);

        GraphRelationsBuilder.forNode(fromNode).withAdjacent(toNode, weight);
    }

    private GraphNode findOrAdd(String name){
        GraphNode node = findNodeByName(name);
        if(node == null){
            node = new GraphNode(name);
            nodes.add(node);
        }

        return node;
    }

    /**
     * Finds length of path
     * @param path Path that need to be found
     * @return Total weight of path
     */
    public Integer findPath(String path){

        String[] splitPath = StringUtils.split(path,"-");

        Queue<String> targetPath = new LinkedList<String>();
        targetPath.addAll(Arrays.asList(splitPath));

        NodeVisitor<Integer> pathDistanceVisitor = new PathDistanceVisitor(targetPath);

        GraphTravelStrategy strategy = new IterativeGraphTravelStrategy(new BreadthFirstAdapter<GraphNodeDto>());

        GraphNode startNode = findNodeByName(targetPath.peek());

        if(startNode == null){
            throw new IllegalArgumentException("Start node " + targetPath.peek() + " from " + path + " does not exists");
        }

        strategy.travel(startNode, pathDistanceVisitor);

        return pathDistanceVisitor.getResult();
    }

    /**
     * Calculates number of trips from starting node to ending node with stops less or equal to passed number
     * @param from Node from
     * @param to Node to
     * @param stops Number of stops
     * @return
     */
    public int countTripsWithLessOrEqualStops(String from, String to, final int stops){

        Predicate<Integer> TARGET = new Predicate<Integer>() {
            @Override
            public boolean apply(Integer steps) {
                return steps <= stops;
            }
        };

        Predicate<Integer> LIMIT = new Predicate<Integer>() {
            @Override
            public boolean apply(Integer steps) {
                return steps > stops;
            }
        };

        NodeVisitor<Integer> visitor = new TripCountVisitor(from, to, TARGET, LIMIT);

        GraphTravelStrategy strategy = new IterativeGraphTravelStrategy(new DepthFirstAdapter<GraphNodeDto>());

        strategy.travel(findNodeByName(from), visitor);

        return visitor.getResult();
    }

    /**
     * Calculates number of trips from starting node to ending node with stops equal to passed number
     * @param from Node from
     * @param to Node to
     * @param stops Number of stops
     * @return
     */
    public int countTripsWithLimitStops(String from, String to, final int stops){

        Predicate<Integer> TARGET = new Predicate<Integer>() {
            @Override
            public boolean apply(Integer step) {
                return step == stops;
            }
        };

        Predicate<Integer> LIMIT = new Predicate<Integer>() {
            @Override
            public boolean apply(Integer step) {
                return step > stops;
            }
        };

        NodeVisitor<Integer> visitor = new TripCountVisitor(from, to, TARGET, LIMIT);

        GraphTravelStrategy strategy = new IterativeGraphTravelStrategy(new DepthFirstAdapter<GraphNodeDto>());

        strategy.travel(findNodeByName(from), visitor);

        return visitor.getResult();
    }

    /**
     * The length of the shortest route
     * @param from Node from
     * @param to Node to
     * @return The length of the shortest route
     */
    public Integer findShortestRoute(String from, String to){

        NodeVisitor<Integer> visitor = new ShortestRouteVisitor(from, to);
        GraphTravelStrategy strategy = new IterativeGraphTravelStrategy(new BreadthFirstAdapter<GraphNodeDto>());

        strategy.travel(findNodeByName(from), visitor);

        return visitor.getResult();
    }

    public Integer findNumberOfRoutesWithDistanceLessThan(String from, String to, final int distance){
        Predicate<Integer> LIMIT = new Predicate<Integer>() {
            @Override
            public boolean apply(Integer weight) {
                return weight < distance;
            }
        };

        NodeVisitor<Integer> visitor = new AllPathsVisitor(from, to, LIMIT);
        GraphTravelStrategy strategy = new RecursiveGraphTravelStrategy();

        strategy.travel(findNodeByName(from), visitor);

        return visitor.getResult();
    }

    private List<String> parsePathToList(String path){
        String[] splitPath = StringUtils.split(path);

        return Arrays.asList(splitPath);
    }

    private GraphNode findNodeByName(String name){
        for(GraphNode n : nodes){
            if(n.getName().equals(name))
                return n;
        }

        return null;
    }

//    @Override
//    public Category addCategory(String parentId, String name) {
//        if(name == null){
//            throw new IllegalArgumentException("Category name cannot be null");
//        }
//
//        if(parentId == null && root != null){
//            throw new IllegalArgumentException("Root already exists");
//        }
//
//        String id = name.toLowerCase();
//
//        if(nodes.containsKey(id)){
//            throw new IllegalArgumentException("Node \"" + id + "\" already exists");
//        }
//
//        Category category = new Category(id, name);
//        TreeNode node = new TreeNode(category);
//        nodes.put(id, node);
//
//        if(parentId == null){
//            node.setLevel(0);
//            root = node;
//            return category;
//        }
//
//        TreeNode parent = nodes.get(parentId.toLowerCase());
//
//        if(parent == null){
//            throw new IllegalArgumentException("There is no node with id \"" + parentId + "\"");
//        }
//
//        addChild(parent, node);
//
//        return category;
//    }

//    private void addChild(TreeNode parent, TreeNode child){
//        child.setLevel(parent.getLevel() + 1);
//
//        if(parent.getLeftChild() == null){
//            parent.addLeftChild(child);
//            return;
//        }
//
//        if(parent.getRightChild() == null) {
//            parent.addRightChild(child);
//            return;
//        }
//
//        throw new IllegalArgumentException("No more free nodes for: " + child.getCategory().getName());
//    }

//    @Override
//    public List<String> listCategories(Algorithm algorithm) {
//        resolveTreeTravelStrategy(algorithm);
//
//        return graphTravelStrategy.getListTree(root);
//    }
//
//    private void resolveTreeTravelStrategy(Algorithm algorithm) {
//        if(Algorithm.Iterative.equals(algorithm)){
//            graphTravelStrategy = new GraphBreadthTravelStrategy();
//            return;
//        }
//
//        graphTravelStrategy = new RecursiveGraphTravelStrategy();
//    }
}
