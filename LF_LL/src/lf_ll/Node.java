package lf_ll;

import java.util.concurrent.atomic.AtomicMarkableReference;

public class Node {

    int data;
    AtomicMarkableReference<Node> next;

    public Node(int data){
        this.data = data;
    }
}
