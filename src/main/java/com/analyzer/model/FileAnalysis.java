package java112.analyzer;
import java112.utilities.PropertiesLoader;
import java.io.*;
import java.util.*;

/**
 * primary processing class of analyzer
 * calls supporting methods and classes to analyze files for distinct tokens and total tokens,
 * calls methods to create and write to a summary file and a file containing all unique tokens
 * 
 * @author mcherry2
 * @version 3.0
 * @since 2024-11-16
 * @see PropertiesLoader
 * @see FileSummaryAnalyzer
 * @see DistinctTokensAnalyzer
 * @see DistinctTokenCountsAnalyzer
 * @see LargestTokensAnalyzer
 * @see TokenLengthsAnalyzer
 * @see TokenLocationSearchAnalyzer
 */
public class FileAnalysis implements PropertiesLoader {

    //instance variables
    private Set<TokenAnalyzer> analyzers;


    /**
     * validate number of arguments coming in
     * @param arguments CLA for input file and properties file paths
     */
    public void validateAnalyzer(String[] arguments) {

        final int ALLOWED_ARGUMENTS = 2;

        // initiate file processing if argument count is valid
        if (arguments.length != ALLOWED_ARGUMENTS) {
            System.out.println("Please enter the file name/path to be analyzed");

        } else {
            analyze(arguments);
            System.out.println("Process Concluded");
        }
    }


    /**
     * primary controller for executing analysis
     * @param arguments CLA
     */
    public void analyze(String[] arguments) {

        String inputFilePath = arguments[0];

        //load and assign properties for validation 
        Properties properties = new Properties();
        properties = loadProperties(arguments[1]);

        createInstance(properties);         //add instances to HashSet<TokenAnalyzer> for loop execution
        openInputFile(inputFilePath);       
        writeOutputFiles(inputFilePath);
    }

    
    /**
     * instantiate Set list of analyzer objects - add new instances to list
     * @see FileSummaryAnalyzer
     * @see DistinctTokensAnalyzer
     * @see LargestTokensAnalyzer
     * @see DistinctTokenCountsAnalyzer
     * @param properties properties type retreived from CLA
     */
    public void createInstance(Properties properties) {
        analyzers = new HashSet<TokenAnalyzer>();
        analyzers.add(new FileSummaryAnalyzer(properties));
        analyzers.add(new DistinctTokensAnalyzer(properties));
        analyzers.add(new LargestTokensAnalyzer(properties));
        analyzers.add(new DistinctTokenCountsAnalyzer(properties));
        analyzers.add(new TokenLengthsAnalyzer(properties));
        analyzers.add(new TokenLocationSearchAnalyzer(properties));
    }
    

    /**
     * opens input file to send data to analyzers
     * @param inputFilePath input file retrieved from CLA
     */
    public void openInputFile(String inputFilePath) {

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFilePath))) {

            //loop through input file
            readInputFile(reader);

        } catch (FileNotFoundException fileNotFound) {
            System.out.println("Error finding file");
            fileNotFound.printStackTrace();

        } catch (IOException exception) {
            System.out.println("Error opening file");
            exception.printStackTrace();

        } catch (Exception exception) {
            System.out.println("Error processing file");
            exception.printStackTrace();
        }
    }


    /**
     * loops through input file - passes tokens to processTokens() one at a time - parses out non-word characters
     * @param reader reader object from openInputFile()
     * @throws IOException IO is already checked in parent function: openInputFile()
     */
    public void readInputFile(BufferedReader reader) throws IOException {

        while (reader.ready()) {

            String fileLine = reader.readLine();
            String[] tokens = fileLine.split("\\W");        //parse out non-word characters here

            //pass tokens of String[] indiviually
            passTokens(tokens);
        }
    }


    /**
     * passes tokens one-by-one from their string array to analyzers
     * filters out empty strings
     * @param tokens tokens from parent function: readInputFile()
     */
    public void passTokens (String[] tokens) {

        for (String token : tokens) {

            //filter out empty tokens if encountered
            if (token.length() > 0) {

                //passes tokens to each analyzer individually via loop through analyzer list
                initiateProcessTokens(token); 
            }
        }
    }


    /**
     * loop analyzers and generate output
     * @param inputFilePath input file retrieved via CLA
     */
    public void writeOutputFiles(String inputFilePath) {
        for (TokenAnalyzer analyzer : analyzers) {
            analyzer.generateOutputFile(inputFilePath); 
        }
    }


    /**
     * loops through analyzer objects and passes tokens
     * @param token tokens from input file
     */
    public void initiateProcessTokens(String token) {
        for (TokenAnalyzer analyzer : analyzers) {
            analyzer.processToken(token);
        }
    }
}