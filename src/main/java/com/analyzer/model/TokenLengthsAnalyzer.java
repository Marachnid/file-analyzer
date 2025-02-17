package com.analyzer.model;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;


/**
 * analyzes file to track lengths of tokens and how often they occur
 * @author mcherry2
 * @version 3.0
 * @since 2024-11-16
 * @see TokenAnalyzer
 */
public class TokenLengthsAnalyzer implements TokenAnalyzer{

    //private instance variables
    private Properties properties;
    private Map<Integer, Integer> distinctTokenLengths;


    /**no arg constructor - initialize new tree map upon being called*/
    public TokenLengthsAnalyzer() {
        distinctTokenLengths = new TreeMap<>();
    }


    /**
     * constructor to set properties values
     * @param properties properties type from CLA properties file
     */
    public TokenLengthsAnalyzer(Properties properties) {
        this();
        this.properties = properties;
    }


    /**
     * gives access to distinctTokensLengths
     * @return count of distinct tokens
     */
    public Map<Integer, Integer> getTokenLengths() {
        return distinctTokenLengths;
    }


    /**
     * processes tokens based on if their length is unique and creates an entry - counts repeated occurences if not unique
     * the logic is a modification of DistinctTokenCounts to assess for token length instead of token value
     * @param token token passed in from input file
     */
    @Override
    public void processToken(String token) {

        //if token length already exists - target token key and increment value : put new token into map - init to 1
        if (distinctTokenLengths.containsKey(token.length())) {
            distinctTokenLengths.put(token.length(), distinctTokenLengths.get(token.length()) + 1);
        } else {
            distinctTokenLengths.put(token.length(), 1);
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
        String outputFilePath = directoryFilePath + properties.getProperty("output.file.token.lengths");        

        try (PrintWriter print = new PrintWriter(new BufferedWriter(new FileWriter(outputFilePath)))) {

            //prints map key/value pairs
            writeTokenLengths(print);

        } catch (IOException exception) {
            System.out.println("Error processing distinct token lengths file");
            exception.printStackTrace();

        } catch (Exception exception) {
            System.out.println("Error writing to distinct token lengths file");
            exception.printStackTrace();
        }
    }


    /**
     * loops through map to print key value pairs as a formatted string
     * @param print printwriter
     * @exception IOException already checked in parent function
     */
    public void writeTokenLengths(PrintWriter print) throws IOException {

        //intitialize a stringbuilder object
        StringBuilder output = new StringBuilder();
        

        for (Map.Entry<Integer, Integer> entry : distinctTokenLengths.entrySet()) {

            //build string by appending key, tab, and value
            output.append(entry.getKey());
            output.append("\t");
            output.append(entry.getValue());

            //print completed string, reset string for next key/value pair
            print.println(output.toString());
            output.setLength(0);
        }
    }
}
