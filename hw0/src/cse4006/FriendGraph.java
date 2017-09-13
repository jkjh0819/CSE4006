package cse4006;

public class FriendGraph {

    private Boolean[][] graph;
    private String[] names;
    private Boolean[] visited;
    private int num = 0, max = 50;

    public FriendGraph(){
        graph = new Boolean[max][max];
        names = new String[max];
        for(int i = 0; i < max; i++){
            for(int j = 0; j < max; j++){
                graph[i][j] = false;
            }
        }
    }

    public void addPerson(Person p){
        names[num++] = p.getName();
    }

    public int findIndex(String name){
        for(int i = 0; i< num; i++){
            if(names[i].equals(name)){
                return i;
            }
        }
        return -1;
    }

    public void addFriendship(String p1, String p2){
        int p1_idx = findIndex(p1);
        int p2_idx = findIndex(p2);

        if(p1_idx != -1 && p2_idx != -1){
            graph[p1_idx][p2_idx] = true;
            graph[p2_idx][p1_idx] = true;
        } else {
            System.out.println("Someone is not in graph");
        }
    }

    public int getDistance(String p1, String p2){
        int cnt = 1, tmp;
        int p1_idx = findIndex(p1);
        int p2_idx = findIndex(p2);
        Queue q = new Queue(num);

        if (p1_idx == -1 || p2_idx == -1) {
            return -1;
        } else if (p1_idx == p2_idx)
            return 0;
        else {
            visited = new Boolean[num];
            for (int i = 0; i < num; i++) {
                visited[i] = false;
            }
            visited[p1_idx] = true;
            q.enqueue(p1_idx);

            while (!q.IsEmpty()) {
                tmp = q.dequeue();
                for (int i = 0; i < num; i++) {
                    if (graph[tmp][i] && !visited[i]) {
                        if (i == p2_idx) {
                            return cnt;
                        } else {
                            visited[i] = true;
                            q.enqueue(i);
                        }
                    }
                }
                cnt++;
            }
        }
        return -1;
    }
}
