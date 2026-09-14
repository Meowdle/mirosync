package com.mirosync;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FolderManager {
    // Default folder storage path
    private static final String DEFAULT_PATH = "C:/Users/Mirosync";
    private final String pathAddress;

    // Constructor for managing the selected file-saving path
    public FolderManager(String pathAddress) {
        // If a preferred address is not provided, use the default
        if (pathAddress == null || pathAddress.isEmpty()) {
            this.pathAddress = DEFAULT_PATH;
            return;
        }
        this.pathAddress = pathAddress;
    }

    public void createFolder() {
        // Path for creating a folder
        Path path = Paths.get(pathAddress);
        try {

            // Handling duplicate addresses during file creation
            if (Files.exists(path))
                throw new FileAlreadyExistsException("error");

            Files.createDirectory(path);
        }
        // Errors while creating folder
        catch (FileAlreadyExistsException e) {
            System.err.println("Folder already exists.");
        }
        catch (IOException e) {
            // TODO : Improved error handling during retries
            throw new RuntimeException(e);
        }
    }

    public void hideFolder() {
        try {
            new ProcessBuilder(
                    "attrib", "+h", pathAddress
            ).start();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void showFolder() {
        try {
            new ProcessBuilder(
                    "attrib", "-h", pathAddress
            ).start();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
