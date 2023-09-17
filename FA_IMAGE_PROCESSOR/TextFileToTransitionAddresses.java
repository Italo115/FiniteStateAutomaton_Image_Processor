import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;

/**
 * This class represents a text file to transition addresses converter. It reads
 * a text file containing the description of an automaton and generates a list
 * of transition addresses for each accept state.
 */
public class TextFileToTransitionAddresses {
	private int maxNumberOfStates;
	private ArrayList<Integer> acceptStates;
	private ArrayList<ArrayList<Integer>> transitionAddresses;
	Map<Integer, Map<Integer, Integer>> mapOfTextFile;
	ArrayList<Integer> currentPathing;
	Set<Integer> visitedPaths;
	Map<Integer, List<Pair>> mapOfTextFileMR;

	/**
	 * Returns the list of transition addresses.
	 *
	 * @return An ArrayList of ArrayLists containing integers that represent the
	 * transition addresses.
	 */
	public ArrayList<ArrayList<Integer>> getTransitionAddresses() {
		return transitionAddresses;
	}

	/**
	 * Constructs a new TextFileToTransitionAddresses object. It initializes the
	 * required data structures and performs the depth-first search for each
	 * accept state.
	 *
	 * @param args The command line arguments containing the text file path.
	 */
	TextFileToTransitionAddresses(String[] args) {
		acceptStates = new ArrayList<>();
		mapOfTextFile = new HashMap<>();
		mapOfTextFileMR = new HashMap<>();
		transitionAddresses = new ArrayList<>();
		currentPathing = new ArrayList<>();
		visitedPaths = new HashSet<>();

		File textFile = new File(args[args.length - 1]);
		textFileToValuesInitialisation(textFile, args);

		if (args.length == 5)
			for (int currentAcceptState : acceptStates) {
				depthFirstSearch(0, currentAcceptState, currentPathing, transitionAddresses,
						visitedPaths, Integer.parseInt(args[3]));

				Iterator<ArrayList<Integer>> iterator = transitionAddresses.iterator();
				while (iterator.hasNext()) {
					ArrayList<Integer> innerList = iterator.next();

					// Check if the size of the inner ArrayList is less the word
					// length
					if (innerList.size() < Integer.parseInt(args[3])) {
						iterator.remove();
					}
				}
			}

		else {
			for (int currentAcceptState : acceptStates) {
				depthFirstSearch(0, currentAcceptState, currentPathing, transitionAddresses,
						visitedPaths);
			}
		}
	}

