package cse4006;

public class Node {
    private int p_idx, p_dist;
    public Node(int idx, int dist){
        p_idx = idx;
        p_dist = dist;
    }

    public int getIdx(){
        return p_idx;
    }

    public int getDist(){
        return p_dist;
    }
}
