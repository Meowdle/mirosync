package com.mirosync.graphic;

import com.mirosync.Mirosync;
import com.mirosync.R.ProgramMessages;
import com.mirosync.folder.FolderManager;
import com.mirosync.password.PasswordManager;
import com.mirosync.validate.Validation;

import java.util.Scanner;

public class Menu {
    private final Scanner scanner = new Scanner(System.in);
    private final Mirosync mirosync = new Mirosync();

    private FolderManager folderManager;
    private PasswordManager passwordManager;
    private Validation validation;
    private int input;

    public Menu(
            FolderManager folderManager,
            PasswordManager passwordManager,
            Validation validation
    ) {
        this.folderManager = folderManager;
        this.passwordManager = passwordManager;
        this.validation = validation;
    }

    public void firstBootMenuPath() {
        mirosync.clearTerminal();
        while (true) {
            firstBootMenuContextPath();
            input = scanner.nextInt();
            switch (input) {
                // Create vault in default location
                case 1 -> {
                    mirosync.createFolderWithOriginalPath();
                    return;
                }
                // Choose custom path
                case 2 -> {
                    String path = scanner.nextLine();
                    mirosync.createFolderWithCostumePath(path);
                    return;
                }
                default -> {
                    System.err.println(ProgramMessages.UNDEFINE_BEHAVIOR);
                }
            }
        }
    }
    private void firstBootMenuContextPath() {
        System.out.println(ProgramMessages.TITLE);
        System.out.println("\n");
        System.out.println(ProgramMessages.FIRST_RUN_TITLE);
        System.out.println("\n");
        System.out.println(ProgramMessages.FIRST_RUN_ORIGINAL_PATH);
        System.out.println(ProgramMessages.FIRST_RUN_COSTUME_PATH);
        System.out.println("\n");
        System.out.print(ProgramMessages.TERMINAL_DOODLE);
    }

    public void firstBootMenuPassword() {
        mirosync.clearTerminal();
        firstBootMenuContextPassword();
        String password = scanner.next();
        mirosync.handlePassword(password);
    }
    private void firstBootMenuContextPassword() {
        System.out.println(ProgramMessages.TITLE);
        System.out.println("\n");
        System.out.println(ProgramMessages.FIRST_RUN_TITLE);
        System.out.println("\n");
        System.out.println(ProgramMessages.FIRST_RUN_CREATE_PASSWORD);
        System.out.println("\n");
        System.out.print(ProgramMessages.TERMINAL_DOODLE);
    }

    public void defaultMenu() {
        while (true) {
            defaultMenuContext();
            input = scanner.nextInt();
            switch (input) {
                case 1 -> {
                    if (!validation.isVaultOpen()) folderManager.showFolder();
                    else folderManager.hideFolder();
                    return;
                }
                case 2 -> {
                    // TODO [Terminal Command]
                    return;
                }
                default -> System.err.println(ProgramMessages.UNDEFINE_BEHAVIOR);
            }
        }
    }
    private void defaultMenuContext() {
        System.out.println(ProgramMessages.TITLE);                  // ..:: Mirosync ::..
        System.out.println("\n");                                   //
        System.out.println(ProgramMessages.DEFAULT_MENU_TITLE);     // >_ Choose what action you wanna do :
        System.out.println("\n");                                   //

        if (!validation.isVaultOpen())
            System.out.println(ProgramMessages.UNLOCK_THE_FOLDER_OPTION);
        else
            System.out.println(ProgramMessages.LOCK_THE_FOLDER_OPTION);

        System.out.println("\n");
        System.out.print(ProgramMessages.TERMINAL_DOODLE);
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



    public void folderStillVisibleMenuTitles() {
        System.out.println(ProgramMessages.DEFAULT_MENU_TITLE);
        System.out.println("\n");
        System.out.println(ProgramMessages.LOCK_THE_FOLDER_OPTION);
        System.out.println(ProgramMessages.STILL_VISIBLE_LEAVE_IT);
        System.out.println("\n");
        System.out.print(ProgramMessages.TERMINAL_DOODLE);
    }
}
