import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Method to make the finite state automaton
 */

public class FiniteStateAutomaton {

  private Map<Integer, List<Edge>> transitions;

  /**
   * Creates a HashMap
   */
  public FiniteStateAutomaton() {
    transitions = new HashMap<>();
  }

  /**
   * Adds a transition to the list
   *
   * @param startPosition startingPosition for traversal
   * @param endPosition   endPosition for traversal
   * @param input         input given
   */
  public void addTransition(int startPosition, int endPosition, int input) {
    Edge edge = new Edge(endPosition, input);
    if (!transitions.containsKey(startPosition)) {
      transitions.put(startPosition, new ArrayList<>());
    }
    transitions.get(startPosition).add(edge);
  }

  /**
   * Method that finds all the permutations of path that are traversable
   *
   * @param startPosition starting position from which we travel
   * @param endPosition   end position we would like to reach
   * @return return the list<list<Integer>> containing all the permutations of inputs to endnode
   */
  public List<List<Integer>> getAllPermutations(int startPosition, int endPosition) {
    List<List<Integer>> result = new ArrayList<>();
    List<Integer> path = new ArrayList<>();
    Set<Integer> visited = new HashSet<>();
    dfs(startPosition, endPosition, path, result, visited);
    return result;
  }

  /**
   * Depth first search method
   *
   * @param curr    current node
   * @param end     end node
   * @param path    path to traverse
   * @param result  node you wish to travel to
   * @param visited boolean checking if it has been visited
   */
  private void dfs(int curr, int end, List<Integer> path, List<List<Integer>> result,
      Set<Integer> visited) {
    if (curr == end) {
      result.add(new ArrayList<>(path));
      return;
    }
    if (!transitions.containsKey(curr)) {
      return;
    }
    visited.add(curr);
    for (Edge edge : transitions.get(curr)) {
      if (!visited.contains(edge.endPosition)) {
        path.add(edge.input);
        dfs(edge.endPosition, end, path, result, visited);
        path.remove(path.size() - 1);
      }
    }
    visited.remove(curr);
  }

  /**
   * Class that finds the edges of bfs
   */
  private static class Edge {

    int endPosition;
    int input;

    /**
     * Method that finds edge
     *
     * @param endPosition endPosition of edge
     * @param input       input given
     */
    Edge(int endPosition, int input) {
      this.endPosition = endPosition;
      this.input = input;
    }
  }
}
