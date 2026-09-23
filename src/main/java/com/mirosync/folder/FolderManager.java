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

    private final VaultCamouflage camouflage = new VaultCamouflage();

    private String getVaultPath() {
        return pathAddress + "/" + camouflage.getCamouflagedName();
    }

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
        Path path = Paths.get(getVaultPath());
        try {

            // Handling duplicate addresses during file creation
            if (Files.exists(path)) {
                System.err.println("Folder already exists.");
                return;
            }
            Files.createDirectories(path);
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

    public void lockFolder() {
        camouflage.applySystemAttributes(getVaultPath());
    }
    public void unlockFolder() {
        camouflage.removeSystemAttributes(getVaultPath());
    }

    public boolean isVisible() throws IOException {
        Path path = Path.of(getVaultPath());

        if (!Files.exists(path)) return false;

        return !(boolean) Files.getAttribute(
                path, "dos:hidden"
        );
    }
}
