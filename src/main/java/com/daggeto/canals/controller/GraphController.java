package com.daggeto.canals.controller;


import com.daggeto.canals.controller.adapter.impl.BreadthFirstAdapter;
import com.daggeto.canals.controller.dto.GraphNodeDto;
import com.daggeto.canals.controller.impl.IterativeGraphTravelStrategy;
import com.daggeto.canals.controller.impl.RecursiveGraphTravelStrategy;
import com.daggeto.canals.domain.GraphNode;
import com.daggeto.canals.utils.GraphRelationsBuilder;
import com.daggeto.canals.visitor.impl.PathDistanceVisitor;
import org.apache.commons.lang.StringUtils;

import java.util.*;
//TODO: implement all calculations

/**
 * Controls all action with graph. Holds all graph nodes and relations between them
 */
public class GraphController {

    private List<GraphNode> nodes;

    IterativeGraphTravelStrategy iterativeStrategy;
    RecursiveGraphTravelStrategy recursiveStrategy;

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

    public Integer findPath(String path){

        String[] splitPath = StringUtils.split(path);

        Queue<String> targetPath = new LinkedList<String>();
        targetPath.addAll(Arrays.asList(splitPath));

        PathDistanceVisitor pathDistanceVisitor = new PathDistanceVisitor(targetPath);

        iterativeStrategy = new IterativeGraphTravelStrategy(new BreadthFirstAdapter<GraphNodeDto>());

        GraphNode startNode = findNodeByName(targetPath.peek());

        if(startNode == null){
            throw new IllegalArgumentException("Start node " + targetPath.peek() + " from " + path + " does not exists");
        }

        iterativeStrategy.travel(startNode, pathDistanceVisitor);

        return pathDistanceVisitor.getResult();
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
