package com.daggeto.canals.controller.adapter.impl;

import com.daggeto.canals.controller.adapter.CollectionAdapter;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Implements adapter for Queue collection
 */
public class BreadthFirstAdapter<T> implements CollectionAdapter<T> {

  private Queue<T> queue;

  public BreadthFirstAdapter() {
    this.queue = new LinkedList<T>();
  }

  public BreadthFirstAdapter(Queue<T> queue) {
    this.queue = queue;
  }

  @Override
  public void add(T t) {
    queue.add(t);
  }

  @Override
  public T takeNext() {
    return queue.remove();
  }

  @Override
  public boolean hasNext() {
    return !queue.isEmpty();
  }

  @Override
  public Class getCollectionClass() {
    return Queue.class;
  }
}
