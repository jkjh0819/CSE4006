package cse4006;

public class Queue {
    private int[] queue;
    private int tail;

    public Queue(int number){
        queue = new int[number];
        tail = 0;
    }

    public void enqueue(int value){
        queue[tail++] = value;
    }

    public int dequeue(){
        int tmp = queue[0];
        for (int i = 1; i < tail; i++) {
            queue[i - 1] = queue[i];
        }
        tail--;
        return tmp;
    }

    public Boolean IsEmpty() {
        return tail == 0;
    }
}
