package com.mirosync;

import java.io.Console;
import java.io.File;
import java.util.Arrays;
import java.util.Objects;

public class Main {
    public static void main(String[] args) {
        File config = new File("config.properties");
        PasswordManager passwordManager = new PasswordManager();
        FolderManager folderManager = new FolderManager(null);

        // It is better than the 'Scanner', because it keeps the passwords hidden.
        Console console = System.console();

        if (!config.exists()) {

            char[] password = console.readPassword();
            String passwordString = new String(password);
            passwordManager.savePassword(
                    passwordManager.hashPassword(passwordString)
            );
            folderManager.createFolder();
            folderManager.hideFolder();
        }
        else {
            char[] password = console.readPassword();
            String passwordString = new String(password);
            if (Objects.equals(
                    passwordManager.hashPassword(passwordString),
                    passwordManager.loadPassword())
            ) {
                folderManager.showFolder();

                System.out.println("Press [Enter] to lock folder...");
                console.readLine(); folderManager.hideFolder();
            }
            else System.err.println("Wrong password.");
        }
    }
}