# The Analyzer Project

This was a class project for my Advanced Java course.

The Analyzer is an MVC application that receives and analyzes the text and String tokens of a user-uploaded .txt file.
The analyzer will provide a:
    1. Identify and count every distinct token (word/string) found within the document
    2. Identify the largest tokens (of a minimum length set in analyzer.properties)
    3. Identify specific search tokens from a search file (resources/search-tokens.txt) and their location as they're found within the user-uploaded file
    4. List a basic summary of the uploaded file's information and data.

This project was originally exclusively operated through the command line and pre-existing files in the project directory but was updated to be an MVC web app for ease of use and to learn Spring-boot/Thyme.

With the updates I've made, I wanted users/viewers to be able to run this program with as little configuration/setup as necessary. This involved learning how to use and implement Spring-Boot and Thyme, as well as web-based file in/out operations
    1. I switched from using a local Tomcat installation and JSP format to using Spring-Boot with Thyme templates instead
    2. I added a web-based file management UI to elimenate the need for CLI/terminal operations necessary to run the program
    3. I converted the project from needing build.xml and web.xml files to maven pom.xml for build management
    4. Cut unnecessary project/build structure and configured the File Analyzer to be a standalone application from original Advanced Java course/project environment

INSTRUCTIONS
    Launch application
    1. Build application: mvn clean install
    2. Launch Spring-Boot: mvn spring-boot:run

    Analysis file
    1. Give script execution priveliges if needed: # chmod +x generateFile.sh
    2. Generate analysis file: ./generateFile.sh
    3. Download largeFile.txt from codespace
    4. (Optional) Select custom search words: edit resources/search-tokens.txt


Planned Updates:
    Implement a bootstrap framework for UI
    Convert commented out stacktrace's to log statements
    Add functionality to use form-entered search-tokens instead of internal search-tokens.txt file
    