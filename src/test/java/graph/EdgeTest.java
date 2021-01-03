package graph;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class EdgeTest {
  private static Edge edge1;
  private static Edge edge2;
  private static Edge edge3;

  @BeforeAll
  static void init() {
    edge1 = new Edge(1, 2, (float) 10.00);
    edge2 = new Edge(1, 2, (float) 10.00);
    edge3 = new Edge(5, 10, (float) 5.55);
  }

  @Test
  void testReflexive() {
    assertEquals(edge1, edge1);
    assertEquals(edge2, edge2);
    assertEquals(edge3, edge3);
  }

  @Test
  void testSymmetric() {
    assertEquals(edge1, edge2);
    assertEquals(edge2, edge1);

    assertFalse(edge1.equals(edge3));
    assertFalse(edge3.equals(edge1));
  }

  @Test
  void testConsistent() {
    for (int i = 0; i < 100; i++) {
      assertEquals(edge1, edge2);
      assertFalse(edge1.equals(edge3));
    }
  }

  @Test
  void testNotEqualNull() {
    assertFalse(edge1.equals(null));
  }
}