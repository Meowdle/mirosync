package com.mirosync;

import com.mirosync.R.ProgramMessages;
import com.mirosync.folder.FolderManager;
import com.mirosync.password.PasswordManager;
import com.mirosync.validate.Validation;

import java.io.File;
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
        instructionsInitializer();
        System.out.println(ProgramMessages.WELCOME);

        Scanner scanner = new Scanner(System.in);

        if (validation.isVaultOpen()) {
            System.out.println(ProgramMessages.VAULT_STILL_OPEN);

            while (true) {
                String answer = scanner.next().toLowerCase();
                switch (answer) {
                    case "y", "yes" -> {
                        lock();
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
            while (retryCount != 0) {
                retryCount--;
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
                System.err.println(ProgramMessages.WRONG_PASSWORD);
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
    private void unlock() {
        folderManager.showFolder();
    }
    private void lock() {
        folderManager.hideFolder();
    }
}
