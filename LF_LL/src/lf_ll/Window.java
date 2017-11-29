package lf_ll;

//this class is for container that include predecessor node and current node
//It means that predecessor and current node are targets of operation
public class Window {
    public Node pred, curr;

    public Window(Node pred_, Node curr_) {
        pred = pred_;
        curr = curr_;
    }
}


