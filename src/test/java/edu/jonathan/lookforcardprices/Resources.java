package edu.jonathan.lookforcardprices;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Resources {

    protected static final Logger logger = Logger.getLogger(Resources.class);

    public static String getContentFromResourceFile(String pathInsideResource){
        String mockContent = null;
        try {
            URI filePath = Resources.class.getResource(pathInsideResource).toURI();
            mockContent = new String(Files.readAllBytes(Paths.get( filePath ) ));
        } catch (IOException e) {
            logger.error(e);
        } catch (URISyntaxException e) {
            logger.error(e);
        }
        return mockContent;
    }
}
