package lf_ll;

import java.util.concurrent.atomic.AtomicMarkableReference;

//this class is for a Node of linked list
//It includes data and next reference.
//Next reference is AtomicMarkableReference that contains atomic operation and marked(logical deletion) bit
public class Node {

    int data;
    AtomicMarkableReference<Node> next;

    public Node(int data){
        this.data = data;
    }
}
