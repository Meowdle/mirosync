package com.mirosync.ui;

import com.mirosync.R.ProgramMessages;

import java.nio.file.Path;
import java.util.Scanner;

public final class Menu {
    private final Scanner scanner = new Scanner(System.in);

    public Path askVaultPath() {
        showVaultPathMenu();
        int choice = readInt();
        if (choice == 2) {
            System.out.print(ProgramMessages.FIRST_RUN_CHOOSE_PATH);
            System.out.print(ProgramMessages.TERMINAL_DOODLE);
            String path = scanner.nextLine().trim();
            return Path.of(path);
        }
        return null; // default path
    }

    public char[] askNewPassword() {
        System.out.println(ProgramMessages.TITLE);
        System.out.println(ProgramMessages.FIRST_RUN_CREATE_PASSWORD);
        System.out.print(ProgramMessages.TERMINAL_DOODLE);
        return scanner.nextLine().trim().toCharArray();
    }

    public char[] askPassword() {
        System.out.print(ProgramMessages.TERMINAL_DOODLE);
        return scanner.nextLine().trim().toCharArray();
    }

    public int showMain(boolean isOpen) {
        System.out.println(ProgramMessages.TITLE);
        System.out.println(ProgramMessages.DEFAULT_MENU_TITLE);
        System.out.println(isOpen
                ? ProgramMessages.LOCK_THE_FOLDER_OPTION
                : ProgramMessages.UNLOCK_THE_FOLDER_OPTION);
        System.out.print(ProgramMessages.TERMINAL_DOODLE);
        return readInt();
    }

    public void showUnlocked() {
        System.out.println(ProgramMessages.FOLDER_UNLOCKED);
        System.out.println(ProgramMessages.TYPE_L_LOCK_FOLDER);
        while (true) {
            System.out.print(ProgramMessages.TERMINAL_DOODLE);
            String answer = scanner.nextLine().trim().toLowerCase();
            if (answer.equals("l")) return;
        }
    }

    public void showLocked(long minutesRemaining) {
        System.out.println(ProgramMessages.TITLE);
        System.out.println(ProgramMessages.LOCKED_MENU_TITLE);
        System.out.println(ProgramMessages.timeLeftToUnlock(minutesRemaining));
    }

    public void showWrongPassword(int attemptsLeft) {
        System.out.println(ProgramMessages.WRONG_PASSWORD);
        System.out.println(ProgramMessages.triesLeft(attemptsLeft));
    }

    private void showVaultPathMenu() {
        System.out.println(ProgramMessages.TITLE);
        System.out.println(ProgramMessages.FIRST_RUN_TITLE);
        System.out.println(ProgramMessages.FIRST_RUN_CHOOS_PATH);
        System.out.println(ProgramMessages.FIRST_RUN_ORIGINAL_PATH);
        System.out.println(ProgramMessages.FIRST_RUN_COSTUME_PATH);
        System.out.print(ProgramMessages.TERMINAL_DOODLE);
    }

    public void showFolderLocked() {
        System.out.println(ProgramMessages.FOLDER_LOCKED);
    }

    public void showPasswordHeader() {
        System.out.println(ProgramMessages.TITLE);
        System.out.println("\n");
        System.out.println(ProgramMessages.ENTER_PASSWORD);
    }

    private int readInt() {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
