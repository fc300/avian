public class InvokeDynamic {
  private final int foo;

  private InvokeDynamic(int foo) {
    this.foo = foo;
  }

  private interface Operation {
    int operate(int a, int b);
  }

  private interface Operation2 {
    long operate(long a, int b);
  }

  private static class Pair<A, B> {
    public final A first;
    public final B second;

    public Pair(A first, B second) {
      this.first = first;
      this.second = second;
    }
  }

  private interface Supplier<T> extends java.io.Serializable {
    T get();
  }

  private static void expect(boolean v) {
    if (! v) throw new RuntimeException();
  }

  public static void main(String[] args) {
    int c = 4;
    Operation op = (a, b) -> a + b - c;
    expect(op.operate(2, 3) == (2 + 3) - 4);

    for (int i = 0; i < 4; ++i) {
      new InvokeDynamic(i).test();
    }
  }

  private void test() {
    { int c = 2;
      Operation op = (a, b) -> ((a + b) * c) - foo;
      expect(op.operate(2, 3) == ((2 + 3) * 2) - foo);
    }

    { int c = 2;
      Operation2 op = (a, b) -> ((a + b) * c) - foo;
      expect(op.operate(2, 3) == ((2 + 3) * 2) - foo);
    }

    { Supplier<Pair<Long, Double>> s = () -> new Pair<Long, Double>(42L, 77.1D);
      expect(s.get().first == 42L);
      expect(s.get().second == 77.1D);
    }

    { double[] a = new double[] { 3.14D };
      Supplier<Pair<Long, Double>> s = () -> new Pair<Long, Double>(42L, a[0]);
      expect(s.get().first == 42L);
      expect(s.get().second == 3.14D);
    }
  }
}
