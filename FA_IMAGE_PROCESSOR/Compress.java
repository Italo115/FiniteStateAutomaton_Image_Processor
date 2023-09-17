import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;

/**
 * This class provides compression and decompression functionality for images
 * using finite automata.
 */
public class Compress {

	/**
	 * The main entry point for the compression and decompression process.
	 * Accepts command line arguments to specify the operation mode and options.
	 *
	 * @param args Command line arguments for specifying the operation mode,
	 * options, and input file.
	 * @throws IOException If an error occurs while reading or writing files.
	 */
	public static void main(String[] args) throws IOException {
		validateArguments(args);
		String inputFilePath = args[args.length - 1];

		if (Integer.parseInt(args[1]) == 1) {// Decompression

			TextFileToTransitionAddresses transitionAddresses = new TextFileToTransitionAddresses(
					args);
			new TransitionAddressesToImage(transitionAddresses.getTransitionAddresses(),
					inputFilePath, Integer.parseInt(args[0]));
		} else {// Compression

			BufferedImage image = ImageIO.read(new File(inputFilePath));
			ImageToTransitionAddresses transitionAddresses = new ImageToTransitionAddresses(image,
					args);
			new TransitionAddressesToTextFile(transitionAddresses.getTransitionAddresses(),
					inputFilePath, args);
		}
	}

	/**
	 * Validates the input arguments for the program. This method checks the
	 * input arguments for various conditions, such as the correct number of
	 * arguments, valid file paths, valid GUI arguments, mode, word length, and
	 * others. It also checks if the input image meets the required conditions
	 * for compression. If the input arguments do not meet the requirements, an
	 * error message is displayed, and the program exits.
	 *
	 * @param args An array of Strings containing the command line arguments.
	 */
	private static void validateArguments(String[] args) {
		int wordLength = 1;
		String flag = " ";

		flag = args[2];
		if (flag.equalsIgnoreCase("f")) {
			if (args.length != 4) {
				System.err.println("Input Error - Invalid number of arguments");
				System.exit(0);
			}
		} else {
			if (args.length != 5) {
				System.err.println("Input Error - Invalid number of arguments");
				System.exit(0);
			}
			try {
				flag = args[2];
				wordLength = Integer.parseInt(args[3]);
			} catch (NumberFormatException e) {
				System.err.println("Input Error - Invalid argument type");
				System.exit(0);
			}
		}

		try { // INPUT ERRORS
			String filePath = args[args.length - 1];
			int gui = Integer.parseInt(args[0]);
			int mode = Integer.parseInt(args[1]);

			if (gui < 0 || gui > 1) {
				System.err.println("Input Error - Invalid GUI argument");
				System.exit(0);
			}

			if (mode < 1 || mode > 2) {
				System.err.println("Input Error - Invalid mode");
				System.exit(0);
			}

			if (!flag.matches("[FfTt]")) {
				System.err.println("Input Error - Invalid multi-resolution flag");
				System.exit(0);
			}

			File file = new File(filePath);
			if (!file.exists() || !file.isFile()) {
				System.err.println("Input Error - Invalid or missing file");
				System.exit(0);
			}

			if (mode == 1) {// DECOMPRESSION ERRORS

				if (wordLength < 0) {
					System.err.println("Decompress Error - Invalid word length");
					System.exit(0);
				}

				String acceptStates = "";
				String numOfStates = "";
				String line = "";
				ArrayList<String> transitions = new ArrayList<>();

				try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
					// Check the first line
					numOfStates = reader.readLine();
					if (numOfStates == null || !numOfStates.matches("^\\d+$")) {
						System.err.println("Decompress Error - Invalid automaton formatting");
						System.exit(0);
					}
					// Check the second line
					acceptStates = reader.readLine();
					acceptStates = acceptStates.trim();
					if (acceptStates == null || !acceptStates.matches("^\\d+(\\s+\\d+)*$")) {
						System.err.println("Decompress Error - Invalid automaton formatting");
						System.exit(0);
					}
					// Check the remaining lines
					while ((line = reader.readLine()) != null) {
						line = line.trim();
						transitions.add(line);
						if (!line.matches("^\\d+\\s+\\d+\\s+\\d+$")) {
							System.err.println("Decompress Error - Invalid automaton formatting");
							System.exit(0);
						}
					}

				} catch (Exception e) {
					System.err.println("Decompress Error - Invalid automaton formatting");
					System.exit(0);
				}

				String[] listOfAcceptStates = acceptStates.split(" ");
				for (String numberStr : listOfAcceptStates) {
					int number = Integer.parseInt(numberStr);
					if (number < 1 || number > Integer.parseInt(numOfStates)) {
						System.err.println("Decompress Error - Invalid accept state");
						System.exit(0);
					}
				}

				for (String transition : transitions) {
					if (transition != null) {
						String[] numbers = transition.split(" ");

						int num1 = Integer.parseInt(numbers[0]);
						int num2 = Integer.parseInt(numbers[1]);
						int num3 = Integer.parseInt(numbers[2]);
						if (num1 < 0 || num1 > Integer.parseInt(numOfStates) - 1 || num2 < 0
								|| num2 > Integer.parseInt(numOfStates) - 1) {
							System.err.println("Decompress Error - Invalid transition");
							System.exit(0);
						}

						if (num3 < 0 || num3 > 3) {
							System.err.println("Decompress Error - Invalid transition");
							System.exit(0);
						}
					}
				}

			} else {// Compression validation
				try {
					BufferedImage image = ImageIO.read(new File(filePath));
					if (image == null) {
						System.err.println("Input Error - Invalid or missing file");
						System.exit(0);
					}
					int width = image.getWidth();
					int height = image.getHeight();

					// Check if width and height are equal and height is a power
					// of 2
					if (width != height || (height & (height - 1)) != 0) {
						System.err.println("Compress Error - Invalid input image");
						System.exit(0);
					}

					for (int y = 0; y < height; y++) {
						for (int x = 0; x < width; x++) {
							int color = image.getRGB(x, y) & 0xFFFFFF;
							if (color != 0 && color != 0xFFFFFF) {
								System.err.println("Compress Error - Invalid input image");
								System.exit(0);
							}
						}
					}
				} catch (IOException e) {
					System.err.println("Input Error - Invalid or missing file");
					System.exit(0);
				}

				if (wordLength < 0 || wordLength > 3) {
					System.err.println("Decompress Error - Invalid word length");
					System.exit(0);
				}
			}
		} catch (NumberFormatException e) {
			System.err.println("Input Error - Invalid argument type");
			System.exit(0);
		}
	}
}