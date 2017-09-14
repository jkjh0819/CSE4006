package cse4006;

/**
 * Node
 * 2017.09.14
 *
 * This class is used for representing Node in graph and composing queue.
 */

public class Node {

  private int idx;
  private int dist;

  /**
   * Node Constructor
   *
   * initiates node index and distance from parent.
   */
  public Node(int idx, int dist) {
    this.idx = idx;
    this.dist = dist;
  }

  /**
   * getIdx
   *
   * return index of node.
   */
  public int getIdx() {
    return idx;
  }

  /**
   * getDist
   *
   * return distance of node.
   */
  public int getDist() {
    return dist;
  }
}
