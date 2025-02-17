package com.analyzer.model;
import java.io.*;
import java.util.*;

/**
 * analyzes file for tokens of a predetermined length or larger indicated in properties file
 * @author mcherry2
 * @version 3.0
 * @since 2024-11-16
 * @see TokenAnalyzer
 */
public class LargestTokensAnalyzer implements TokenAnalyzer {

    //private instance variables
    private Properties properties;
    private Set<String> largestTokens;
    private int minimumTokenLength;

    /**empty constructor */
    public LargestTokensAnalyzer() {
        largestTokens = new TreeSet<>();
    }


    /**
     * create LargestTokens object w/ pre-loaded arguments for analysis
     * @param properties properties type from CLA
     */
    public LargestTokensAnalyzer(Properties properties) {
        this();
        this.properties = properties;
        String minimumTokenLengthString = properties.getProperty("largest.words.minimum.length");
        minimumTokenLength = Integer.parseInt(minimumTokenLengthString);
    }


    /**
     * get method for largestToken, not used right now
     * @return largestTokens HashSet
     */
    public Set<String> getLargestTokens() {
        return largestTokens;
    }


    /**
     * process tokens meeting properties parameters for min length
     * @param token token passed in from input file
     */
    @Override
    public void processToken(String token) {
        if (token.length() >= minimumTokenLength) {
            largestTokens.add(token);
        }
    }


    /**
     * generate a formatted output file
     * @param inputFilePath input file to be analyzed
     */
    @Override
    public void generateOutputFile(String inputFilePath) {

        //reference hard-coded values from properties file
        String directoryFilePath = properties.getProperty("output.directory");          //controls where file goes - output/ - in this case
        String outputFilePath = directoryFilePath + properties.getProperty("output.file.largest.words"); //combine directory path with output file name


        try (PrintWriter print = new PrintWriter(new BufferedWriter(new FileWriter(outputFilePath)))) {

            //loops through tokens and writes individually
            writeLargestTokens(print);
            
        } catch (IOException exception) {
            System.out.println("Error processing LargestTokens file");
            exception.printStackTrace();

        } catch (Exception exception) {
            System.out.println("Error writing to LargestTokens file");
            exception.printStackTrace();
        }
    }


    /**
     * loops and writes tokens one by one
     * @param print printwriter from parent function
     * @throws IOException already validated in parent function: generateOutputFile
     */
    public void writeLargestTokens(PrintWriter print) throws IOException {

        for (String token : largestTokens) {
            print.println(token);
        }
    }
}