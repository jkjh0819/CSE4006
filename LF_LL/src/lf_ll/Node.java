package lf_ll;

import java.util.concurrent.atomic.AtomicMarkableReference;

public class Node {

    int data;
    AtomicMarkableReference<Node> next;

    public Node(){
        this.data = 0;
    }

    public Node(int data){
        this.data = data;
    }
}
