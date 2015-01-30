package com.daggeto.canals.controller;


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

/**
 * Controls all action with graph. Holds all graph nodes and relations between them
 */
public class GraphController {

  private final String START_NODE_NOT_EXISTS = "Start node %s from %s does not exists";

  private List<GraphNode> nodes;

  public GraphController() {
    nodes = new ArrayList<GraphNode>();
  }

  public void addRoute(String from, String to, int weight) {
    assert from != null;
    assert to != null;

    GraphNode fromNode = findOrAdd(from);
    GraphNode toNode = findOrAdd(to);

    GraphRelationsBuilder.forNode(fromNode).withAdjacent(toNode, weight);
  }

  private GraphNode findOrAdd(String name) {
    GraphNode node = findNodeByName(name);

    if (node == null) {
      node = new GraphNode(name);
      nodes.add(node);
    }

    return node;
  }

  /**
   * Finds length of path
   *
   * @param path Path that need to be found
   * @return Total weight of path
   */
  public Integer findPath(String path) {
    String[] splitPath = StringUtils.split(path, "-");

    Queue<String> targetPath = new LinkedList<String>();
    targetPath.addAll(Arrays.asList(splitPath));

    NodeVisitor<Integer> pathDistanceVisitor = new PathDistanceVisitor(targetPath);

    GraphTravelStrategy strategy = new IterativeGraphTravelStrategy(new BreadthFirstAdapter<GraphNodeDto>());

    GraphNode startNode = findNodeByName(targetPath.peek());
    if (startNode == null) {
      return 0;
    }

    strategy.travel(startNode, pathDistanceVisitor);

    return pathDistanceVisitor.getResult();
  }

  /**
   * Calculates number of trips from starting node to ending node with stops less or equal to passed number
   *
   * @param from  Node from
   * @param to    Node to
   * @param stops Number of stops
   * @return
   */
  public int countTripsWithLessOrEqualStops(String from, String to, final int stops) {
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

    GraphNode startNode = findNodeByName(from);
    if (startNode == null) {
      return 0;
    }

    strategy.travel(findNodeByName(from), visitor);

    return visitor.getResult();
  }

  /**
   * Calculates number of trips from starting node to ending node with stops equal to passed number
   *
   * @param from  Node from
   * @param to    Node to
   * @param stops Number of stops
   * @return
   */
  public int countTripsWithLimitStops(String from, String to, final int stops) {
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

    GraphNode startNode = findNodeByName(from);
    if (startNode == null) {
      return 0;
    }

    strategy.travel(findNodeByName(from), visitor);

    return visitor.getResult();
  }

  /**
   * The length of the shortest route
   *
   * @param from Node from
   * @param to   Node to
   * @return The length of the shortest route
   */
  public Integer findShortestRoute(String from, String to) {
    NodeVisitor<Integer> visitor = new ShortestRouteVisitor(from, to);

    GraphTravelStrategy strategy = new IterativeGraphTravelStrategy(new BreadthFirstAdapter<GraphNodeDto>());

    GraphNode startNode = findNodeByName(from);
    if (startNode == null) {
      return 0;
    }

    strategy.travel(findNodeByName(from), visitor);

    return visitor.getResult();
  }

  /**
   * Number of routes where total distance is less than specified
   *
   * @param from     Node from
   * @param to       Node to
   * @param distance Distance to compare
   * @return Number of routes where total distance is less than specified
   */
  public Integer findNumberOfRoutesWithDistanceLessThan(String from, String to, final int distance) {
    Predicate<Integer> LIMIT = new Predicate<Integer>() {
      @Override
      public boolean apply(Integer weight) {
        return weight < distance;
      }
    };

    NodeVisitor<Integer> visitor = new AllPathsVisitor(from, to, LIMIT);

    GraphTravelStrategy strategy = new RecursiveGraphTravelStrategy();

    GraphNode startNode = findNodeByName(from);
    if (startNode == null) {
      return 0;
    }

    strategy.travel(findNodeByName(from), visitor);

    return visitor.getResult();
  }

  private GraphNode findNodeByName(String name) {
    for (GraphNode n : nodes) {
      if (n.getName().equals(name))
        return n;
    }

    return null;
  }
}
