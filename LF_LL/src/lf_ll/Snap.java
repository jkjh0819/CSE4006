package lf_ll;

//this class is for container that include predecessor node and current node
//It means that predecessor and current node are targets of operation
public class Snap {
    public Node pred, curr;

    public Snap(Node pred_, Node curr_) {
        pred = pred_;
        curr = curr_;
    }
}


