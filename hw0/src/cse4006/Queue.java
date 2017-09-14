package cse4006;

/**
 * Queue
 * 2017.09.14
 *
 * This class uses for composing queue and using it. It follows circular queue.
 */

public class Queue {

  private Node[] queue;
  private int head, tail;
  private int max;

  /**
   * Queue Constructor
   *
   * if there's no parameter, initialize queue size as max 50
   */
  public Queue(){
    max = 50;
    queue = new Node[max];
    head = 0;
    tail = 0;
  }

  /**
   * Queue Constructor
   *
   * initialize queue size as given number
   */
  public Queue(int number) {
    max = number;
    queue = new Node[max];
    head = 0;
    tail = 0;
  }

  /**
   * enqueue
   *
   * add node to queue. if queue is full, throw exception.
   */
  public void enqueue(Node n) {

    if (isFull()) {
      throw new ArrayIndexOutOfBoundsException();
    }

    tail = (tail + 1) % (max);
    queue[tail] = n;
  }

  /**
   * dequeue
   *
   * return node at head position of queue. If queue is empty, throw exception.
   */
  public Node dequeue() {

    if (isEmpty()) {
      throw new ArrayIndexOutOfBoundsException();
    }

    head = (head + 1) % max;
    return queue[head];
  }

  /**
   * isEmpty
   *
   * test whether queue is empty. If head is same to tail, queue is empty.
   */
  public Boolean isEmpty() {
    return (head == tail);
  }

  /**
   * isFull
   *
   * test whether queue is full. If there's no position for increase tail, queue is full.
   */
  public Boolean isFull() {
    return ((tail + 1) % this.max == head);
  }

}
