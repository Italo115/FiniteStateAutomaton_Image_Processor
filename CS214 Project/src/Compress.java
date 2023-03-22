import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;


/**
 * Main class for compressions
 */
public class Compress {

  /**
   * It is a main method that preforms: Decompression Compression Multi-resolution
   *
   * @param args takes in command args
   */
  public static void main(String[] args) {
    validateArguments(args);
    if (Integer.parseInt(args[1]) == 1) {
      new AutomataToPNG(args);


    } else {
      try {

        BufferedImage image = ImageIO.read(new File(getFileName(args)));
        List<List<Integer>> inputs = new ArrayList<>();
        findAllBlackQuadrants(image, new ArrayList<>(), inputs);
        System.out.println(inputs);

      }catch (IOException e){
        e.printStackTrace();
      }

    }


  }
  private void createTxtFile(List<List<Integer>> inputs){
    int states = 1;
    int temp = 0;
    //TODO







  }

  private static void findAllBlackQuadrants(BufferedImage image, List<Integer> currentCoords, List<List<Integer>> coordinates) {
    if (image.getWidth() == 1 && image.getHeight() == 1) {
      if (image.getRGB(0, 0) == 0xFF000000) { // Black pixel
        coordinates.add(new ArrayList<>(currentCoords));
      }
      return;
    }

    if (allPixelsBlack(image)) {
      coordinates.add(new ArrayList<>(currentCoords));
      return;
    }

    Image[] quadrants = divideAndConquer(image, 0, 0);
    for (int i = 0; i < quadrants.length; i++) {
      BufferedImage subImage = (BufferedImage) quadrants[i];
      List<Integer> newCoords = new ArrayList<>(currentCoords);
      newCoords.add(i);
      findAllBlackQuadrants(subImage, newCoords, coordinates);
    }
  }

  private static boolean allPixelsBlack(BufferedImage image) {
    for (int x = 0; x < image.getWidth(); x++) {
      for (int y = 0; y < image.getHeight(); y++) {
        if (image.getRGB(x, y) != 0xFF000000) { // Not a black pixel
          return false;
        }
      }
    }
    return true;
  }

  private static Image[] divideAndConquer(BufferedImage image, int x, int y) {
    Image[] quadrants = new Image[4];
    quadrants[0] = image.getSubimage(x, y + image.getHeight() / 2, image.getWidth() / 2, image.getHeight() / 2);
    quadrants[1] = image.getSubimage(x, y, image.getWidth() / 2, image.getHeight() / 2);
    quadrants[3] = image.getSubimage(x + image.getWidth() / 2, y, image.getWidth() / 2, image.getHeight() / 2);
    quadrants[2] = image.getSubimage(x + image.getWidth() / 2, y + image.getHeight() / 2, image.getWidth() / 2, image.getHeight() / 2);
    return quadrants;
  }




  /**
   * Gets the name of the file
   *
   * @param args argument containing the file
   * @return file name
   */
  public static String getFileName(String[] args) {
    String fileName = args[3];
    if (args[2].equalsIgnoreCase("t")) {
      fileName = args[4];
    }
    return fileName;
  }


