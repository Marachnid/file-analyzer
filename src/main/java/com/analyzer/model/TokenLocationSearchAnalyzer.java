package com.analyzer.model;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;


/**
 * searches file for matches with keywords and records their token positions
 * keywords are aquired by opening/reading config/search-tokens.txt (path is found via properties file)
 *      SEARCH tokens are CASE INSENSITIVE - search tokens are added as lowercase and processed tokens are .toLowerCase()
 * @author mcherry2
 * @version 4.0
 * @since 2025-2-17
 * @see TokenAnalyzer
 */
public class TokenLocationSearchAnalyzer implements TokenAnalyzer {

    //private instance variables
    private Properties properties;
    private Map<String, List<Integer>> foundLocations;
    private int currentTokenLocation;


    /**no arg constructor - initialize TreeMap and current location*/
    public TokenLocationSearchAnalyzer() {
        foundLocations = new TreeMap<>();
        currentTokenLocation = 0;
    }


    /**
     * constructor to set properties values and run subprocess to obtain search tokens
     * @param properties properties type from CLA properties file
     */
    public TokenLocationSearchAnalyzer(Properties properties) {
        this();
        this.properties = properties;
        openSearchFile(properties);     //obtain search tokens
    }


    /**
     * gives access to foundLocations
     * @return count of distinct tokens
     */
    public Map<String, List<Integer>> getFoundLocations() {
        return foundLocations;
    }


    /**
     * subprocess to open search file from properties
     * adds tokens found from search file into foundLocations instance map
     * @param properties properties
     */
    public final void openSearchFile(Properties properties) {

        //locate search tokens via classpath property
        String searchTokenPath = properties.getProperty("classpath.search.tokens");
        InputStream inputStream = this.getClass().getResourceAsStream(searchTokenPath);
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);


