package com.analyzer.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.analyzer.model.Driver;

/**
 * Receives incoming file and sends it to the analyzer main method for analysis.
 * UI added to eliminate the need for running scripts in the terminal.
 * Returns a list of output files with data/analysis on the input file.
 * 
 * Web-based file management (upload/download) and spring-boot/thyme are newer topics to me,
 * collectGeneratedFiles(), removeUploadDirectory(), downloadFile() are a mix of tutorials, research, and AI
 * to learn, troubleshoot, and configure them to work with my application
 */
@Controller
public class ResultServlet {

    //used for collecting the generated files from analysis output directory
    String OUTPUT_DIRECTORY = "src/main/output";

    /**
     * analysis servlet that receives an incoming file to analyze
     * analyzes strings/tokens of input file and produces output files detailing insights
     * returns the output files as a list of downloadable items to be displayed in results.html
     * @param file file received from form
     * @param model Spring interface to pass data
     * @return results if successful, otherwise return index
     */
    @PostMapping("/results")
    public String analyzeUploadedFile(@RequestParam("file") MultipartFile file, Model model) {

        //quick check for file upload, redirects back to itself if file is empty
        if (file.isEmpty()) {return "index";}


        //initialize temporary directory for try/catch block
        Path uploadDirectory = null;

        //try catch block to create a temp directory and process input file
        try {
            uploadDirectory = Files.createTempDirectory("analysisFile");
            Path uploadFile = uploadDirectory.resolve(file.getOriginalFilename());
            file.transferTo(uploadFile);

            //pass the file to the main method of the Analysis Driver and analyze file
            String filePath = uploadFile.toString();
            Driver.main(new String[]{filePath});

            //searches project output directory and adds output filepaths to list
            List<String> generatedFiles = collectGeneratedFiles();

            //make list available to results page
            model.addAttribute("generatedFiles", generatedFiles);


        } catch (IOException exception) {
            exception.printStackTrace();
            return "index";
            
        } finally {
            //delete the temporary directory when finished
            if (uploadDirectory != null) {
                removeUploadDirectory(uploadDirectory);
            }
        }
        //forward to results if successfull
        return "results";
    }


    /**
     * scans project directory for output files and returns a list of filenames 
     * @return list of filePath strings
     * @throws IOException handled in parent try/catch
     */
    private List<String> collectGeneratedFiles() throws IOException {
        List<String> filePaths = new ArrayList<>();
        Path outputDir = Paths.get(OUTPUT_DIRECTORY);

        //check outputdirectory for files, if true: convert to string and add to list
        if (Files.exists(outputDir)) {
            Files.list(outputDir).forEach(item -> {
                filePaths.add(item.getFileName().toString());
            });
        }
        return filePaths;
    }


    /**
     * removes temporary directory and files after completing analysis
     * not as knowledgeable on this
     * @param uploadDirectory temporary directory for upload file
     */
    private void removeUploadDirectory(Path uploadDirectory) {
        try {
            Files.walk(uploadDirectory)             //generate stream of path objects
                .sorted(Comparator.reverseOrder())  //delete child files/path objects before parents
                .map(Path::toFile)                  //maps path objects to file objects to use delete() method
                .forEach(file -> {                  
                    if (!file.delete()) {           //designates file for deletion on exit 
                        file.deleteOnExit();
                    }
                });
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }


    /**
     * enables download of internal output/server files by browser/client
     * maps get request with analysis files URL patterns
     * not as knowledgeable on this
     */ 
    @GetMapping("/download/{filename:.+}")
    public ResponseEntity<InputStreamResource> downloadFile(@PathVariable String filename) {
        try {
            Path file = Paths.get(OUTPUT_DIRECTORY).resolve(filename);
            InputStreamResource resource = new InputStreamResource(Files.newInputStream(file));

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);
        } catch (IOException exception) {
            exception.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}
