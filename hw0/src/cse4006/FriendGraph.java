package cse4006;

/**
 *  FriendGraph
 *  2017.09.14
 *
 *  This class provides connection between people, finding distance
 */
public class FriendGraph {

  private Boolean[][] graph;
  private String[] names;
  private Boolean[] visited;
  private int num = 0;
  private int max = 50;

  /**
   * FriendGraph constructor
   *
   * initiates graph, names array
   */

  public FriendGraph() {
    graph = new Boolean[max][max];
    names = new String[max];
    for (int i = 0; i < max; i++) {
      for (int j = 0; j < max; j++) {
        graph[i][j] = false;
      }
    }
  }

  /**
   * addPerson
   *
   * if person's name is not in names array, add it and increase num
   * num represent the number of registered person.
   */
  public void addPerson(Person p) {
    for(int i = 0; i < num; i++){
      if(names[i].equals(p.getName())){
        System.out.println("already have same person");
        return;
      }
    }
    names[num++] = p.getName();
  }

  /**
   * findIndex
   *
   * search given name in names and return index.
   */
  public int findIndex(String name) {
    for (int i = 0; i < num; i++) {
      if (names[i].equals(name)) {
        return i;
      }
    }
    return -1;
  }

  /**
   * addFriendShip
   *
   * change boolean value of graph to true. It means that there's a relation.
   */
  public void addFriendship(String p1, String p2) {
    int p1_idx = findIndex(p1);
    int p2_idx = findIndex(p2);

    if (p1_idx != -1 && p2_idx != -1) { //if two people is registered
      graph[p1_idx][p2_idx] = true;
      graph[p2_idx][p1_idx] = true;
    } else { // someone is not registered.
      System.out.println("Someone is not in graph");
    }
  }

  /**
   * getDistance
   *
   * calculate the shortest distance between two person. It uses BFS.
   */
  public int getDistance(String p1, String p2) {
    int cnt = 0;
    int p1_idx = findIndex(p1);
    int p2_idx = findIndex(p2);
    Queue q = new Queue(num + 1);
    Node n = new Node(p1_idx, cnt);

    if (p1_idx == -1 || p2_idx == -1) { // Someone is not registered.
      System.out.println("Someone is not in graph");
      return -1;
    } else if (p1_idx == p2_idx) { // self loop
      return 0;
    } else {
      visited = new Boolean[num];
      for (int i = 0; i < num; i++) {
        visited[i] = false;
      }
      visited[p1_idx] = true;
      q.enqueue(n);

      while (!q.isEmpty()) {
        n = q.dequeue();
        for (int i = 0; i < num; i++) {
          if (graph[n.getIdx()][i] && !visited[i]) { // if node has a relation and is not visited
            if (i == p2_idx) { // if the node is goal(p2),
              return n.getDist() + 1;
            } else {
              visited[i] = true;
              q.enqueue(new Node(i, cnt + 1));
            }
          }
        }
        cnt++;
      }
    }
    return -1;
  }
}
