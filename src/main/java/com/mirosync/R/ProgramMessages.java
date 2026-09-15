package com.mirosync.R;

public final class ProgramMessages {
    private ProgramMessages() {}

    public static final String WELCOME = "..:: Mirosync Protector ::..";
    public static final String FIRST_OPTION = "[1]Create vault in default location";
    public static final String SECOND_OPTION = "[2] Choose custom path";
    public static final String TERMINAL_DOODLE = ">_ ";
    public static final String FIRST_RUN = "First boost detected. Please choose a password : ";
    public static final String UNLOCKED = "Folder is unlocked";
    public static final String LOCKED = "Folder is locked";
    public static final String PRESS_KEY = "Press any key to lock folder...";
    public static final String WRONG_PASSWORD = "Wrong password. Try again.";
    public static final String USE_TERMINAL = "Please use a terminal";
    public static String triesLeft(int count) {
        return "Tries left: " + count;
    }
    public static final String VAULT_STILL_OPEN = "Folder is still unlocked and visible. Lock it? [Y/N]";
    public static final String UNDEFINE_BEHAVIOR = "Undefine_behavior";
    public static final String ENTER_PASSWORD = "Enter your password ";
}
