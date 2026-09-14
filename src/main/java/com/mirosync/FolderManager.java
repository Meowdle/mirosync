package com.mirosync;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FolderManager {
    public void createFolder(String pathAddress) {
        Path path = Paths.get(pathAddress);
        try {
            Files.createDirectory(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