  /**
   * Validates the input arguments given
   *
   * @param args arguments to be validated
   * @throws IllegalArgumentException if the number or type of arguments are incorrect
   */
  public static void validateArguments(String[] args) throws IllegalArgumentException {
    if (args.length < 4 || args.length > 5) {
      System.err.println("Input Error - Invalid number of arguments");
      System.exit(0);
    }
    String gui = args[0];
    if (gui.length() > 1) {
      System.err.println("Input Error - Invalid argument type");
      System.exit(0);
    } else if (!Character.isDigit(gui.charAt(0))) {
      System.err.println("Input Error - Invalid argument type");
      System.exit(0);
    } else if (Integer.parseInt(args[0]) != 0 && Integer.parseInt(args[0]) != 1) {
      System.err.println("Input Error - Invalid GUI argument");
      System.exit(0);
    }
    String mode = args[1];
    if (mode.length() > 1) {
      System.err.println("Input Error - Invalid mode");
      System.exit(0);
    } else if (!Character.isDigit(mode.charAt(0))) {
      System.err.println("Input Error - Invalid argument type");
      System.exit(0);
    } else if (Integer.parseInt(args[1]) != 1 && Integer.parseInt(args[1]) != 2) {
      System.err.println("Input Error - Invalid mode");
      System.exit(0);
    }

    String multiResFlag = args[2].toLowerCase();
    if (Character.isDigit(multiResFlag.charAt(0))) {
      System.err.println("Input Error - Invalid argument type");
      System.exit(0);
    }
    if (!multiResFlag.equalsIgnoreCase("f") && !multiResFlag.equalsIgnoreCase("t")) {
      System.err.println("Input Error - Invalid multi-resolution flag");
      System.exit(0);
    }

    File inputFile = new File(args[3]);
    if (!inputFile.exists() || !inputFile.isFile()) {
      System.err.println("Input Error - Invalid or missing file");
      System.exit(0);
    }

    if (Integer.parseInt(args[1]) == 1) {
      String textFileName = args[3];
      if (args[2].equalsIgnoreCase("t")) {
        textFileName = args[4];
      }

      File file = new File(textFileName);
      validateDecompressionErrors(file);
      // Decompression validations
      // Add further validation for decompression errors (7, 8, 9, 10) here as required
    } else {
      String pictureFileName = args[3];
      if (args[2].equalsIgnoreCase("t")) {
        pictureFileName = args[4];
      }
      File file = new File(pictureFileName);
      validateCompressionErrors(file, 2);
      System.out.print("");
      // Compression validations
      // Add further validation for compression errors (11, 12) here as required
    }
  }

  /**
   * Validates the given arguments for compression
   *
   * @param file   png containing the information necessary to produce automata
   * @param method Choosing the  method
   */
  public static void validateCompressionErrors(File file, int method) {
    try {
      BufferedImage image = ImageIO.read(file);

      if (image == null) {
        System.err.println("Compress Error - Invalid input image");
        System.exit(0);
      }

      int width = image.getWidth();
      int height = image.getHeight();

      if (width != height) {
        System.err.println("Compress Error - Invalid input image");
        System.exit(0);
      }

      for (int x = 0; x < width; x++) {
        for (int y = 0; y < height; y++) {
          int pixel = image.getRGB(x, y);
          if (pixel != 0xFF000000 && pixel != 0xFFFFFFFF) {
            System.err.println("Compress Error - Invalid input image");
            System.exit(0);
          }
        }
      }

      if (method < 1 || method > 3) {
        System.err.println("Compress Error - Invalid multi-resolution method");
        System.exit(0);
      }

    } catch (IOException e) {
      System.err.println("Compress Error - Cannot read image");
      System.exit(0);
    }
  }

  /**
   * Validates the decompression file and its integrity
   *
   * @param file containing integers of automata
   */
  public static void validateDecompressionErrors(File file) {
    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
      String line;
      int lineNumber = 0;
      int numberOfStates = -1;
      int[] acceptStates = null;

      while ((line = reader.readLine()) != null) {
        lineNumber++;

        if (lineNumber == 1) {
          numberOfStates = Integer.parseInt(line);
          if (numberOfStates <= 0) {
            System.err.println("Decompress Error - Invalid number of states");
            System.exit(0);
          }
        } else if (lineNumber == 2) {
          acceptStates = Arrays.stream(line.split(" ")).
              mapToInt(Integer::parseInt).toArray();
          for (int acceptState : acceptStates) {
            if (acceptState < 0 || acceptState >= numberOfStates) {
              System.err.println("Decompress Error - Invalid accept state");
              System.exit(0);
            }
          }
        } else {
          String[] transitionParts = line.split(" ");
          if (transitionParts.length != 3) {
            System.err.println("Decompress Error - Invalid automaton formatting");
            System.exit(0);
          }

          int originState = Integer.parseInt(transitionParts[0]);
          int destinationState = Integer.parseInt(transitionParts[1]);
          char alphabetCharacter = transitionParts[2].charAt(0);

          if (originState < 0 || originState >= numberOfStates || destinationState < 0
              || destinationState >= numberOfStates) {
            System.err.println("Decompress Error - Invalid transition");
            System.exit(0);
          }

          if (!Character.isDigit(alphabetCharacter)) {
            System.err.println("Decompress Error - Invalid alphabet character");
            System.exit(0);
          }
        }
      }
    } catch (NumberFormatException e) {
      System.err.println("Decompress Error - Invalid automaton formatting");
      System.exit(0);
    } catch (IOException e) {
      System.err.println("Decompress Error - Cannot read file");
      System.exit(0);
    }
  }
}
