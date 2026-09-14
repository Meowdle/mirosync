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
            if (Files.exists(path))
                throw new FileAlreadyExistsException(
                        "File with this name already exist."
                );
            Files.createDirectory(path);
        } // Error while creating folder
        catch (IOException e) {
            // TODO : Improved error handling during retries
            throw new RuntimeException(e);
        }
    }
}
