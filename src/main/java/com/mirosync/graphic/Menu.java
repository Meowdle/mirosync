package com.mirosync.graphic;

import com.mirosync.R.ProgramMessages;

import java.util.Scanner;

public class Menu {

    private final Scanner scanner = new Scanner(System.in);
    private int input;

    public int firstBootMenuPath() {
        while (true) {
            firstBootMenuContextPath();
            input = scanner.nextInt();
            switch (input) {
                case 1, 2 -> {
                    return input;
                }
                default -> System.err.println(ProgramMessages.UNDEFINE_BEHAVIOR);
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

    public String firstBootMenuPassword() {
        firstBootMenuContextPassword();
        return scanner.next();
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

    public int defaultMenu(boolean isFolderOpen) {
        while (true) {
            defaultMenuContext(isFolderOpen);
            input = scanner.nextInt();
            switch (input) {
                case 1, 2 -> {
                    return input;
                }
                default -> System.err.println(ProgramMessages.UNDEFINE_BEHAVIOR);
            }
        }
    }
    private void defaultMenuContext(boolean isFolderOpen) {
        System.out.println(ProgramMessages.TITLE);
        System.out.println("\n");
        System.out.println(ProgramMessages.DEFAULT_MENU_TITLE);
        System.out.println("\n");
        System.out.println(isFolderOpen
                ? ProgramMessages.UNLOCK_THE_FOLDER_OPTION
                : ProgramMessages.LOCK_THE_FOLDER_OPTION);
        System.out.println("\n");
        System.out.print(ProgramMessages.TERMINAL_DOODLE);
    }

    public int folderStillVisibleMenu() {
        while (true) {
            folderStillVisibleMenuTitles();
            input = scanner.nextInt();
            switch (input) {
                case 1, 2-> {
                    return input;
                }
                default -> System.err.println(ProgramMessages.UNDEFINE_BEHAVIOR);
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
