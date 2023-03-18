




import org.w3c.dom.Node;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;

public class Main {
    //TODO LIST
    //TODO 1) Traverse through map storing inputs and deleting the inputs and start to end node
    //TODO 2) Using the information gained from the traversal, Create a png, easy
    //Phase 1 Complete
    //TODO 3) Phase 2

    public static void main(String[] args) throws Exception {
        //---------------------------------------------------------//
        /*                      Input Errors                       */
        //---------------------------------------------------------//
        InputValidation(args);
        //---------------------------------------------------------//
        /*                 Decompression Errors                    */
        //---------------------------------------------------------//

        //TODO 31 March

        //---------------------------------------------------------//
        /*                  Compression Errors                     */
        //---------------------------------------------------------//

        //TODO Phase 2

        //---------------------------------------------------------//
        /*                    Initialisation                       */
        //---------------------------------------------------------//
        File file = new File(args[3]);
        ArrayList<Integer> outputNodes = outputNodesInitialisation(file);
        FiniteStateAutomaton fsm = automataGenerator(file);
        System.out.println(fsm.getAllPermutations(0,4));
        List<List<Integer>> inputs = fsm.getAllPermutations(0,4);
        int pixelRatio = 0;
        for (int i = 0;i<inputs.size();i++){
            if (pixelRatio<inputs.get(i).size()){
            pixelRatio = inputs.get(i).size();}
        }
        pixelRatio = (int)Math.pow(2,pixelRatio);








        //---------------------------------------------------------//
        /*                     Decompression                       */
        //---------------------------------------------------------//





        

    }

    public static int[] modulus(int input1, int input2) {
        int row = (input1 % 4 + 4) % 4; // ensure row is between 0 and 3
        int col = (input2 % 4 + 4) % 4; // ensure col is between 0 and 3
        return new int[]{3 - col / 2, row / 2}; // compute row and col indexes
    }

    public static int sizeOfPNG(FiniteStateAutomaton fsm,ArrayList<Integer> outputNodes) {
        int size = Integer.MIN_VALUE;
        for (int j : outputNodes) {
            for (int k = 0;k<fsm.getAllPermutations(0,j).size();k++) {
                for (int i : fsm.getAllPermutations(0, j).get(k)) {
                    if (fsm.getAllPermutations(0,j).get(k).size()>size){
                        size =fsm.getAllPermutations(0,j).get(k).size();
                    }

                }
            }
        }
        return size;
    }

    public static FiniteStateAutomaton automataGenerator(File file) throws FileNotFoundException {
        Scanner scanner = new Scanner(file);
        int size = Integer.parseInt(scanner.nextLine());
        scanner.nextLine();
        FiniteStateAutomaton fsm = new FiniteStateAutomaton();
        //----------------------------------------------------------------------//
        while (scanner.hasNext()) {
            int fromState = scanner.nextInt();
            int toState = scanner.nextInt();
            int input = scanner.nextInt();
            fsm.addTransition(fromState,toState,input);
            scanner.nextLine();
        }
        //---------------------------------------------------------------------//


        scanner.close();
        return fsm;
    }
    public static void handleError(ErrorType type, String message) {
        switch (type) {

            //---------------------------------------------------------//
            /*                      Input Errors                       */
            //---------------------------------------------------------//


            //Incorrect number of arguments given
            case INVALID_ARGUMENTS -> System.err.println("Input Error - Invalid number of arguments.");

            //Argument is not of necessary type
            case INVALID_ARGUMENT_TYPE -> System.err.println("Input Error - Invalid argument type.");

            //If the GUI is not 0,1
            case INVALID_GUI -> System.err.println("Input Error - Invalid GUI.");

            //If the mode is not 1 or 2 then this error will display
            case INVALID_MODE -> System.err.println("Input Error - Invalid mode.");

            //If the multi-resolution flag is not in {f,t,F,T}
            case INVALID_MULTI_RESOLUTION_FLAG -> System.err.println("Input Error - Invalid multi-resolution flag.");

            //File is given, but file cannot be opened or does not exist
            case INVALID_FILE -> System.err.println("Input Error - Invalid file " + message);


            //---------------------------------------------------------//
            /*                  Decompression Errors                   */
            //---------------------------------------------------------//

            // Multi-resolution file is negative
            case INVALID_WORD_LENGTH -> System.out.println("Decompress Error - Invalid word length");

            // If the file contains invalid automaton formatting - strings
            // instead of integers
            case INVALID_FILE_FORMATTING -> System.out.println("Decompress Error - Invalid automaton formatting");

            // If an invalid accept state is given
            case INVALID_ACCEPT_STATE -> System.out.println("Decompress Error - Invalid accept state");

            // If a transition includes an invalid state number or alphabet character
            case INVALID_TRANSITION -> System.out.println("Decompress Error - Invalid transition");


            //---------------------------------------------------------//
            /*                  Compression Errors                     */
            //---------------------------------------------------------//

            // If the image given is not square pr contains pixel values other than
            // black or white
            case INVALID_IMAGE -> System.out.println("Compress Error - Invalid input image");

            // If the multi-resolution method given is not in {1,2,3}
            case INVALID_MULTI_RESOLUTION_METHOD ->
                    System.out.println("Compress Error - Invalid multi-resolution method");
            default -> System.err.println("Error: Unknown error occurred.");
        }
        System.exit(1);
    }

    public enum ErrorType {
        INVALID_ARGUMENTS,
        INVALID_ARGUMENT_TYPE,
        INVALID_GUI,
        INVALID_MODE,
        INVALID_MULTI_RESOLUTION_FLAG,
        INVALID_WORD_LENGTH,
        INVALID_FILE_FORMATTING,
        INVALID_ACCEPT_STATE,
        INVALID_TRANSITION,
        INVALID_IMAGE,
        INVALID_MULTI_RESOLUTION_METHOD,
        INVALID_FILE
    }

    public static void InputValidation(String[] args) {
        if (args.length > 4) {
            handleError(ErrorType.INVALID_ARGUMENTS, "");
            return;
        }

        try {
            int arg0 = Integer.parseInt(args[0]);
            int arg1 = Integer.parseInt(args[1]);

            if (arg0 != 0 && arg0 != 1) {
                handleError(ErrorType.INVALID_GUI, "");
                return;
            }

            if (arg1 != 1 && arg1 != 2) {
                handleError(ErrorType.INVALID_MODE, "");
                return;
            }

            if (!args[2].equalsIgnoreCase("f") && !args[2].equalsIgnoreCase("t")) {
                handleError(ErrorType.INVALID_MULTI_RESOLUTION_FLAG, "");
            }
        } catch (NumberFormatException e) {
            handleError(ErrorType.INVALID_ARGUMENT_TYPE, "");
        }
    }
    public static ArrayList<Integer> outputNodesInitialisation(File file) throws FileNotFoundException{
        ArrayList<Integer> outputNodes = new ArrayList<>();
        Scanner scanner = new Scanner(file);
        scanner.nextLine();
        String outputs = scanner.nextLine().trim();
        for (String word : outputs.split("\\s+")) {
            outputNodes.add(Integer.parseInt(word));
        }


        return outputNodes;
    }
}