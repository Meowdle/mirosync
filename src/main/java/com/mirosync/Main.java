package com.mirosync;

import java.io.Console;
import java.io.File;
import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        File config = new File("config.properties");
        PasswordManager passwordManager = new PasswordManager();
        FolderManager folderManager = new FolderManager(null);

        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome page");

        if (!config.exists()) {

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
                if (Objects.equals(
                        passwordManager.hashPassword(password),
                        passwordManager.loadPassword())
                ) {
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