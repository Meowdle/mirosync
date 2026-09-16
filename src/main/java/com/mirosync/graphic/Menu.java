package com.mirosync.graphic;

import com.mirosync.R.ProgramMessages;

import java.util.Scanner;

public class Menu {

    private final Scanner scanner = new Scanner(System.in);
    private int input;

    public int firstBootMenuPath() {
        firstBootMenuContextPath();
        while (true) {
            input = getInput();
            switch (input) {
                case 1, 2 -> {
                    return input;
                }
                default -> {
                    System.err.println(ProgramMessages.UNDEFINE_BEHAVIOR);
                    System.out.print(ProgramMessages.TERMINAL_DOODLE);
                }
            }
        }
    }
    public String firstBootMenuCustomPath() {
        System.out.print(ProgramMessages.FIRST_RUN_CHOOSE_PATH);
        System.out.println("\n");
        System.out.print(ProgramMessages.TERMINAL_DOODLE);
        return scanner.next();
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
        System.out.println("\n");
        System.out.print(ProgramMessages.TERMINAL_DOODLE);
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
        defaultMenuContext(isFolderOpen);
        while (true) {
            input = getInput();
            switch (input) {
                case 1, 2 -> {
                    return input;
                }
                default -> {
                    System.err.println(ProgramMessages.UNDEFINE_BEHAVIOR);
                    System.out.print(ProgramMessages.TERMINAL_DOODLE);
                }
            }
        }
    }
    private void defaultMenuContext(boolean isFolderOpen) {
        System.out.println(ProgramMessages.TITLE);
        System.out.println("\n");
        System.out.println(ProgramMessages.DEFAULT_MENU_TITLE);
        System.out.println("\n");
        System.out.println(isFolderOpen
                ? ProgramMessages.LOCK_THE_FOLDER_OPTION
                : ProgramMessages.UNLOCK_THE_FOLDER_OPTION);
        System.out.println("\n");
        System.out.print(ProgramMessages.TERMINAL_DOODLE);
    }

    private int getInput() {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        }
        catch (NumberFormatException e) {
            return -1;
        }
    }

    public String enterPasswordMenuHeader() {
        System.out.println(ProgramMessages.TITLE);
        System.out.println("\n");
        System.out.println(ProgramMessages.ENTER_PASSWORD);
        System.out.print(ProgramMessages.TERMINAL_DOODLE);
        return scanner.next();
    }

    public String enterPasswordMenu() {
        return scanner.nextLine().trim();
    }

    public String afterOpeningFolderMenu() {
        System.out.println(ProgramMessages.TYPE_L_LOCK_FOLDER);
        while (true) {
            System.out.println("\n");
            System.out.print(ProgramMessages.TERMINAL_DOODLE);
            String answer = scanner.next().toLowerCase();
            if (answer.equals("l"))
                return answer;
        }
    }

    public void lockedProgramMenu(long minutesRemaining) {
        lockedProgramMenuContext(minutesRemaining);
    }

    private void lockedProgramMenuContext(long minutesRemaining) {
        System.out.println(ProgramMessages.TITLE);
        System.out.println("\n");
        System.out.println(ProgramMessages.LOCKED_MENU_TITLE);
        System.out.println("\n");
        System.out.println(
                ProgramMessages.timeLeftToUnlock(
                        minutesRemaining
                )
        );
    }

    public void wrongPasswordMenu(int triesLeft) {
        System.out.println(ProgramMessages.WRONG_PASSWORD);
        System.out.println(ProgramMessages.triesLeft(triesLeft));
        System.out.println("\n");
        System.out.print(ProgramMessages.TERMINAL_DOODLE);
    }
}
