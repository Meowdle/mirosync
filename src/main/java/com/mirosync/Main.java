package com.mirosync;

import com.mirosync.folder.FolderManager;
import com.mirosync.password.PasswordManager;

import java.io.File;
import java.util.Scanner;

public class Main {
    private static File config;
    private static PasswordManager passwordManager;
    private static FolderManager folderManager;
    public static void main(String[] args) {
        config = new File("config.properties");
        passwordManager = new PasswordManager();
        folderManager = new FolderManager(null);

        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome page");

        if (!config.exists() || folderManager.exists()) {

            System.out.println("Set a password");

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
                retryCount = retryCount - 1;
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