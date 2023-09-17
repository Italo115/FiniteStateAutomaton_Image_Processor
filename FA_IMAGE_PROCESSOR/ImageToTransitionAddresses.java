import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is responsible for converting a black and white image into a list
 * of transition addresses. Each transition address represents the path from the
 * top-left corner of the image to a black pixel.
 */
public class ImageToTransitionAddresses {
	private ArrayList<ArrayList<Integer>> transitionAddresses;
	private static int[] inclusiveQuads;

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
	 * Returns the inclusive quadrants array.
	 *
	 * @return An int[] representing the inclusive quadrants.
	 */
	public static int[] getinclusiveQuads() {
		return inclusiveQuads;

	}

	/**
	 * Constructs a new ImageToTransitionAddresses object. It initializes the
	 * required data structures and extracts the transition addresses from the
	 * input image.
	 *
	 * @param image The input BufferedImage containing the image data.
	 */
	ImageToTransitionAddresses(BufferedImage image, String[] args) {
		int imageSize = image.getHeight();
		List<String> addressStrings = new ArrayList<>();
		transitionAddresses = new ArrayList<>();

		if (args.length == 5) {
			inclusiveQuads = findQuadrantsExceptLightest(image);
		}

		// Extract transition addresses from the image
		extractAddresses(image, 0, 0, imageSize, "", addressStrings);

		// Convert the address strings to lists of integers and add them to the
		// list of transition addresses
		for (String address : addressStrings) {
			ArrayList<Integer> intAddress = new ArrayList<>();

			// Convert each character in the address string to an integer and
			// add it to the intAddress list
			for (char c : address.toCharArray()) {
				intAddress.add(Character.getNumericValue(c));
			}
			transitionAddresses.add(intAddress);
		}
	}

	/**
	 * Finds and returns the indices of the quadrants in the image except for
	 * the one with the lightest (minimum) black pixel count.
	 *
	 * @param image The BufferedImage to process.
	 * @return An int[] containing the indices of the quadrants except for the
	 * lightest one.
	 */
	public static int[] findQuadrantsExceptLightest(BufferedImage image) {
		int width = image.getWidth();
		int height = image.getHeight();

		int halfWidth = width / 2;
		int halfHeight = height / 2;

		// Define the 4 quadrants of the image
		int[][] quadrants = { { 0, halfHeight, halfWidth, height }, // bottom
																	// left
				{ 0, 0, halfWidth, halfHeight }, // top left
				{ halfWidth, halfHeight, width, height }, // bottom right
				{ halfWidth, 0, width, halfHeight } // top right
		};

		int[] blackPixelCounts = new int[4];

		// Iterate through the quadrants and count black pixels
		for (int quadrantIndex = 0; quadrantIndex < quadrants.length; quadrantIndex++) {
			int[] quadrant = quadrants[quadrantIndex];

			for (int x = quadrant[0]; x < quadrant[2]; x++) {
				for (int y = quadrant[1]; y < quadrant[3]; y++) {
					// Check if the pixel is black and increment the count if it
					// is
					if (image.getRGB(x, y) == 0xFF000000) {
						blackPixelCounts[quadrantIndex]++;
					}
				}
			}
		}

		int minBlackPixelsIndex = 0;
		int minBlackPixels = blackPixelCounts[0];

		// Find the index of the quadrant with the minimum black pixels
		for (int i = 1; i < blackPixelCounts.length; i++) {
			if (blackPixelCounts[i] < minBlackPixels) {
				minBlackPixels = blackPixelCounts[i];
				minBlackPixelsIndex = i;
			}
		}

		// Create a list to store the indices of the other quadrants (not the
		// lightest one)
		ArrayList<Integer> otherQuadrantsList = new ArrayList<>();
		for (int i = 0; i < blackPixelCounts.length; i++) {
			if (i != minBlackPixelsIndex) {
				otherQuadrantsList.add(i);
			}
		}

		// Convert the list of other quadrants to an int[] array
		int[] otherQuadrants = new int[otherQuadrantsList.size()];
		for (int i = 0; i < otherQuadrantsList.size(); i++) {
			otherQuadrants[i] = otherQuadrantsList.get(i);
		}

		return otherQuadrants;
	}

	/**
	 * Recursively extracts transition addresses from the given image and stores
	 * them in the provided list. It divides the image into quadrants and checks
	 * if the quadrant is black. If it is, it adds the current path to the
	 * address list.
	 *
	 * @param image The input BufferedImage containing the image data.
	 * @param x The x-coordinate of the top-left corner of the current quadrant.
	 * @param y The y-coordinate of the top-left corner of the current quadrant.
	 * @param quadrantSize The size of the current quadrant.
	 * @param path The current path to the quadrant.
	 * @param addressStrings The list of transition addresses to be updated.
	 */
	public static void extractAddresses(BufferedImage image, int x, int y, int quadrantSize,
			String path, List<String> addressStrings) {
		// If the current quadrant is black, add the path to the list of address
		// strings
		if (isQuadrantBlack(image, x, y, quadrantSize)) {
			addressStrings.add(path);
			return;
		} else if (quadrantSize == 1) {
			// If the quadrant size is 1, no further division is possible, so
			// return
			return;
		}

		// Divide the quadrant size by 2 for further processing
		quadrantSize /= 2;

		// Recursively call the extractAddresses method for each of the four new
		// quadrants
		extractAddresses(image, x, y + quadrantSize, quadrantSize, path + "0", addressStrings);
		extractAddresses(image, x, y, quadrantSize, path + "1", addressStrings);
		extractAddresses(image, x + quadrantSize, y + quadrantSize, quadrantSize, path + "2",
				addressStrings);
		extractAddresses(image, x + quadrantSize, y, quadrantSize, path + "3", addressStrings);
	}

	/**
	 * Checks if a quadrant of the given image is entirely black.
	 *
	 * @param image The input BufferedImage containing the image data.
	 * @param x The x-coordinate of the top-left corner of the quadrant.
	 * @param y The y-coordinate of the top-left corner of the quadrant.
	 * @param quadrantSize The size of the quadrant to be checked.
	 * @return true if the quadrant is entirely black, false otherwise.
	 */
	public static boolean isQuadrantBlack(BufferedImage image, int x, int y, int quadrantSize) {
		int white = 0xFFFFFF;

		// Iterate through each pixel in the quadrant
		for (int i = x; i < x + quadrantSize; i++) {
			for (int j = y; j < y + quadrantSize; j++) {
				// If a white pixel is found, return false, as the quadrant is
				// not entirely black
				if ((image.getRGB(i, j) & 0xFFFFFF) == white) {
					return false;
				}
			}
		}
		// If no white pixels are found, the quadrant is entirely black, so
		// return true
		return true;
	}
}