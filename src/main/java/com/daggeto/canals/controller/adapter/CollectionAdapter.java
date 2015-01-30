package com.daggeto.canals.controller.adapter;

/**
 * Adapter for getting elements from collection in custom order.
 * Something like Iterator, however has method to add elements.
 * Used in travel strategies to implement DFS or BFS algorithms functionality.
 *
 * @param <T> Type of elements in collection
 */
public interface CollectionAdapter<T> {

  /**
   * Add new element to collection
   *
   * @param t Element that will be added to collection
   */
  public void add(T t);

  /**
   * Get next element from collection and removes it
   *
   * @return Next element from collection
   */
  public T takeNext();

  /**
   * Check if collection has next element to take
   *
   * @return True if collection has next element to take, false otherwise
   */
  public boolean hasNext();

  /**
   * Class of used collection
   *
   * @return Class of used collection
   */
  public Class getCollectionClass();

}