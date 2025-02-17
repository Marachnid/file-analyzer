package com.analyzer.utilities; 

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


/**
 * This interface contains a default method that can be used anywhere a Properties
    * object is needed to be loaded.
    * @author Eric Knapp
    *
    */
public interface PropertiesLoader {

    /**
     * This default method will load a properties file into a Properties instance
        * and return it.
        * @param propertiesFilePath a path to a file on the java classpath list
        * @return a populated Properties instance or an empty Properties instance if
        * the file path was not found.
        */
    default Properties loadProperties(String propertiesFilePath){

        Properties properties = new Properties();

        try {

            //extra validation for incorrect/non-existant files returning as null instead of IO catching the error
            InputStream inputStream = this.getClass().getResourceAsStream(propertiesFilePath);

            if (inputStream == null) {

                System.out.println("Error finding file: " + propertiesFilePath);

            } else {

                properties.load(this.getClass().getResourceAsStream(propertiesFilePath));
            }


        } catch (IOException ioException) {

            ioException.printStackTrace();

        } catch (Exception exception) {

            exception.printStackTrace();
        }
        
        return properties;
    }
}