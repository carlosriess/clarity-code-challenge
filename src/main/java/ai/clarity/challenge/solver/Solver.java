package ai.clarity.challenge.solver;

import static ai.clarity.challenge.Solution.newFoundSolution;

import ai.clarity.challenge.Operator;
import ai.clarity.challenge.Solution;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class Solver {

  private static Logger LOGGER = LogManager.getLogger(Solver.class);
  private final Supplier<InputStream> input;
  private final String variableToSolve;
  private final Operator operator;
  private List<String> equationLines;
  private int phase;

  public Solver(Supplier<InputStream> input, Operator operator, String variable) {
    this.input = input;
    this.operator = operator;
    this.variableToSolve = variable;
  }

  /**
   * Solve variable
   *
   * @return The solution for the target variable.
   */
  public Solution solve() {

    // TO BE IMPLEMENTED
    LOGGER.info("solve {}", variableToSolve);
    init();

    Long resultSol = recursiveOperation(variableToSolve);
    LOGGER.info("{} = {} ", variableToSolve, resultSol);
    return newFoundSolution(variableToSolve, resultSol);
  }

  /**
   * Given the Supplier of data the init() method extracts all equation info into a List of lines
   * and checks for the correctness with a regex expression.Sets phase = 0 to signal the starting
   * point of the solution
   */
  private void init() {
    phase = 0;
    BufferedReader reader = new BufferedReader(new InputStreamReader(input.get()));
    equationLines =
        reader
            .lines()
            .map(
                line -> {
                  boolean matches =
                      line.matches(
                          "([a-zA-Z]*|[a-zA-Z0-9]*){1}(\\s=\\s){1}([a-zA-Z]*|[a-zA-Z0-9]*|\\d*)(\\s#\\s([a-zA-Z]*|[a-zA-Z0-9]*|\\d*))*(\\s?)");
                  if (!matches) throw new RuntimeException("Not a valid expression");
                  return line.trim();
                })
            .collect(Collectors.toList());
  }

  /**
   * variable : when parsing the lines of the equation x=y+7 will be y When phase = 0 variable
   * should be equal to variable to solve, otherwise there's no solution
   *
   * <p>returns : value of operation applying recursive algorithm or null in case there's an error
   * or no solution is found
   */
  private Long recursiveOperation(String variable) {
    // check variable is unique
    // Variable to solve and
    if (phase == 0 && !variableToSolve.equals(variable)) return null;
    else if (phase > 0 && variableToSolve.equals(variable)) return null;

    phase++;

    String varLine =
        equationLines.stream()
            .filter(line -> line.startsWith(variable))
            .findFirst()
            .orElseGet(() -> "");

    // In case we try to solve a variable that is not defined in any equation.
    if (varLine.isEmpty()) return null;

    String[] values = varLine.split("=")[1].split("#");
    List<String> valuesList =
        Arrays.asList(values).stream().map(value -> value.trim()).collect(Collectors.toList());
    // check if variable is repeated in values
    long finalResult = 0;
    for (int i = 0; i < valuesList.size(); i++) {
      Long number;
      try {
        number = Long.parseLong(valuesList.get(i));
      } catch (NumberFormatException e) {
        number = recursiveOperation(valuesList.get(i));
        if (number == null) return null;
      }
      finalResult = operator.operate(finalResult, number);
    }

    return finalResult;
  }
}
