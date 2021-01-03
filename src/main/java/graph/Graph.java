package graph;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;

public class Graph implements Iterable<Edge> {
  private int[] vertices1;
  private int[] vertices2;
  private float[] values;
  private final int size;

  private Graph(Builder builder) {
    this.vertices1 = builder.vertices1;
    this.vertices2 = builder.vertices2;
    this.values = builder.values;
    this.size = builder.size;

    sortGraph();
    verifyEdgesWithSameVerticesAreAbsent();
  }

  public int size() {
    return size;
  }

  public Iterator<Edge> vertexIterator(int vertex) {
    return new Iterator<Edge>() {
      int index = findVertexIndex(vertices1, vertex, 0, size - 1, false);

      @Override
      public boolean hasNext() {
        return index >= 0 && index < size && vertices1[index] == vertex;
      }

      @Override
      public Edge next() {
        if (!hasNext()) {
          throw new NoSuchElementException();
        }
        Edge res = new Edge(vertex, vertices2[index], values[index]);
        index++;
        return res;
      }
    };
  }

  @Override
  public Iterator<Edge> iterator() {
    return new Iterator<Edge>() {
      int index = 0;

      @Override
      public boolean hasNext() {
        return index < size;
      }

      @Override
      public Edge next() {
        if (!hasNext()) {
          throw new NoSuchElementException();
        }
        Edge res = new Edge(vertices1[index], vertices2[index], values[index]);
        index++;
        return res;
      }
    };
  }

  public Optional<Float> getEdgeValue(int vertex1, int vertex2) {
    int vertex1FirstIndex = findVertexIndex(vertices1, vertex1, 0, size - 1, false);

    if (vertex1FirstIndex < 0) {
      return Optional.empty();
    }

    int vertex1LastIndex = findVertexIndex(vertices1, vertex1, 0, size - 1, true);
    int vertex2Index = findVertexIndex(vertices2, vertex2, vertex1FirstIndex, vertex1LastIndex, false);

    if (vertex2Index < 0) {
      return Optional.empty();
    }

    return Optional.of(values[vertex2Index]);
  }

  private static int findVertexIndex(int[] vertices, int vertex, int low, int high, boolean last) {
    int res = -1;

    while (low <= high) {
      int mid = (low + high) / 2;

      if (vertices[mid] > vertex) {
        high = mid - 1;
      } else if (vertices[mid] < vertex) {
        low = mid + 1;
      } else {
        res = mid;
        if (last) {
          low = mid + 1;
        } else {
          high = mid - 1;
        }
      }
    }

    return res;
  }

  private void sortGraph() {
    qsort(vertices1, 0, size - 1);

    int low = 0;
    int high = 0;
    while (high < size - 1) {
      high++;
      if (vertices1[high - 1] != vertices1[high]) {
        qsort(vertices2, low, high - 1);
        low = high;
      }
    }

    qsort(vertices2, low, high);
  }

  private void qsort(int[] array, int begin, int end) {
    if (begin < end) {
      int partitionIndex = partition(array, begin, end);

      qsort(array, begin, partitionIndex - 1);
      qsort(array, partitionIndex + 1, end);
    }
  }

  private int partition(int[] array, int begin, int end) {
    int pivot = array[end];
    int i = (begin - 1);

    for (int j = begin; j < end; j++) {
      if (array[j] <= pivot) {
        i++;

        swapRows(i, j);
      }
    }

    swapRows(i+1, end);

    return i+1;
  }

  private void verifyEdgesWithSameVerticesAreAbsent() {
    for (int i = 0; i < size - 1; i++) {
      if (vertices1[i] == vertices1[i+1] && vertices2[i] == vertices2[i+1]) {
        Edge edge1 = new Edge(vertices1[i], vertices2[i], values[i]);
        Edge edge2 = new Edge(vertices1[i+1], vertices2[i+1], values[i+1]);
        throw new IllegalArgumentException("Edges with same vertices are not allowed. "
            + edge1 + " has same vertices as " + edge2);
      }
    }
  }

  private void swapRows(int row1Index, int row2Index) {
    int vertex1tmp = vertices1[row1Index];
    int vertex2tmp = vertices2[row1Index];
    float valuetmp = values[row1Index];

    vertices1[row1Index] = vertices1[row2Index];
    vertices2[row1Index] = vertices2[row2Index];
    values[row1Index] = values[row2Index];
    vertices1[row2Index] = vertex1tmp;
    vertices2[row2Index] = vertex2tmp;
    values[row2Index] = valuetmp;
  }

  public static class Builder {
    private int[] vertices1;
    private int[] vertices2;
    private float[] values;
    private final int capacity;
    private int size;

    public Builder(int capacity) {
      if (capacity < 0) {
        throw new IllegalArgumentException("Capacity should be >= 0, given capacity = " + capacity);
      }

      this.capacity = capacity;
      this.vertices1 = new int[capacity];
      this.vertices2 = new int[capacity];
      this.values = new float[capacity];
    }

    public Builder addEdges(Iterable<Edge> edges) {
      for (Edge edge : edges) {
        addEdge(edge);
      }
      return this;
    }

    public Builder addEdge(Edge edge) {
      edge = Objects.requireNonNull(edge, "Adding null edge");
      return addEdge(edge.getVertex1(), edge.getVertex2(),  edge.getValue());
    }

    public Builder addEdge(int vertex1, int vertex2, float value) {
      if (size >= capacity) {
        throw new CapacityExceededException("Current capacity = " + capacity + " exceeded");
      }

      if (vertices1 == null || vertices2 == null || values == null) {
        throw new IllegalStateException("Adding edge after build()");
      }

      vertices1[size] = vertex1;
      vertices2[size] = vertex2;
      values[size] = value;
      size++;

      return this;
    }

    public Graph build() {
      Graph graph = new Graph(this);
      vertices1 = null;
      vertices2 = null;
      values = null;
      return graph;
    }
  }
}
