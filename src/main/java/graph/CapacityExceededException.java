package graph;

public class CapacityExceededException extends RuntimeException {
  CapacityExceededException() {
  }

  CapacityExceededException(String msg) {
    super(msg);
  }

  CapacityExceededException(String msg, Throwable cause) {
    super(msg, cause);
  }
}