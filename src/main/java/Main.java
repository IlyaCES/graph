import graph.Edge;
import graph.Graph;
import java.util.ArrayList;
import java.util.List;

public class Main {

  public static void main(String[] args) {
    List<Edge> edges = new ArrayList<>();
    edges.add(new Edge(1, 2, (float) 2.53));
    edges.add(new Edge(4, 2, (float) 10.00));
    edges.add(new Edge(2, 1, (float) 5.64));
    edges.add(new Edge(1, 3, (float) 4.23));
    edges.add(new Edge(4, 1, (float) 4.44));
    edges.add(new Edge(5, 3, (float) 90.44));
    edges.add(new Edge(4, 5, (float) 88.34));
    edges.add(new Edge(5, 2, (float) 33.21));
    edges.add(new Edge(3, 5, (float) 13.98));
    edges.add(new Edge(3, 2, (float) 56.33));

    Graph graph = new Graph.Builder(10)
        .addEdges(edges)
        .build();
    System.out.println("In list:");
    for (Edge edge : edges) {
      System.out.println(edge);
    }

    System.out.println("In graph:");
    for (Edge edge : graph) {
      System.out.println(edge);
    }

    System.out.println("(2, 1) value = " + graph.getEdgeValue(2, 1));
  }
}