	/**
	 * Initializes the instance variables by reading the text file containing
	 * the automaton description. It stores the number of states, the accept
	 * states, and the transitions in corresponding data structures.
	 *
	 * @param textFile The text file containing the automaton description.
	 */
	private void textFileToValuesInitialisation(File textFile, String[] args) {
		try {
			Scanner scanner = new Scanner(textFile);

			// Read the number of states
			String line = scanner.nextLine();
			this.maxNumberOfStates = Integer.parseInt(line);

			// Read the accept states
			line = scanner.nextLine();
			Scanner scanner1 = new Scanner(line);

			while (scanner1.hasNextInt()) {
				this.acceptStates.add(scanner1.nextInt());
			}
			scanner1.close();

			// Read the transitions
			while (scanner.hasNext()) {
				int fromState = scanner.nextInt();
				int toState = scanner.nextInt();
				int transition = scanner.nextInt();

				if (args.length == 5) {

					if (!mapOfTextFileMR.containsKey(fromState)) {
						mapOfTextFileMR.put(fromState, new ArrayList<>());
					}
					mapOfTextFileMR.get(fromState).add(new Pair(transition, toState));

				} else {

					if (!mapOfTextFile.containsKey(fromState)) {
						mapOfTextFile.put(fromState, new HashMap<>());
					}
					mapOfTextFile.get(fromState).put(transition, toState);
				}
			}
			scanner.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Performs a depth-first search to find all paths from the initial state to
	 * the given accept state. The found paths are stored in the
	 * transitionAddresses data structure.
	 *
	 * @param firstState The initial state of the search.
	 * @param currentAcceptState The target accept state.
	 * @param currentPathing The current path taken in the search.
	 * @param transitionAddresses The data structure to store the found paths.
	 * @param isVisitedPaths The set of visited states to avoid loops.
	 * @param wordLength used for multi res
	 */
	private void depthFirstSearch(int firstState, int currentAcceptState,
			ArrayList<Integer> currentPathing, ArrayList<ArrayList<Integer>> transitionAddresses,
			Set<Integer> isVisitedPaths, int wordLength) {

		if (wordLength < 0) {
			return;
		}

		if (firstState == currentAcceptState) {
			transitionAddresses.add(new ArrayList<>(currentPathing));
		}

		// If the map does not contain the first state, there's nothing more to
		// search from this state, so return
		if (!mapOfTextFileMR.containsKey(firstState)) {
			return;
		}

		// Iterate through the transitions of the current state (firstState)
		for (Pair transition : mapOfTextFileMR.get(firstState)) {
			// Add the current transition to the current path
			currentPathing.add(transition.getFirst());

			// Perform a depth-first search recursively on the destination
			// state
			depthFirstSearch(transition.getSecond(), currentAcceptState, currentPathing,
					transitionAddresses, isVisitedPaths, wordLength - 1);

			// Remove the last added transition from the current path, as we
			// backtrack
			int pathSize = currentPathing.size() - 1;
			currentPathing.remove(pathSize);
		}
	}

	/**
	 * Performs a depth-first search to find all paths from the initial state to
	 * the given accept state. The found paths are stored in the
	 * transitionAddresses data structure.
	 *
	 * @param firstState The initial state of the search.
	 * @param currentAcceptState The target accept state.
	 * @param currentPathing The current path taken in the search.
	 * @param transitionAddresses The data structure to store the found paths.
	 * @param isVisitedPaths The set of visited states to avoid loops.
	 */
	private void depthFirstSearch(int firstState, int currentAcceptState,
			ArrayList<Integer> currentPathing, ArrayList<ArrayList<Integer>> transitionAddresses,
			Set<Integer> isVisitedPaths) {

		if (firstState == currentAcceptState) {
			transitionAddresses.add(new ArrayList<>(currentPathing));
			return;
		}

		// If the map does not contain the first state, there's nothing more to
		// search from this state, so return
		if (!mapOfTextFile.containsKey(firstState)) {
			return;
		}

		// Mark the first state as visited
		isVisitedPaths.add(firstState);

		// Iterate through the transitions of the current state (firstState)
		for (Entry<Integer, Integer> transition : mapOfTextFile.get(firstState).entrySet()) {

			// If the destination state (transition.getValue()) has not been
			// visited yet, proceed
			if (!isVisitedPaths.contains(transition.getValue())) {

				// Add the current transition to the current path
				currentPathing.add(transition.getKey());

				// Perform a depth-first search recursively on the destination
				// state
				depthFirstSearch(transition.getValue(), currentAcceptState, currentPathing,
						transitionAddresses, isVisitedPaths);

				// Remove the last added transition from the current path, as we
				// backtrack
				int pathSize = currentPathing.size() - 1;
				currentPathing.remove(pathSize);
			}
		}
		// Remove the first state from the visited set as we backtrack
		isVisitedPaths.remove(firstState);
	}

	/**
	 * This class represents a pair of two integer values. It is used for
	 * storing two related integer values together in a single object.
	 */
	public class Pair {
		int first;
		int second;

		/**
		 * Constructs a new Pair with the given first and second integer values.
		 *
		 * @param first The first integer of the pair.
		 * @param second The second integer of the pair.
		 */
		public Pair(int first, int second) {
			this.first = first;
			this.second = second;
		}

		/**
		 * Returns the first integer of the pair.
		 *
		 * @return The first integer of the pair.
		 */
		public int getFirst() {
			return this.first;
		}

		/**
		 * Returns the second integer of the pair.
		 *
		 * @return The second integer of the pair.
		 */
		public int getSecond() {
			return this.second;
		}
	}
}