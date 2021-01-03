package graph;

import java.util.Objects;

public class Edge {

  private final int vertex1;
  private final int vertex2;
  private final float value;

  public Edge(int vertex1, int vertex2, float value) {
    this.vertex1 = vertex1;
    this.vertex2 = vertex2;
    this.value = value;
  }

  public int getVertex2() {
    return vertex2;
  }

  public int getVertex1() {
    return vertex1;
  }

  public float getValue() {
    return value;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Edge edge = (Edge) o;
    return getVertex1() == edge.getVertex1() &&
        getVertex2() == edge.getVertex2() &&
        Float.compare(edge.getValue(), getValue()) == 0;
  }

  @Override
  public int hashCode() {
    return Objects.hash(getVertex1(), getVertex2(), getValue());
  }

  @Override
  public String toString() {
    return "graph.Edge{" +
        "vertex1=" + vertex1 +
        ", vertex2=" + vertex2 +
        ", value=" + value +
        '}';
  }
}
