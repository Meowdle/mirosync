package com.mirosync;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FolderManager {
    // Default folder storage path
    private final String DEFAULT_PATH = "C:/Users/Mirosync";

    public void createFolder(String pathAddress) {
        // If a preferred address is not provided, use the default.
        if (pathAddress == null || pathAddress.isEmpty())
            pathAddress = DEFAULT_PATH;
        // Path for creating a folder
        Path path = Paths.get(pathAddress);
        try {
            Files.createDirectory(path);
        } // Error while creating folder
        catch (IOException e) {
            // TODO : Improved error handling during retries
            throw new RuntimeException(e);
        }
    }
}
