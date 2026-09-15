package com.mirosync;

import com.mirosync.folder.FolderManager;
import com.mirosync.password.PasswordManager;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

public class Mirosync {
    private final String path;
    public Mirosync(String path) {
        this.path = path;
    }
    public void start() {
        FolderManager folderManager = new FolderManager(
                path == null ? null : path
        );
        PasswordManager passwordManager = new PasswordManager();
        File files = new File("config.properties");
        System.out.println("..:: Mirosync Protector ::..");

        Scanner scanner = new Scanner(System.in);
        if (!files.exists()) {
            System.out.println("First boost detected. Please choose a password : ");

            String password = scanner.next();
            if (password == null) {
                System.err.println("Please use a terminal");
                return;
            }
            passwordManager.savePassword(
                    passwordManager.hashPassword(password)
            );
            folderManager.createFolder();
            folderManager.hideFolder();
        }
        else {
            int retryCount = 3;
            while (retryCount != 0) {
                retryCount--;
                String password = scanner.next();
                if (passwordManager.passwordValidator(password)) {
                    System.err.println("Folder is unlocked");
                    folderManager.showFolder();
                    System.out.println("Press any key to lock folder...");
                    scanner.next();
                    System.err.println("Folder is locked");
                    folderManager.hideFolder();
                    return;
                }
                System.out.println("try left : " + retryCount);
                System.err.println("Wrong password. Try again.");
            }
        }
    }
}