        try (BufferedReader reader = new BufferedReader(inputStreamReader)) {

            //retrieves tokens from file - enters values into instance map as keys
            retrieveSearchTokens(reader);

        } catch (FileNotFoundException fileNotFound) {
            System.out.println("Error finding Search Token file");
            // fileNotFound.printStackTrace();

        } catch (IOException exception) {
            System.out.println("Error reading Search Token file");
            // exception.printStackTrace();

        } catch (Exception exception) {
            System.out.println("Error processing Search Token file");
            // exception.printStackTrace();
        }
    }


    /**
     * adds values from search file into instance map as keys - initializes ArrayList for key values to be added later
     * @param reader reader object 
     * @throws IOException checked in parent function
     */
    public void retrieveSearchTokens(BufferedReader reader) throws IOException {

        while (reader.ready()) {
            String token = reader.readLine();

            //filters out empty tokens and adds tokens to foundLocations
            addSearchTokens(token);
        }
    }


    /**
     * adds tokens to foundLocations map as keys, initialize list for values
     * filters out blank values
     * @param token tokens passed in from search file
     */
    public void addSearchTokens(String token) {

            if (token.length() != 0) {
                token = token.toLowerCase();
                foundLocations.put(token, new ArrayList<>());
            }
    }


    /**
     * processes tokens if they are contained in foundLocations as a key - else does nothing
     * keeps a running count to track overall position in the input file
     * @param token token passed in from input file
     */
    @Override
    public void processToken(String token) {

        //increments before conditions to mark stepping into a new line
        currentTokenLocation++; 

        token = token.toLowerCase();

        //find matching tokens and add their position to matching key's list if so 
        if (foundLocations.containsKey(token)) {
            foundLocations.get(token).add(currentTokenLocation);
        }
    }


    /**
     * generate a formatted output file
     * @param inputFilePath input filepath
     */
    @Override
    public void generateOutputFile(String inputFilePath) {
        
        //reference properties for output directory and file name
        String directoryFilePath = properties.getProperty("output.directory");
        String outputFilePath = directoryFilePath + properties.getProperty("output.file.token.search.locations");
        

        try (PrintWriter print = new PrintWriter(new BufferedWriter(new FileWriter(outputFilePath)))) {

            //prints a formatted output
            writeTokenLocations(print);

        } catch (IOException exception) {
            System.out.println("Error processing search token output file");
            // exception.printStackTrace();

        } catch (Exception exception) {
            System.out.println("Error writing to search token output file");
            // exception.printStackTrace();
        }
    }


    /**
     * loops through map to print key value pairs
     * @param print printwriter object
     * @exception IOException already checked in parent function
     */
    public void writeTokenLocations(PrintWriter print) throws IOException {

        for (Map.Entry<String, List<Integer>> entry : foundLocations.entrySet()) {
            formatOutput(entry, print);
        }
    }
    

    /**
     * builds output string - creates string framework and includes foundPosition values if they exist
     * @param entry map of search keys and values
     * @param print printwriter object
     */
    public void formatOutput(Map.Entry<String, List<Integer>> entry, PrintWriter print) {

        StringBuilder output = new StringBuilder();

        output.append(entry.getKey());
        output.append(" =");

        print.println(output);
        print.println();

        //if search tokens are found/exist, append their values and a new line for space
        if (!entry.getValue().isEmpty()) {
            printFoundLocationValues(entry, print);
            print.println();
        }
    }


    /**
     * loops through foundLocation key value list and prints items as formatted strings
     * @param entry foundLocations map
     * @param print printwriter object
     */
    public void printFoundLocationValues(Map.Entry<String, List<Integer>> entry, PrintWriter print) {

        //reference column limit from properties file
        final String STRING_LIMIT = properties.getProperty("location.search.line.limit");
        final int LINE_LIMIT = Integer.parseInt(STRING_LIMIT);
        
        //key values of foundLocations
        final List<Integer> LOCATIONS = entry.getValue();
        

        //instantiate stringbuilder object for output
        StringBuilder output = new StringBuilder();

        //initialize formatting values
        int lineLengthTracker = 0;
        int loopCounter = 0;

        
        for (int item : LOCATIONS) {

            /*
                used for tracking newLine conditions for wordwrap
                >= instead of > : > was causing some lines to go to 81 characters with a space at the end
            */
            String currentValue = item + ", ";
            int currentValueLength = currentValue.length();
       

            //prints an empty line when length limit reached - returns updated lineLengthTracker
            lineLengthTracker = insertNewLine(LINE_LIMIT, lineLengthTracker, currentValueLength, print);

            //adds ", " to all entry items except the final value
            output = insertFoundPositions(LOCATIONS, item, loopCounter, print, output);


            //display output and update variables for next iteration
            print.print(output);
            output.setLength(0);

            loopCounter++; 
            lineLengthTracker += currentValueLength; //needs to be incremented after lineLengthTracker = insertNewLine()
        }

        //create some space for the next key/value output
        print.println();
    }


    /**
     * prints an empty line for key list values after exceeding line length limit
     * @param LINE_LIMIT line limit loaded from properties
     * @param lineLengthTracker running count of current length of current output string
     * @param print printwriter object
     * @return updated lineLengthTracker
     */
    public int insertNewLine(final int LINE_LIMIT, int lineLengthTracker, int currentValueLength, PrintWriter print) {

        int currentLineLength = currentValueLength + lineLengthTracker;

        if (currentLineLength >= LINE_LIMIT) {
            print.println();
            lineLengthTracker = 0;
        }
        return lineLengthTracker;
    }


    /**
     * formats found positions into strings with commas - avoids trailing comma
     * @param LOCATIONS integer list for found positions of map keys/search words
     * @param item iterator of foundLocations
     * @param loopCounter running loopcounter for position location
     * @param print printwriter object
     * @param output StringBuilder object
     * @return formatted string
     */
    public StringBuilder insertFoundPositions(final List<Integer> LOCATIONS, int item, int loopCounter,
            PrintWriter print, StringBuilder output) {
        
        output.append(String.valueOf(item));

        //adds commas to all values except final value
        if (loopCounter != (LOCATIONS.size() -1)) {
            output.append(", "); 
        }
        return output;
    }
}