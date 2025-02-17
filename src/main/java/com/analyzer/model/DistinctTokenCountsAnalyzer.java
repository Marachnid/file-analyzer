package com.analyzer.model;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;


/**
 * analyzes file to find distinct tokens and how often they occur
 * @author mcherry2
 * @version 4.0
 * @since 2025-2-17
 * @see TokenAnalyzer
 */
public class DistinctTokenCountsAnalyzer implements TokenAnalyzer {

    //private instance variables
    private Properties properties;
    private Map<String, Integer> distinctTokenCounts;


    /**empty constructor */
    public DistinctTokenCountsAnalyzer() {
        distinctTokenCounts = new TreeMap<>();
    }


    /**
     * create DistinctTokenCounts object and initialize distinctTokenCounts HashMap
     * @param properties properties type from CLA properties file
     */
    public DistinctTokenCountsAnalyzer(Properties properties) {
        this();
        this.properties = properties;
    }


    /**
     * gives access to distinctTokensCount
     * @return count of distinct tokens
     */
    public Map<String, Integer> getDistinctTokenCounts() {
        return distinctTokenCounts;
    }



    /**
     * processes tokens based on if they are unique or not - counts repeated occurences
     * @param token token passed in from input file
     */
    @Override
    public void processToken(String token) {

        //if the key already exists, update it's value
        if (distinctTokenCounts.containsKey(token)) {
            distinctTokenCounts.put(token, distinctTokenCounts.get(token) + 1);

        } else {
            //else add new token and set initial value to 1
            distinctTokenCounts.put(token, 1);
        }
    }


    /**
     * generate a formatted output file
     * @param inputFilePath input filepath
     */
    @Override
    public void generateOutputFile(String inputFilePath) {
        

        String directoryFilePath = properties.getProperty("output.directory");
        String outputFilePath = directoryFilePath + properties.getProperty("output.file.distinct.counts");
        

        try (PrintWriter print = new PrintWriter(new BufferedWriter(new FileWriter(outputFilePath)))) {

            //loop and write tokens
            writeDistinctTokenCounts(print);

            
        } catch (IOException exception) {
            System.out.println("Error processing summary file");
            // exception.printStackTrace();

        } catch (Exception exception) {
            System.out.println("Error writing to summary file");
            // exception.printStackTrace();
        }
    }


    /**
     * loops through map to print key and value pairs
     * @param print printwriter
     */
    public void writeDistinctTokenCounts(PrintWriter print) throws IOException {

        //updated to use a stringbuilder
        StringBuilder output = new StringBuilder();

        for (Map.Entry<String, Integer> entry : distinctTokenCounts.entrySet()) {

            output.append(entry.getKey());
            output.append("\t");
            output.append(entry.getValue());
            
            print.println(output.toString());
            output.setLength(0);
        }
    }
}