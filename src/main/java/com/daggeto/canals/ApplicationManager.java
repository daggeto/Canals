package com.daggeto.canals;

import com.daggeto.canals.controller.GraphController;
import org.apache.commons.lang.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ApplicationManager {

  private class Route {

    String from;
    String to;
    int weight;

    public Route(String from, String to, int weight) {
      this.from = from;
      this.to = to;
      this.weight = weight;
    }
  }

  private class Command {

    String command;
    List<String> args;

    public Command() {
    }

    public Command(String command, List<String> args) {
      this.command = command;
      this.args = args;
    }
  }

  public static String ADD_COMMAND = "add";
  public static String CALCULATE_COMMAND = "calculate";
  public static String EXIT_COMMAND = "exit";
  private final String NO_SUCH_ROUTE = "NO SUCH ROUTE";
  private final String WRONG_ROUTE = "Wrong route: %s";
  private final String WRONG_COMMAND = "Wrong command";

  private GraphController controller;

  public void start() {
    setController(new GraphController());
    System.out.println("Canals App Start");
    System.out.println("Commands:");
    System.out.println("    add route  - To add new route. Example: AB7 BC2 ... . From A to B with weight of 7");
    System.out.println("    calculate  - Run all calculations");
    System.out.println("    exit       - To exit application");

    Command commandLine = new Command();

    while (!EXIT_COMMAND.equals(commandLine.command)) {
      commandLine = readCommand();

      if (commandLine.command == null) {
        continue;
      }

      try {
        executeCommand(commandLine);
      } catch (IllegalArgumentException e) {
        System.out.println(e.getMessage());
      }

    }

  }

  private void executeCommand(Command commandLine) {
    if (ADD_COMMAND.equals(commandLine.command)) {
      addRoute(commandLine);
      return;
    }

    if (CALCULATE_COMMAND.equals(commandLine.command)) {
      calculate();
    }
  }

  private void calculate() {
    Integer result = controller.findPath("A-B-C");
    printResultString(1, result);

    result = controller.findPath("A-D");
    printResultString(2, result);

    result = controller.findPath("A-D-C");
    printResultString(3, result);

    result = controller.findPath("A-E-B-C-D");
    printResultString(4, result);

    result = controller.findPath("A-E-D");
    printResultString(5, result);

    result = controller.countTripsWithLessOrEqualStops("C", "C", 3);
    printResultString(6, result);

    result = controller.countTripsWithLimitStops("A", "C", 4);
    printResultString(7, result);

    result = controller.findShortestRoute("A", "C");
    printResultString(8, result);

    result = controller.findShortestRoute("B", "B");
    printResultString(9, result);

    result = controller.findNumberOfRoutesWithDistanceLessThan("C", "C", 30);
    printResultString(10, result);
  }

  private void printResultString(int number, Integer result) {
    String resultString = result != null && result != 0 ? String.valueOf(result) : NO_SUCH_ROUTE;
    System.out.printf("#%d: %s\n", number, resultString);
  }

  private void addRoute(Command addCommand) {
    if (!validateAddCommand(addCommand)) {
      throw new IllegalArgumentException(WRONG_COMMAND);
    }

    List<Route> preparedRoutes = prepareRoutes(addCommand.args);

    for (Route route : preparedRoutes) {
      controller.addRoute(route.from, route.to, route.weight);
    }

  }

  private List<Route> prepareRoutes(List<String> routes) {
    List<Route> result = new ArrayList<Route>();
    for (String route : routes) {
      result.add(parseRoute(route));
    }
    return result;
  }

  private Route parseRoute(String route) {
    try {
      String from = route.substring(0, 1);
      String to = route.substring(1, 2);
      int weight = Integer.valueOf(route.substring(2));

      return new Route(from, to, weight);

    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException(String.format(WRONG_ROUTE, route));
    } catch (IndexOutOfBoundsException e) {
      throw new IllegalArgumentException(String.format(WRONG_ROUTE, route));
    }
  }

  private boolean validateAddCommand(Command addCommand) {
    if (addCommand.args == null || addCommand.args.size() == 0) {
      return false;
    }

    return true;
  }

  private boolean validateListCommand(Command listCommand) {
    return listCommand.args != null && !listCommand.args.isEmpty();
  }

  private Command readCommand() {
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    try {
      System.out.println("Execute action:");
      String line = br.readLine();

      List<String> splitCommand = Arrays.asList(StringUtils.split(line, " "));

      if (splitCommand.isEmpty()) {
        return new Command();
      }

      return new Command(splitCommand.get(0), splitCommand.subList(1, splitCommand.size()));
    } catch (IOException e) {
      e.printStackTrace();
    }

    return null;
  }

  public void setController(GraphController controller) {
    this.controller = controller;
  }
}
