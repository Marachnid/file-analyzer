# file-analyzer

This application receives an incoming .txt file and analyzes the file to find the total number of tokens,
unique tokens, token lengths, and general file information to create a series of output documents detailing
the analysis. 

A user interface was added to allow easier use of the application away from the command line. SpringBoot and Thyme 
were also used to create the web environment. Rather than relying on the user having Tomcat or other tools to run the program,
SpringBoot and Thyme are used to create a simplified environment.


# Running the application
To start SpringBoot with Maven, use: mvn spring-boot:run

To start SpringBoot if Maven isn't installed locally, or there are issues with using the mvn command, 
right-click and run java/utilities/SpringBootStartup.java

SpringBoot will open to port 8080