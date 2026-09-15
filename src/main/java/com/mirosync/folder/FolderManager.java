package com.mirosync.folder;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FolderManager {
    // Default folder storage path
    private static final String DEFAULT_PATH =
            Path.of(
                    System.getProperty("user.home"),
                    "Mirosync"
            ).toString();
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
            Process process = new ProcessBuilder(
                    "attrib", "+h", pathAddress
            ).start();
            process.waitFor();
        }
        catch (InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void showFolder() {
        try {
            Process process = new ProcessBuilder(
                    "attrib", "-h", pathAddress
            ).inheritIO().start();
            int exitCode = process.waitFor();
            System.out.println("attrib exit code: " + exitCode);
        }
        catch (InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    // It checks whether the folder has been created at the specified path
    public boolean exists() {
        return Files.exists(Path.of(DEFAULT_PATH));
    }
}
