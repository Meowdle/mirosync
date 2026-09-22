package com.mirosync;

import com.mirosync.folder.FolderManager;
import com.mirosync.graphic.Menu;
import com.mirosync.password.PasswordHasher;
import com.mirosync.password.PasswordStorage;
import com.mirosync.security.LockoutManager;
import com.mirosync.validate.Validation;

import java.io.File;
import java.io.IOException;

public class Mirosync {

    private FolderManager folderManager;
    private Validation validation;
    private LockoutManager lockoutManager;
    private PasswordHasher passwordHasher;
    private PasswordStorage passwordStorage;
    private Menu menu;

    public void start() {

        // Refer to the terminal cleaning step from the previous instructions
        clearTerminal();

        // Reference to the core constructor
        instructionsInitializer();

        // If the software is launching for the first time, the condition is triggered
        if (isFirstRun()) {
            firstBootMenuPathHandler();
            handlePassword();
        }
        else {
            defaultMenu();
        }
    }

    public void firstBootMenuPathHandler() {
        switch (menu.firstBootMenuPath()) {
            case 1 -> createFolderWithOriginalPath();
            case 2 -> createFolderWithCostumePath(
                    menu.firstBootMenuCustomPath()
            );
        }
    }

    public void defaultMenu() {
        if (lockoutManager.isLocked()) {
            clearTerminal();
            menu.lockedProgramMenu(
                    lockoutManager.remainingTimeToUnlock()
            );
            return;
        }
        while (true) {

            switch (
                    menu.defaultMenu(
                            validation.isVaultOpen()
                    )
            ) {
                case 1 -> {
                    clearTerminal();

                    if (!validation.isVaultOpen()) {
                        menu.enterPasswordMenuHeader();
                        int attempts = 3;
                        while (attempts > 0) {

                            if (validation.passwordValidator(
                                    menu.enterPasswordMenu()
                            )) {
                                unlock();
                                if (menu.afterOpeningFolderMenu().equals("l")) {
                                    clearTerminal();
                                    lock();
                                }
                                return;
                            }

                            attempts--;
                            if (attempts == 0) {
                                clearTerminal();
                                lockoutManager.lockOut();
                                menu.lockedProgramMenu(
                                        lockoutManager.remainingTimeToUnlock()
                                );
                                return;
                            }

                            menu.wrongPasswordMenu(attempts);
                        }
                        return;
                    }
                    else {
                        lock();
                        menu.folderForcedLocked();
                    }

                    return;
                }
                case 2 -> {
                    // TODO [Terminal Command]
                    return;
                }
            }
        }
    }

    // Core Builder
    private void instructionsInitializer() {
        folderManager   = new FolderManager(null);
        passwordHasher  = new PasswordHasher();
        passwordStorage = new PasswordStorage();
        validation      = new Validation(folderManager, passwordHasher, passwordStorage);
        lockoutManager  = new LockoutManager();
        menu            = new Menu();
    }
    // It checks whether the software is being run for the first time
    private boolean isFirstRun() {
        return !new File("config.properties").exists();
    }

    // Start Menu – Option to create a folder with the root path
    public void createFolderWithOriginalPath() {
        folderManager.createFolder();
        folderManager.hideFolder();
    }

    // Start Menu – Option to create a folder at a selected location
    public void createFolderWithCostumePath(String path) {
        folderManager = new FolderManager(path);
        validation    = new Validation(folderManager, passwordHasher, passwordStorage);
        folderManager.createFolder();
        folderManager.hideFolder();
    }

    // It manages the password; it first encrypts it and then saves it.
    public void handlePassword() {
        String password = menu.firstBootMenuPassword();
        PasswordHasher.HashResults hashResults
                = passwordHasher.generateHash(password);
        passwordStorage.save(hashResults);
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
    private void lock()   {folderManager.hideFolder();}
}
