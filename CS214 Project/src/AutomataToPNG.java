import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import javax.imageio.ImageIO;

/**
 * Class that preforms decompression
 */
public class AutomataToPNG {

  private ArrayList<Integer> outputNodes;
  private FiniteStateAutomaton fsm;
  private File file;

  /**
   * Constructor that creates AutomataToPNG object, then preforms decompression
   *
   * @param args arguments given from terminal
   */
  AutomataToPNG(String[] args) {
    try {
      ensureSingleBlankNewline(getFileName(args));
      file = new File(getFileName(args));
      outputNodes = outputNodesInitialisation(file);
      fsm = automataGenerator(file);
      System.out.println(fsm.getAllPermutations(0,outputNodes.get(0)));
      try {
        imageCreator(fsm, 0, outputNodes, getFileName(args));
      } catch (IOException e) {
        e.printStackTrace();
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }

  /**
   * This method creates an image from an automata, decompression
   *
   * @param fsm         Finite State Automata
   * @param input       Start state, typically zero
   * @param outputNodes All the different accept states
   * @param inputFile   Takes in the name of the inputFile from the argument in terminal
   * @throws IOException File not found
   */
  private void imageCreator(FiniteStateAutomaton fsm, int input,
      ArrayList<Integer> outputNodes, String inputFile) throws IOException {
    int n = resolution(fsm, 0, outputNodes);
    int imageSize = (int) Math.pow(2, n);

    // Create a white BufferedImage with the size calculated from the resolution
    BufferedImage image = new BufferedImage(imageSize, imageSize, BufferedImage.TYPE_BYTE_BINARY);
    Graphics2D graphics = image.createGraphics();
    graphics.setColor(Color.WHITE);
    graphics.fillRect(0, 0, imageSize, imageSize);

    graphics.setColor(Color.BLACK);

    for (int i : outputNodes) {
      for (List<Integer> j : fsm.getAllPermutations(input, i)) {
        int x = 0;
        int y = 0;
        int area = imageSize;

        for (int k : j) {
          area /= 2;
          switch (k) {
            case 0:
              // No change in x, y value is updated
              y += area;
              break;
            case 1:
              // No change needed for case 1, as x and y values remain the same
              break;
            case 2:
              // Update both x and y values
              x += area;
              y += area;
              break;
            case 3:
              // Update the x value
              x += area;
              break;
            default:
              System.out.print("");
              break;
          }
        }

        graphics.fillRect(x, y, area, area);
      }
    }
    graphics.dispose();
    if (inputFile.contains("/")) {
      inputFile = inputFile.substring(inputFile.lastIndexOf("/"));
    }
    String outputFileName = inputFile.replace(".txt", "_dec.png");
    File file = new File("out/" + outputFileName);
    ImageIO.write(image, "png", file);
  }

  /**
   * Calculates the resolution of the image using the formula 2^n *2^n
   *
   * @param fsm         Finite State Automata
   * @param input       Start state, typically zero
   * @param outputNodes All the different accept states so that the largest n value can be obtained
   * @return n = the longest set of coordinates
   */
  private int resolution(FiniteStateAutomaton fsm, int input,
      ArrayList<Integer> outputNodes) {
    int n = 0;

    for (int i : outputNodes) {
      for (List<Integer> j : fsm.getAllPermutations(input, i)) {
        if (n < j.size()) {
          n = j.size();
        }
      }

    }
    return n;
  }

  /**
   * Generates an automata from a given text file
   *
   * @param file file containing fromState, toState, transition
   * @return A finiteStateAutomata
   * @throws FileNotFoundException exception caught when there is no file
   */
  private FiniteStateAutomaton automataGenerator(File file) throws FileNotFoundException {
    Scanner scanner = new Scanner(file);
    scanner.nextLine();
    scanner.nextLine();
    FiniteStateAutomaton fsm = new FiniteStateAutomaton();
    //----------------------------------------------------------------------//
    while (scanner.hasNext()) {
      int fromState = scanner.nextInt();
      int toState = scanner.nextInt();
      int input = scanner.nextInt();
      fsm.addTransition(fromState, toState, input);
      scanner.nextLine();
    }
    //---------------------------------------------------------------------//

    scanner.close();
    return fsm;
  }

  /**
   * Creates an array list containing all the accepted states
   *
   * @param file file containing fromState,toState,input
   * @return array list of accepted states
   * @throws FileNotFoundException exception caught when no file is found
   */
  private ArrayList<Integer> outputNodesInitialisation(File file)
      throws FileNotFoundException {
    ArrayList<Integer> outputNodes = new ArrayList<>();
    Scanner scanner = new Scanner(file);
    scanner.nextLine();
    String outputs = scanner.nextLine().trim();
    for (String word : outputs.split("\\s+")) {
      outputNodes.add(Integer.parseInt(word));
    }

    return outputNodes;
  }

  /**
   * Gets the file name
   *
   * @param args arguments given
   * @return String containing file name
   */
  private String getFileName(String[] args) {
    if (args[2].equalsIgnoreCase("t")) {
      return args[4];
    } else {
      return args[3];
    }

  }

  /**
   * If the inputted text file doesn't end with a newline, add a newline to it If the inputted text
   * file ends with a newline, do nothing
   *
   * @param inputFileName name of file to be inputted
   */
  private void ensureSingleBlankNewline(String inputFileName) {
    try {
      List<String> lines = Files.readAllLines(Paths.get(inputFileName));

      // Remove all blank lines from the end
      while (lines.size() > 0 && (lines.get(lines.size() - 1).isEmpty()
          || lines.get(lines.size() - 1) == null)) {
        lines.remove(lines.size() - 1);
      }

      // Add one blank newline
      lines.add("");

      try (BufferedWriter writer = new BufferedWriter(new FileWriter(inputFileName))) {
        for (int i = 0; i < lines.size(); i++) {
          String line = lines.get(i);
          if (line != null) {
            writer.write(line);
          }
          if (i < lines.size() - 1) {
            writer.newLine();
          }
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}


