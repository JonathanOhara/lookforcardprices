package edu.jonathan.lookforcardprices;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Resources {

    public static String getContentFromResourceFile(String pathInsideResource){
        String mockContent = null;
        try {
            URI filePath = Resources.class.getResource(pathInsideResource).toURI();
            mockContent = new String(Files.readAllBytes(Paths.get( filePath ) ));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return mockContent;
    }
}
