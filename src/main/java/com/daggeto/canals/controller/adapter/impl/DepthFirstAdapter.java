package com.daggeto.canals.controller.adapter.impl;

import com.daggeto.canals.controller.adapter.CollectionAdapter;

import java.util.Stack;

/**
 * Implements adapter for Stack collection
 */
public class DepthFirstAdapter<T> implements CollectionAdapter<T> {

  private Stack<T> stack;

  public DepthFirstAdapter() {
    this.stack = new Stack<>();
  }

  public DepthFirstAdapter(Stack<T> stack) {
    this.stack = stack;
  }

  @Override
  public void add(T t) {
    stack.push(t);
  }

  @Override
  public T takeNext() {
    return stack.pop();
  }

  @Override
  public boolean hasNext() {
    return !stack.isEmpty();
  }

  @Override
  public Class getCollectionClass() {
    return Stack.class;
  }
}
