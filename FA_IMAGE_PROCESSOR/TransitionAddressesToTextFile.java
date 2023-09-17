import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * This class is responsible for generating a transition map based on given
 * transition addresses and writing it to a text file. The transition map is
 * represented as a Map<Integer, Map<Integer, Integer>>.
 */
public class TransitionAddressesToTextFile {
	private final Map<Integer, Map<Integer, Integer>> mapOfTextFile = new HashMap<>();
	private final List<Set<String>> language;
	private int alphabetSize = 4;
	private int currentProcessingState;
	private int totalStates;

	/**
	 * Constructs an instance of TransitionAddressesToTextFile and initializes
	 * the required fields. Builds the transition map and writes it to a text
	 * file using the specified input file path.
	 *
	 * @param inputs The list of transition addresses.
	 * @param inputFilePath The path to the input file.
	 * @param args Command line arguments.
	 */
	public TransitionAddressesToTextFile(ArrayList<ArrayList<Integer>> inputs, String inputFilePath,
			String[] args) {
		this.language = new ArrayList<>();
		totalStates = 0;
		currentProcessingState = 0;
		mapOfTextFile.put(0, new HashMap<>());

		// Convert input transition addresses to language sets
		language.add(new HashSet<>(convertLanguageToIntegers(inputs)));
		buildTransitionMap();

		// Write the transition map to a text file
		if (args.length == 5) {
			writeToTextFile(inputFilePath, Integer.parseInt(args[3]));
		} else {
			writeToTextFile(inputFilePath, -1);
		}
	}

	/**
	 * Builds the transition map by iterating through the current unprocessed
	 * states and updating the map with appropriate transitions.
	 */
	private void buildTransitionMap() {
		while (currentProcessingState <= totalStates) {

			for (int alphabet = 0; alphabet < alphabetSize; alphabet++) {
				// Find the reverse language for the current state and alphabet
				Set<String> reverseLanguage = findReverseLanguage(currentProcessingState,
						String.valueOf(alphabet));

				if (!reverseLanguage.isEmpty()) {
					boolean languageLocated = false;

					for (int j = 0; j <= totalStates; j++) {
						// Check if the reverse language exists in the language
						// list
						if (language.get(j).equals(reverseLanguage)) {
							// Add the transition to the map
							mapOfTextFile.putIfAbsent(currentProcessingState, new HashMap<>());
							mapOfTextFile.get(currentProcessingState).put(alphabet, j);
							languageLocated = true;
							break;
						}
					}

					if (!languageLocated) {
						// If the reverse language is not found, add a new state
						totalStates++;
						language.add(reverseLanguage);
						mapOfTextFile.put(totalStates, new HashMap<>());
						mapOfTextFile.get(currentProcessingState).put(alphabet, totalStates);
						mapOfTextFile.putIfAbsent(currentProcessingState, new HashMap<>());
						mapOfTextFile.get(currentProcessingState).put(alphabet, totalStates);
					}
				}
			}
			// Move to the next state for processing
			currentProcessingState++;
		}
	}

	/**
	 * Finds the reverse language for the given state and prefix.
	 *
	 * @param state The state for which the reverse language is to be found.
	 * @param prefix The prefix of the reverse language.
	 * @return A Set<String> containing the reverse language.
	 */
	private Set<String> findReverseLanguage(int state, String prefix) {
		Set<String> inverse = new HashSet<>();

		// Iterate through the language set of the given state
		for (String word : language.get(state)) {

			// If the word starts with the prefix, add it to the inverse set
			// after removing the prefix
			if (word.startsWith(prefix)) {
				inverse.add(word.substring(prefix.length()));
			}
		}
		return inverse;
	}

	/**
	 * Converts the language to a set of strings.
	 *
	 * @param language The list of transition addresses to be converted.
	 * @return A Set<String> containing the transition addresses as strings.
	 */
	private Set<String> convertLanguageToIntegers(ArrayList<ArrayList<Integer>> language) {
		Set<String> languageIntegers = new HashSet<>();

		for (List<Integer> word : language) {
			// Convert the list of integers to a string and add it to the set
			languageIntegers.add(convertWordToString(word));
		}
		return languageIntegers;
	}

	/**
	 * Converts a list of integers representing a word to a string.
	 *
	 * @param word The list of integers representing a word.
	 * @return A String representing the word.
	 */
	private String convertWordToString(List<Integer> word) {
		StringBuilder sb = new StringBuilder();

		// Iterate through the list of integers and append each to the string
		// builder
		for (int symbol : word) {
			sb.append(symbol);
		}
		return sb.toString();
	}

	/**
	 * Writes the transition map to a text file using the specified input file
	 * path and method.
	 *
	 * @param inputFilePath The path to the input file.
	 * @param method The method used for generating the transition map.
	 */
	private void writeToTextFile(String inputFilePath, int method) {
		ArrayList<Integer> acceptStates = new ArrayList<>();

		if (method == 3) {
			for (int k = 0; k <= totalStates; k++) {
				acceptStates.add(k);
			}
		} else {
			for (int k : mapOfTextFile.keySet()) {
				if (mapOfTextFile.get(k).isEmpty()) {
					acceptStates.add(k);
				}
			}
		}

		try {
			if (inputFilePath.contains("/")) {
				inputFilePath = inputFilePath.substring(inputFilePath.lastIndexOf("/"));
			}

			BufferedWriter writer = new BufferedWriter(
					new FileWriter("out/" + inputFilePath.replace(".png", "_cmp.txt")));

			writer.write(Integer.toString(totalStates + 1));
			writer.newLine();

			Iterator<Integer> acceptStatesIterator = acceptStates.iterator();
			while (acceptStatesIterator.hasNext()) {
				writer.write(Integer.toString(acceptStatesIterator.next()));
				if (acceptStatesIterator.hasNext()) {
					writer.write(" ");
				}
			}
			writer.newLine();

			if (method == 2) {
				for (int k = 0; k < 4; k++) {
					writer.write("0 0 " + k);
					writer.newLine();
				}
			}

			for (Map.Entry<Integer, Map<Integer, Integer>> fromStateEntry : mapOfTextFile
					.entrySet()) {
				int fromState = fromStateEntry.getKey();
				Map<Integer, Integer> toStatesMap = fromStateEntry.getValue();

				for (Map.Entry<Integer, Integer> toStateEntry : toStatesMap.entrySet()) {
					int transition = toStateEntry.getKey();
					int toState = toStateEntry.getValue();

					writer.write(fromState + " " + toState + " " + transition);
					writer.newLine();
				}
			}

			if (method == 1) {
				int[] inclusiveQuads = ImageToTransitionAddresses.getinclusiveQuads();

				for (int k : acceptStates) {
					for (int i = 0; i < 3; i++) {
						writer.write(k + " " + k + " " + inclusiveQuads[i]);
						writer.newLine();
					}
				}
			}

			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}