package graph;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import graph.Graph.Builder;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;


class GraphBuilderTest {
  private static Edge someEdge = new Edge(0, 1, 0);

  private static Stream<Arguments> edgesForTest() {
    Object[] edges1 = {
        new Edge(1, 2, (float) 2.53),
        new Edge(4, 2, (float) 10.00),
        new Edge(2, 1, (float) 5.64),
        new Edge(1, 3, (float) 4.23),
        new Edge(4, 1, (float) 4.44),
        new Edge(5, 3, (float) 90.44),
        new Edge(4, 5, (float) 88.34),
        new Edge(5, 2, (float) 33.21),
        new Edge(3, 5, (float) 13.98),
        new Edge(3, 2, (float) 56.33)
    };

    Object[] edges2 = {
        new Edge(1, 2, (float) 88.34),
        new Edge(2, 1, (float) 33.21),
        new Edge(3, 1, (float) 13.98),
        new Edge(1, 3, (float) 56.33)
    };

    return Stream.of(
        Arguments.of(Arrays.asList(edges1)),
        Arguments.of(Arrays.asList(edges2))
    );
  }

  @ParameterizedTest
  @MethodSource("edgesForTest")
  void testAddEdges(List<Edge> edges) {
    Graph graph = new Graph.Builder(edges.size())
        .addEdges(edges)
        .build();

    Set<Edge> graphEdges = new HashSet<>();
    for (Edge edge : graph) {
      graphEdges.add(edge);
    }

    assertEquals(new HashSet<>(edges), graphEdges);
  }

  @ParameterizedTest
  @MethodSource("edgesForTest")
  void testAddEdge(List<Edge> edges) {
    Graph.Builder builder = new Graph.Builder(edges.size());

    for (Edge edge : edges) {
      builder.addEdge(edge);
    }
    Graph graph = builder.build();

    Set<Edge> graphEdges = new HashSet<>();
    for (Edge edge : graph) {
      graphEdges.add(edge);
    }

    assertEquals(new HashSet<>(edges), graphEdges);
  }

  @Test
  void testAddNullEdge() {
    Graph.Builder builder = new Builder(5);

    assertThrows(NullPointerException.class, () -> builder.addEdge(null));
  }

  @Test
  void testExceedCapacity() {
    Graph.Builder builder = new Builder(0);

    assertThrows(CapacityExceededException.class, () -> builder.addEdge(someEdge));
  }

  @Test
  void testAddAfterBuild() {
    Graph.Builder builder = new Graph.Builder(1);

    builder.build();
    assertThrows(IllegalStateException.class, () -> {
      builder.build();
      builder.addEdge(someEdge);
    });
  }

  @Test
  void testCreateBuilderWithNegativeCapacity() {
    assertThrows(IllegalArgumentException.class, () -> new Graph.Builder(-1));
  }
}
