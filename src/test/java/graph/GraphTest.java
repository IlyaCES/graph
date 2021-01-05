package graph;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class GraphTest {
  private static List<Edge> edges1 = Arrays.asList(
      new Edge(1, 2, (float) 2.53),
      new Edge(1, 3, (float) 4.23),
      new Edge(2, 1, (float) 5.64),
      new Edge(3, 2, (float) 56.33),
      new Edge(3, 5, (float) 13.98),
      new Edge(4, 1, (float) 4.44),
      new Edge(4, 2, (float) 10.00),
      new Edge(4, 5, (float) 88.34),
      new Edge(5, 2, (float) 33.21),
      new Edge(5, 3, (float) 90.44),
      new Edge(6, 5, (float) 0.22),
      new Edge(7, 1, (float) 7.54),
      new Edge(7, 8, (float) 8.99),
      new Edge(8, 7, (float) 7.88)
  );

  private static List<Edge> edges2 = Arrays.asList(
      new Edge(1, 2, (float) 88.34),
      new Edge(1, 3, (float) 56.33),
      new Edge(2, 1, (float) 33.21),
      new Edge(3, 1, (float) 13.98),
      new Edge(4, 1, (float) 4.10)
  );

  private static List<Edge> edges3 = Arrays.asList(
      new Edge(1, 2, (float) 1.2),
      new Edge(2, 3, (float) 2.3),
      new Edge(3, 4, (float) 3.4),
      new Edge(4, 5, (float) 4.5),
      new Edge(5, 6, (float) 5.6),
      new Edge(6, 7, (float) 6.7),
      new Edge(7, 8, (float) 7.8),
      new Edge(8, 9, (float) 8.9),
      new Edge(9, 10, (float) 9.10),
      new Edge(10, 11, (float) 10.11),
      new Edge(11, 1, (float) 11.1)
  );

  private static Stream<Arguments> edgesForTest() {
    return Stream.of(
        Arguments.of(edges1),
        Arguments.of(edges2),
        Arguments.of(edges3)
    );
  }

  private static Stream<Arguments> edgesForSortedTest() {
    List<Edge> edges1Shuffled = new ArrayList<>(edges1);
    List<Edge> edges2Shuffled = new ArrayList<>(edges2);
    List<Edge> edges3Shuffled = new ArrayList<>(edges3);
    Collections.shuffle(edges1Shuffled);
    Collections.shuffle(edges2Shuffled);
    Collections.shuffle(edges3Shuffled);

    return Stream.of(
        Arguments.of(edges1, edges1Shuffled),
        Arguments.of(edges2, edges2Shuffled),
        Arguments.of(edges3, edges3Shuffled)
    );
  }

  @ParameterizedTest
  @MethodSource("edgesForTest")
  void testGetEdgeValue(List<Edge> edges) {
    Graph graph = new Graph.Builder(edges.size())
        .addEdges(edges)
        .build();

    for (Edge edge : edges) {
      Optional<Float> result = graph.getEdgeValue(edge.getVertex1(), edge.getVertex2());
      assertTrue(result.isPresent());
      float foundValue = result.get();
      assertEquals(edge.getValue(), foundValue);
    }
  }

  @Test
  void testGetNotPresentEdgeValue() {
    Graph graph = new Graph.Builder(0).build();

    assertFalse(graph.getEdgeValue(0, 1).isPresent());
    assertFalse(graph.getEdgeValue(5, 2).isPresent());
    assertFalse(graph.getEdgeValue(1, 5).isPresent());
  }

  @Test
  void testGetEdgeValueFirstVertexIsPresentSecondIsNot() {
    Graph graph = new Graph.Builder(3)
        .addEdge(new Edge(0, 1, 0))
        .addEdge(new Edge(1, 0, (float) 1.0))
        .addEdge(new Edge(2, 4, (float) 2.0))
        .build();

    assertFalse(graph.getEdgeValue(0, 5555).isPresent());
    assertFalse(graph.getEdgeValue(1, 222).isPresent());
    assertFalse(graph.getEdgeValue(2, 333).isPresent());
  }

  @ParameterizedTest
  @MethodSource("edgesForSortedTest")
  void testGraphSorted(List<Edge> edgesSorted, List<Edge> edgesShuffled) {
    Graph graph = new Graph.Builder(edgesShuffled.size())
        .addEdges(edgesShuffled)
        .build();

    Iterator<Edge> graphIterator = graph.iterator();
    Iterator<Edge> edgesSortedIterator = edgesSorted.iterator();

    while (graphIterator.hasNext() && edgesSortedIterator.hasNext()) {
      assertEquals(edgesSortedIterator.next(), graphIterator.next());
    }
  }

  @Test
  void testIterator() {
    Edge edge1 = new Edge(0, 1, 0);
    Edge edge2 = new Edge(1, 0, (float) 1.0);
    Edge edge3 = new Edge(2, 4, (float) 2.0);

    Graph graph = new Graph.Builder(3)
        .addEdge(edge1)
        .addEdge(edge2)
        .addEdge(edge3)
        .build();

    Iterator<Edge> it = graph.iterator();
    assertTrue(it.hasNext());
    assertEquals(edge1, it.next());
    assertTrue(it.hasNext());
    assertEquals(edge2, it.next());
    assertTrue(it.hasNext());
    assertEquals(edge3, it.next());
    assertFalse(it.hasNext());
  }

  @Test
  void testIteratorOnEmptyGraph() {
    Graph graph = new Graph.Builder(0).build();

    Iterator<Edge> it = graph.iterator();

    assertFalse(it.hasNext());
    assertFalse(it.hasNext());
    assertFalse(it.hasNext());
    assertThrows(NoSuchElementException.class, it::next);
    assertThrows(NoSuchElementException.class, it::next);
    assertThrows(NoSuchElementException.class, it::next);
  }

  @Test
  void testIteratorOnOneItem() {
    Edge edge = new Edge(0, 1, 0);

    Graph graph = new Graph.Builder(1)
        .addEdge(edge)
        .build();

    Iterator<Edge> it = graph.iterator();
    assertTrue(it.hasNext());
    assertTrue(it.hasNext());
    assertEquals(edge, it.next());
    assertFalse(it.hasNext());
    assertThrows(NoSuchElementException.class, it::next);
  }

  @Test
  void testIteratorRemoveUnsupported() {
    Graph graph = new Graph.Builder(0).build();

    Iterator<Edge> it = graph.iterator();
    assertThrows(UnsupportedOperationException.class, it::remove);
  }

  @Test
  void testVertexIterator() {
    Edge edge12 = new Edge(1, 2, (float) 2.53);
    Edge edge21 = new Edge(2, 1, (float) 5.64);
    Edge edge32 = new Edge(3, 2, (float) 56.33);
    Edge edge35 = new Edge(3, 5, (float) 13.98);
    Edge edge42 = new Edge(4, 2, (float) 10.00);
    Edge edge45 = new Edge(4, 5, (float) 88.34);

    List<Edge> edges = Arrays.asList(
        edge12,
        edge21,
        edge32,
        edge35,
        edge42,
        edge45
    );

    Graph.Builder builder = new Graph.Builder(edges.size());

    for (Edge edge : edges) {
      builder.addEdge(edge);
    }
    Graph graph = builder.build();

    Iterator<Edge> iterator1 = graph.vertexIterator(1);
    assertTrue(iterator1.hasNext());
    assertEquals(edge12, iterator1.next());
    assertFalse(iterator1.hasNext());

    Iterator<Edge> iterator2 = graph.vertexIterator(2);
    assertTrue(iterator2.hasNext());
    assertEquals(edge21, iterator2.next());
    assertFalse(iterator1.hasNext());

    Iterator<Edge> iterator3 = graph.vertexIterator(3);
    assertTrue(iterator3.hasNext());
    assertEquals(edge32, iterator3.next());
    assertTrue(iterator3.hasNext());
    assertEquals(edge35, iterator3.next());
    assertFalse(iterator3.hasNext());

    Iterator<Edge> iterator4 = graph.vertexIterator(4);
    assertTrue(iterator4.hasNext());
    assertEquals(edge42, iterator4.next());
    assertTrue(iterator4.hasNext());
    assertEquals(edge45, iterator4.next());
    assertFalse(iterator4.hasNext());
  }

  @Test
  void testVertexIteratorOnAbsentVertex() {
    Graph graph = new Graph.Builder(0).build();

    Iterator<Edge> it = graph.vertexIterator(1);
    assertFalse(it.hasNext());
    assertThrows(NoSuchElementException.class, it::next);

    Iterator<Edge> it2 = graph.vertexIterator(100);
    assertFalse(it2.hasNext());
    assertThrows(NoSuchElementException.class, it2::next);
  }

  @Test
  void testVertexIteratorOnOneItem() {
    Edge edge = new Edge(0, 1, 0);

    Graph graph = new Graph.Builder(1)
        .addEdge(edge)
        .build();

    Iterator<Edge> it = graph.vertexIterator(0);
    assertTrue(it.hasNext());
    assertTrue(it.hasNext());
    assertEquals(edge, it.next());
    assertFalse(it.hasNext());
    assertFalse(it.hasNext());
    assertThrows(NoSuchElementException.class, it::next);
    assertThrows(NoSuchElementException.class, it::next);
  }

  @Test
  void testVertexIteratorRemoveUnsupported() {
    Graph graph = new Graph.Builder(0).build();

    Iterator<Edge> it = graph.vertexIterator(0);
    assertThrows(UnsupportedOperationException.class, it::remove);
  }

  @ParameterizedTest
  @MethodSource("edgesForTest")
  void testSize(List<Edge> edges) {
    Graph graph = new Graph.Builder(edges.size())
        .addEdges(edges)
        .build();

    assertEquals(edges.size(), graph.size());
  }

  @Test
  void testBuildGraphWithEdgesWithSameVertices() {
    Edge edge1 = new Edge(0, 1, (float) 0.11);
    Edge edge2 = new Edge(0, 1, (float) 0.12);
    Graph.Builder builder = new Graph.Builder(2);

    builder.addEdge(edge1);
    builder.addEdge(edge2);
    assertThrows(IllegalArgumentException.class, builder::build);
  }
}