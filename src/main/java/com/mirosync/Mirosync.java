package com.mirosync;

import com.mirosync.R.ProgramMessages;
import com.mirosync.folder.FolderManager;
import com.mirosync.graphic.Menu;
import com.mirosync.password.PasswordManager;
import com.mirosync.validate.Validation;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Mirosync {

    private final String path;
    public Mirosync(String path) {
        this.path = path;
    }

    private FolderManager folderManager;
    private PasswordManager passwordManager;
    private Validation validation;

    public void start() {
        clearTerminal();
        instructionsInitializer();
        new Menu().startUpMenu();

        Scanner scanner = new Scanner(System.in);
        if (validation.isVaultOpen()) {
            System.out.println(ProgramMessages.VAULT_STILL_OPEN);

            while (true) {
                String answer = scanner.next().toLowerCase();
                switch (answer) {
                    case "y", "yes" -> {
                        lock();
                        System.out.println(ProgramMessages.LOCKED);
                        return;
                    }
                    case "n", "no" -> {
                        return;
                    }
                    default -> {
                        System.err.println(ProgramMessages.UNDEFINE_BEHAVIOR);
                    }
                }
            }
        }
        if (isFirstRun()) {
            System.out.println(ProgramMessages.FIRST_RUN);

            String password = scanner.next();
            if (password == null) {
                System.err.println(ProgramMessages.USE_TERMINAL);
                return;
            }
            handleInput(password);
            folderManager.createFolder();
            lock();
        }
        else {
            int retryCount = 3;
            System.out.println(ProgramMessages.TITLE);
            while (retryCount != 0) {
                retryCount--;
                System.out.print(ProgramMessages.ENTER_PASSWORD);
                String password = scanner.next();
                if (validation.passwordValidator(password)) {
                    System.out.println(ProgramMessages.UNLOCKED);
                    unlock();
                    System.out.println(ProgramMessages.PRESS_KEY);
                    scanner.next();
                    System.out.println(ProgramMessages.LOCKED);
                    lock();
                    return;
                }
                System.out.println(ProgramMessages.triesLeft(retryCount));
                System.err.println(
                        retryCount == 0
                                ? ProgramMessages.WRONG_PASSWORD
                                : ProgramMessages.OUT_OF_CHANCE
                );
            }
        }
    }
    private void instructionsInitializer() {
        folderManager = new FolderManager(path);
        passwordManager = new PasswordManager();
        validation = new Validation(passwordManager, folderManager);
    }
    private boolean isFirstRun() {
        return !new File("config.properties").exists();
    }
    private void handleInput(String password) {
        passwordManager.savePassword(
                passwordManager.hashPassword(password)
        );
    }
    private void clearTerminal() {
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
    private void unlock() {
        folderManager.showFolder();
    }
    private void lock() {
        folderManager.hideFolder();
    }
}
