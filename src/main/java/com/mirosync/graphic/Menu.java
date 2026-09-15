package com.mirosync.graphic;

import com.mirosync.R.ProgramMessages;

import java.util.Scanner;

public class Menu {
    private final Scanner scanner = new Scanner(System.in);
    public void startUpMenu() {

        while (true) {
            welcomeMenu();
            int input = scanner.nextInt();
            switch (input) {
                case 1 -> {
                    // TODO
                    return;
                }
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

    public void enterPasswordMenu() {

    }

    private void welcomeMenu() {
        System.out.println(ProgramMessages.TITLE);
        System.out.println("\n");
        System.out.println(ProgramMessages.FIRST_OPTION);
        System.out.println(ProgramMessages.SECOND_OPTION);
        System.out.println("\n");
        System.out.print(ProgramMessages.TERMINAL_DOODLE);
    }
}
