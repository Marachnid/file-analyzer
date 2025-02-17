package com.analyzer.model;

/**
 * public interface - defines abstract methods called by FileSummaryAnalyzer and DistinctTokensAnalyzer
 * 
 * @author mcherry2
 * @version 3.0
 * @since 2024-11-16
 * @see FileSummaryAnalyzer
 * @see DistinctTokensAnalyzer
 * @see DistinctTokenCountsAnalyzer
 * @see LargestTokensAnalyzer
 */
public interface TokenAnalyzer {

    /**
     * processes tokens passed in
     * @param token token from input file
     */
    public void processToken(String token);


    /**
     * generates an output file from input/output paths
     * @param inputFilePath user-entered filepath
     */
    public void generateOutputFile(String inputFilePath);
}