import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * This class represents an image created from transition addresses. It takes a
 * 2D ArrayList of Integers, an input file path, and a flag indicating whether
 * to use a GUI. The image is generated based on the transition addresses and
 * can be saved to a file or displayed in a GUI.
 */
public class TransitionAddressesToImage {
	private BufferedImage image;

	/**
	 * Constructs a TransitionAddressesToImage object, generates the image, and
	 * either saves it to a file or displays it in a GUI.
	 *
	 * @param transitionAddresses a 2D ArrayList of Integers representing the
	 * transition addresses
	 * @param inputFilePath the input file path to be used for naming the output
	 * image file
	 * @param gui an integer indicating whether to use a GUI (1) or not (0)
	 */
	TransitionAddressesToImage(ArrayList<ArrayList<Integer>> transitionAddresses,
			String inputFilePath, int gui) {
		int maxElements = 0;
		int rowWithMaxElementsIndex = -1;

		// Find the size of the largest row
		for (int i = 0; i < transitionAddresses.size(); i++) {
			ArrayList<Integer> currentList = transitionAddresses.get(i);
			if (currentList.size() > maxElements) {
				maxElements = currentList.size();
				rowWithMaxElementsIndex = i;
			}
		}

		// Calculate the size of the image based on the largest row
		int imageSize = (int) Math.pow(2, maxElements);
		image = new BufferedImage(imageSize, imageSize, BufferedImage.TYPE_BYTE_BINARY);
		Graphics2D graphicImage = image.createGraphics();

		// Set all pixels to white initially
		graphicImage.setColor(Color.white);
		graphicImage.fillRect(0, 0, imageSize, imageSize);

		// Draw each transition address on the image
		for (ArrayList<Integer> current : transitionAddresses) {

			int x = 0;
			int y = 0;
			int blockScale = imageSize;
			for (int address : current) {
				blockScale = blockScale / 2;

				// Calculate the position of the current block based on the
				// current address
				switch (address) {
				case 0:
					y = y + blockScale;
					break;

				case 1:
					break;

				case 2:
					y = y + blockScale;
					x = x + blockScale;
					break;

				case 3:
					x = x + blockScale;
					break;
				}
			}
			// Draw the current block on the image
			graphicImage.setColor(Color.black);
			graphicImage.fillRect(x, y, blockScale, blockScale);
		}
		graphicImage.dispose();

		// If the GUI flag is set to 0, save the image to a file
		if (gui == 0) {
			try {
				if (inputFilePath.contains("/")) {
					inputFilePath = inputFilePath.substring(inputFilePath.lastIndexOf("/"));
				}
				if (!ImageIO.write(image, "png",
						new File("out/" + inputFilePath.replace(".txt", "_dec.png")))) {
					throw new IOException("Error writing image to file");
				}
			} catch (IOException e) {
				System.err.println("Error: " + e.getMessage());
				System.exit(0);
			}
		} else {// display the image on a GUI
			displayImageInGUI();
		}
	}

	/**
	 * Displays the generated image in a GUI using a JFrame. The JFrame is
	 * titled "Decompressed Image" and will close when the user exits the
	 * window.
	 */
	private void displayImageInGUI() {
		JFrame frame = new JFrame("Decompressed Image");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(image.getWidth(), image.getHeight());

		JPanel panel = new JPanel() {
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(image, 0, 0, null);
			}
		};

		panel.setPreferredSize(new Dimension(image.getWidth(), image.getHeight()));
		frame.getContentPane().add(panel);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
}