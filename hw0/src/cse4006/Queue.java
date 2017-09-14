package cse4006;

public class Queue {
    private Node[] queue;
    private int head, tail;
    private int max;

    public Queue(int number){
        max = number;
        queue = new Node[max];
        head = 0;
        tail = 0;
    }

    public void enqueue(Node n){

        if(isFull()) throw  new ArrayIndexOutOfBoundsException();

        tail = (tail+1)%(max);
        queue[tail] = n;
    }

    public Node dequeue(){

        if(isEmpty()) throw new ArrayIndexOutOfBoundsException();

        head = (head+1)%max;
        return queue[head];
    }

    public Boolean isEmpty() {
        return (head == tail);
    }

    public Boolean isFull(){
        return ((tail+1)%this.max==head);
    }

}
