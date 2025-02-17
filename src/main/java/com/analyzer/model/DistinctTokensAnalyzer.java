package com.analyzer.model;
import java.io.*;
import java.util.*;


/**
 * responsible for adding only unique tokens into a list and 
 * generating an output file containing all unique tokens
 * 
 * @author mcherry2
 * @version 3.0
 * @since 2024-11-16
 * @see TokenAnalyzer
 */
public class DistinctTokensAnalyzer implements TokenAnalyzer {

    //private instance variables
    private Set<String> distinctTokens;
    private Properties properties;


    /**empty constructor*/
    public DistinctTokensAnalyzer() {
        distinctTokens = new TreeSet<>();
    }
    
    
    /**
     * create a DistinctTokensAnalyzer object w/properties
     * @param properties properties of analyzer
     */
    public DistinctTokensAnalyzer(Properties properties) {
        this();
        this.properties = properties;
    }


    /**
     * returns distinct tokens set
     * @return distinctTokens
     */
    public Set<String> getDistinctTokens() {
        return distinctTokens;
    }


    /**
     * adds tokens to a Set list - only collects unique tokens
     * @param token passed from input file
     */
    @Override
    public void processToken(String token) {
        distinctTokens.add(token);
    }



    /**
     * generates an output file of only distinct tokens
     * @param inputFilepath user-designated file to be analyzed
     */
    @Override
    public void generateOutputFile(String inputFilepath) {

        //reference hard-coded values from properties file
        String directoryFilePath = properties.getProperty("output.directory");
        String outputFilePath = directoryFilePath + properties.getProperty("output.file.distinct");


        try (PrintWriter print = new PrintWriter(new BufferedWriter(new FileWriter(outputFilePath)))) {

            //loop through list of tokens and print individually
            writeDistinctTokens(print);

        } catch (IOException exception) {

            System.out.println("Error writing distinct tokens to file");
            exception.printStackTrace();

        } catch (Exception exception) {

            System.out.println("Error processing distinct token file");
            exception.printStackTrace();
        }
    }


    /**
     * loops through set list of distinct tokens and writes them on their own line
     * @param print BufferedReader object for input file
     * @throws IOException IO already validated in gnerateOutputFile
     */
    public void writeDistinctTokens(PrintWriter print) throws IOException {

        for (String token: distinctTokens) {
            print.println(token);
        }
    }
}