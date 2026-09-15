package com.mirosync.graphic;

import com.mirosync.R.ProgramMessages;
import com.mirosync.folder.FolderManager;
import com.mirosync.password.PasswordManager;
import com.mirosync.validate.Validation;

import java.util.Scanner;

public class Menu {
    private FolderManager folderManager;
    private PasswordManager passwordManager;
    private Validation validation;
    private final Scanner scanner = new Scanner(System.in);
    private int input;
    public void startUpMenu() {

        while (true) {
            welcomeMenuTitles();
            input = scanner.nextInt();
            switch (input) {
                // Create vault in default location
                case 1 -> {
                    new FolderManager(null).createFolder();
                    return;
                }
                // Choose custom path
                case 2 -> {
                    String path = scanner.nextLine();
                    new FolderManager(path).createFolder();
                    return;
                }
                default -> {
                    System.err.println(ProgramMessages.UNDEFINE_BEHAVIOR);
                }
            }
        }
    }

    public void folderStillVisibleMenu() {
        while (true) {
            folderStillVisibleMenuTitles();
            input = scanner.nextInt();
            switch (input) {
                // Create vault in default location
                case 1 -> {
                    // TODO
                    return;
                }
                // Choose custom path

                case 2 -> {
                    // TODO SOME
                    return;
                }
                default -> {
                    System.err.println(ProgramMessages.UNDEFINE_BEHAVIOR);
                }
            }
        }
    }

    private void welcomeMenuTitles() {
        System.out.println(ProgramMessages.TITLE);
        System.out.println("\n");
        System.out.println(ProgramMessages.FIRST_RUN_TITLE);
        System.out.println("\n");
        System.out.println(ProgramMessages.FIRST_RUN_ORIGINAL_PATH);
        System.out.println(ProgramMessages.FIRST_RUN_COSTUME_PATH);
        System.out.println("\n");
        System.out.print(ProgramMessages.TERMINAL_DOODLE);
    }

    public void folderStillVisibleMenuTitles() {
        System.out.println(ProgramMessages.STILL_VISIBLE_LOCK_IT_TITLE);
        System.out.println("\n");
        System.out.println(ProgramMessages.LOCK_THE_FOLDER_OPTION);
        System.out.println(ProgramMessages.STILL_VISIBLE_LEAVE_IT);
        System.out.println("\n");
        System.out.print(ProgramMessages.TERMINAL_DOODLE);
    }
}
