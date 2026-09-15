package com.mirosync;

import com.mirosync.folder.FolderManager;
import com.mirosync.graphic.Menu;
import com.mirosync.password.PasswordManager;
import com.mirosync.validate.Validation;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Mirosync {

    private FolderManager folderManager;
    private PasswordManager passwordManager;
    private Validation validation;
    private Menu menu;

    public void START() {

        // Refer to the terminal cleaning step from the previous instructions
        clearTerminal();

        // Reference to the core constructor
        instructionsInitializer();

        // If the software is launching for the first time, the condition is triggered
        if (isFirstRun()) {
            menu.firstBootMenuPassword();
        }
        else {
            menu.defaultMenu(validation.isVaultOpen());
        }
    }

    public void firstBootMenuPathHandler() {
        menu.firstBootMenuPath();
        while (true) {
            switch (menu.firstBootMenuPath()) {
                case 1 -> {
                    createFolderWithOriginalPath();
                    return;
                }
                case 2 -> {
                    String path = new Scanner(System.in).nextLine();
                    createFolderWithCostumePath(path);
                    return;
                }
            }
        }
    }

    // Core Builder
    private void instructionsInitializer() {
        passwordManager = new PasswordManager();
        validation = new Validation(passwordManager, folderManager);
        menu = new Menu();
    }
    // It checks whether the software is being run for the first time
    private boolean isFirstRun() {
        return !new File("config.properties").exists();
    }

    // Start Menu – Option to create a folder with the root path
    public void createFolderWithOriginalPath() {
        folderManager = new FolderManager(null);
    }

    // Start Menu – Option to create a folder at a selected location
    public void createFolderWithCostumePath(String path) {
        folderManager = new FolderManager(path);
    }

    /**
     * It manages the password; it first encrypts it and then saves it.
     * @param password
     */
    public void handlePassword() {
        passwordManager.savePassword(
                passwordManager.hashPassword(
                        menu.firstBootMenuPassword()
                )
        );
    }

    // Clearing the terminal of previous commands
    public void clearTerminal() {
        try {
            /*
             * "cmd"  -> open window terminal
             * "/c"   -> do this prompt, then close
             * "cls"  -> clear screen for windows
             */
            new ProcessBuilder("cmd", "/c", "cls")
                    .inheritIO()    // Use the operational terminal
                    .start()        // Start the process (cls starts running)
                    .waitFor();     // Wait till 'cls' command processing is finished
        } // ignoring the exception
        catch (InterruptedException | IOException _) {}
    }

    // For quick management of file locking and unlocking
    private void unlock() {folderManager.showFolder();}
    private void lock() {folderManager.hideFolder();}
}
