package com.daggeto.canals.controller;


import com.daggeto.canals.domain.GraphNode;
import com.daggeto.canals.visitor.NodeVisitor;

public interface GraphTravelStrategy {

  /**
   * Start travel through graph starting from passed node
   *
   * @param nodeFrom      Node where to start travel
   * @param visitListener Listener that handle node visit
   */
  public void travel(GraphNode nodeFrom, NodeVisitor visitListener);
}
