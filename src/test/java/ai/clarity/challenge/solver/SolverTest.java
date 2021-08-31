package ai.clarity.challenge.solver;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ai.clarity.challenge.Solution;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Optional;
import java.util.function.Supplier;

public class SolverTest {

  private static Supplier<InputStream> stringAsInput(String string) {
    return () -> new ByteArrayInputStream(string.getBytes());
  }

  @Test
  public void solve1plus2() {
    // given
    String string = "a = 1 # 2";

    Solver solver = new Solver(stringAsInput(string), Long::sum, "a");

    // when
    Solution solution = solver.solve();

    // then
    assertEquals(Optional.of(3L), solution.getValue());
  }

  @Test
  public void solveForward() {
    // given
    String string = "myvar = 1 # 2\n" + "forwardResult = myvar # 2";

    Solver solver = new Solver(stringAsInput(string), Long::sum, "forwardResult");

    // when
    Solution solution = solver.solve();

    // then
    assertEquals(Optional.of(5L), solution.getValue());
  }

  @Test
  public void solveBackward() {
    // given
    String string = "fun = last # 2\n" + "backwardResult = fun # last # 3\n" + "last = 1\n";

    Solver solver = new Solver(stringAsInput(string), Long::sum, "backwardResult");

    // when
    Solution solution = solver.solve();

    // then
    assertEquals(Optional.of(7L), solution.getValue());
  }

  @Test
  public void solveNoSolution() {
    // given
    String string = "x = y # 2\n" + "z = x # y # 3";

    Solver solver = new Solver(stringAsInput(string), Long::sum, "z");

    // when
    Solution solution = solver.solve();

    // then
    assertTrue(solution.getValue().isEmpty());
  }

  @Test
  public void solveLoop() {
    // given
    String string = "look1 = 2 # look2 \n" + "look2 = look3 # 99 # 12\n" + "look3 = 1 # look1";

    Solver solver = new Solver(stringAsInput(string), Long::sum, "look1");

    // when
    Solution solution = solver.solve();

    // then
    assertTrue(solution.getValue().isEmpty());
  }

  @Test
  public void solveLongExample() {

    // given
    String equation =
        "x = 3759 # 6218 # 1054 # 2547 # 8654 # 8574 # 9083 # 5607\n"
            + "g = 7206 # x # 8837 # x\n"
            + "l = 5008 # g # g # g # 1970 # 2741 # 2146 # x\n"
            + "j = 1978 # 4198 # 8447 # x\n"
            + "w = j # g # 9302 # 2909 # 2104 # 7339 # j # x\n"
            + "v = 2283 # l # g # 4257 # w # 8987 # 3253 # 2423 # 3048\n"
            + "t = 256 # 8530 # l # 7386 # g\n"
            + "d = 4612 # t # t # w # t # v # 1054\n"
            + "f = 1274 # l # j # 4450 # j # 7756 # l # 9727 # t # l\n"
            + "s = d # t # t # w # x # t # d # l # g # w";
    // when
    Solver solver = new Solver(stringAsInput(equation), Long::sum, "x");

    Solution solution = solver.solve();

    // then
    assertEquals(Optional.ofNullable(45496L), solution.getValue());

    // when
    solver = new Solver(stringAsInput(equation), Long::sum, "g");

    solution = solver.solve();

    // then
    assertEquals(Optional.ofNullable(107035L), solution.getValue());

    // when
    solver = new Solver(stringAsInput(equation), Long::sum, "l");

    solution = solver.solve();

    // then
    assertEquals(Optional.ofNullable(378466L), solution.getValue());

    // when
    solver = new Solver(stringAsInput(equation), Long::sum, "j");

    solution = solver.solve();

    // then
    assertEquals(Optional.ofNullable(60119L), solution.getValue());

    // when
    solver = new Solver(stringAsInput(equation), Long::sum, "w");

    solution = solver.solve();

    // then
    assertEquals(Optional.ofNullable(294423L), solution.getValue());

    // when
    solver = new Solver(stringAsInput(equation), Long::sum, "v");

    solution = solver.solve();

    // then
    assertEquals(Optional.ofNullable(804175L), solution.getValue());

    // when
    solver = new Solver(stringAsInput(equation), Long::sum, "t");

    solution = solver.solve();

    // then
    assertEquals(Optional.ofNullable(501673L), solution.getValue());

    // when
    solver = new Solver(stringAsInput(equation), Long::sum, "d");

    solution = solver.solve();

    // then
    assertEquals(Optional.ofNullable(2609283L), solution.getValue());

    // when
    solver = new Solver(stringAsInput(equation), Long::sum, "f");

    solution = solver.solve();

    // then
    assertEquals(Optional.ofNullable(1780516L), solution.getValue());

    // when
    solver = new Solver(stringAsInput(equation), Long::sum, "s");

    solution = solver.solve();

    // then
    assertEquals(Optional.ofNullable(7843428L), solution.getValue());
  }

  @Test
  public void solveExampleWithLambda() {
    // given
    String equation =
        "varA = 55 # varB # 2\n" + "varB = varC # 99 # 12 # 256 # 1000\n" + "varC = 5";

    // when
    Solver solver =
        new Solver(
            stringAsInput(equation),
            (x, y) -> {
              return x + y;
            },
            "varA");

    Solution solution = solver.solve();

    // then
    assertEquals(Optional.ofNullable(1429L), solution.getValue());
  }
}
