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
 */
@Controller
public class ResultServlet {

    private static final String OUTPUT_DIR = "src/main/output";

    /**
     * Process incoming file.
     * @param file file received from form
     * @param model Spring interface to pass data
     * @return results if successful, otherwise return to index
     */
    @PostMapping("/results")
    public String handleFileUpload(@RequestParam("file") MultipartFile file, Model model) {
        if (file.isEmpty()) {
            model.addAttribute("message", "Please select a file to upload");
            return "index";
        }

        Path uploadDirectory = null;

        try {
            // Get filepath of uploaded file
            uploadDirectory = Files.createTempDirectory("tempFiles");
            Path uploadFile = uploadDirectory.resolve(file.getOriginalFilename());
            file.transferTo(uploadFile);

            // Pass uploaded filepath to the main method of the file analyzer and initiate analysis
            String filePath = uploadFile.toString();
            Driver.main(new String[]{filePath});

            List<String> generatedFiles = collectGeneratedFiles();

            model.addAttribute("generatedFiles", generatedFiles);

            // Add a success message to the model
            model.addAttribute("message", "File analyzed successfully. Check the output files below.");

        } catch (IOException e) {
            e.printStackTrace();
            model.addAttribute("message", "An error occurred while processing the file");
            return "index";
        } finally {
            // Clean up the temporary directory
            if (uploadDirectory != null) {
                cleanUpTempDirectory(uploadDirectory);
            }
        }

        // Forward to the results page
        return "results";
    }


    //grabs the filenames of the generated output files
    private List<String> collectGeneratedFiles() throws IOException {
        List<String> filePaths = new ArrayList<>();
        Path outputDir = Paths.get(OUTPUT_DIR);

        if (Files.exists(outputDir)) {
            Files.list(outputDir).forEach(file -> {
                filePaths.add(file.getFileName().toString());
            });
        }

        return filePaths;
    }


    //cleans up temporary upload file and directory
    private void cleanUpTempDirectory(Path tempDir) {
        try {
            Files.walk(tempDir)
                .sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .forEach(file -> {
                    if (!file.delete()) {
                        file.deleteOnExit();
                    }
                });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //maps file downloads to output links
    @GetMapping("/download/{filename:.+}")
    public ResponseEntity<InputStreamResource> downloadFile(@PathVariable String filename) {
        try {
            Path file = Paths.get(OUTPUT_DIR).resolve(filename);
            InputStreamResource resource = new InputStreamResource(Files.newInputStream(file));

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}
