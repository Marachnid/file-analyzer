package java112.analyzer;
import java.io.*;
import java.util.*;
import java.io.File;
import java.net.URI;
import java.text.SimpleDateFormat;

/**
 * responsible for producing a summary output file for the analyzed file
 * formats file display and counts total tokens
 * 
 * @author mcherry2
 * @version 3.0
 * @since 2024-11-16
 * @see TokenAnalyzer
 */
public class FileSummaryAnalyzer implements TokenAnalyzer {

    //private instance variable
    private Properties properties;
    private int totalTokensCount;


    /**empty constructor*/
    public FileSummaryAnalyzer() {}


    /**
     * create a FileSummaryAnalyzer object w/properties
     * @param properties properties of analyzer
     */
    public FileSummaryAnalyzer(Properties properties) {
        this();
        this.properties = properties;
    }


    /**
     * returns totalTokens count
     * @return totalTokens parsed from user file
     */
    public int getTotalTokensCount() {
    return totalTokensCount;
    }


    /**
     * counts the total number of tokens - increments every time called
     * @param token token item passed from input file
     */
    @Override
    public void processToken(String token) {
        totalTokensCount++;
    }
    

    /**
     * writes formatted file analysis summary from input file data
     * @param inputFilePath user-entered input file to be analyzed
     */
    @Override
    public void generateOutputFile(String inputFilePath) {

        //reference hard-coded values from config/project2.prop
        String directoryFilePath = properties.getProperty("output.directory");
        String outputFilePath = directoryFilePath + properties.getProperty("output.file.summary");
        String applicationName = properties.getProperty("application.name");
        String authorName = properties.getProperty("author");
        String authorEmail = properties.getProperty("author.email.address");
        
        //file property variables
        File file = new File(inputFilePath);
        String absolutePath = file.getAbsolutePath();
        URI fileUri = file.toURI();
        
        //date/format variables
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
        Date now = new Date();

        
        try (PrintWriter print = new PrintWriter(new BufferedWriter(new FileWriter(outputFilePath)))) {
            
            //lastModifiedTime precision conversion to work with Date
            long lastModifiedTime = file.lastModified();
            Date lastModifiedDate = new Date(lastModifiedTime);

            //file size
            long fileByteSize = file.length();

            //format dates
            String formattedLastModifiedTime = dateFormat.format(lastModifiedDate);
            String formattedDateOfAnalysis = dateFormat.format(now);


            //file summary output
            print.println("Application Name: " + applicationName);
            print.println("Author: " + authorName);
            print.println("Author email: " + authorEmail);
            print.println("File Path: " + absolutePath);
            print.println("Date of analysis: " + formattedDateOfAnalysis);
            print.println("Last Modified: " + formattedLastModifiedTime);
            print.println("File Size (bytes): " + fileByteSize);
            print.println("File URI: " + fileUri);
            print.println("Total Tokens: " + totalTokensCount);


        } catch (IOException exception) {
            System.out.println("Error writing to summary file");
            exception.printStackTrace();

        } catch (Exception exception) {
            System.out.println("Error processing summary file");
            exception.printStackTrace();
        }
    }
}