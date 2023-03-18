import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FiniteStateAutomaton {

    private Map<Integer, List<Edge>> transitions;

    public FiniteStateAutomaton() {
        transitions = new HashMap<>();
    }

    public void addTransition(int startPosition, int endPosition, int input) {
        Edge edge = new Edge(endPosition, input);
        if (!transitions.containsKey(startPosition)) {
            transitions.put(startPosition, new ArrayList<>());
        }
        transitions.get(startPosition).add(edge);
    }

    public List<List<Integer>> getAllPermutations(int startPosition, int endPosition) {
        List<List<Integer>> result = new ArrayList<>();
        List<Integer> path = new ArrayList<>();
        Set<Integer> visited = new HashSet<>();
        dfs(startPosition, endPosition, path, result, visited);
        return result;
    }

    private void dfs(int curr, int end, List<Integer> path, List<List<Integer>> result, Set<Integer> visited) {
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
     * private void dfs(int curr, int end, List<Integer> path, List<List<Integer>> result, Set<Pair<Integer, Integer>> visited) {
     *     for (Edge edge : transitions.get(curr)) {
     *         Pair<Integer, Integer> currState = new Pair<>(curr, edge.input);
     *         if (!visited.contains(currState)) {
     *             visited.add(currState);
     *             path.add(edge.input);
     *             dfs(edge.endPosition, end, path, result, visited);
     *             path.remove(path.size() - 1);
     *             visited.remove(currState);
     *         }
     *     }
     * }
     */

    private static class Edge {
        int endPosition;
        int input;

        public Edge(int endPosition, int input) {
            this.endPosition = endPosition;
            this.input = input;
        }
    }
}