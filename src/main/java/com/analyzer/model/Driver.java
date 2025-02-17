package com.analyzer.model;

/**
 * main method/driver class for Analyzer program - initiates program with FileAnalysis instance
 * 
 * @author mcherry2
 * @version 3.0
 * @since 2024-11-16
 */
public class Driver {

    /**
     * main method for analyzer
     * @param args CL designated file to be analyzed
     */
    public static void main(String[] args) {

        //used for tracking program execution time
        long startTime = System.currentTimeMillis(); 
        long endTime;
        double executionDuration;


        FileAnalysis test = new FileAnalysis();
        test.validateAnalyzer(args);


        endTime = System.currentTimeMillis();
        executionDuration = (endTime - startTime) / 1000.0;

        System.out.println("Execution time: " + executionDuration + " seconds");
    }
}