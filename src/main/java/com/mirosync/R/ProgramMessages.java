package com.mirosync.R;

public final class ProgramMessages {
    // no instance creation allowed
    private ProgramMessages() {}

    // global
    public static final String TITLE = "..:: Mirosync ::..";
    public static final String TERMINAL_DOODLE = ">_ ";
    public static final String FOLDER_LOCKED = "[*] Folder is locked";
    public static final String FOLDER_UNLOCKED = "[*] Folder is unlocked";
    public static final String UNDEFINE_BEHAVIOR = "[Undefine_behavior]";
    public static final String TYPE_L_LOCK_FOLDER = "[+] Type (L, l) to lock your folder.";
    public static final String ENTER_PASSWORD = "[+] Enter your password : ";

    // == first boot ==
    public static final String FIRST_RUN_TITLE = "[*] First boost detected. Please complete Mirosync configuration : "; // first boot title
    public static final String FIRST_RUN_CHOOS_PATH = "[+] Choose where you want Mirosync to be saved.";
    public static final String FIRST_RUN_ORIGINAL_PATH = "[1] Original path - (C:/User/PC_NAME/Mirosync)"; // first option - original path
    public static final String FIRST_RUN_COSTUME_PATH = "[2] Custom path"; // second option - costume path
    public static final String FIRST_RUN_CREATE_PASSWORD = "[+] Create a strong password : ";
    public static final String FIRST_RUN_CHOOSE_PATH = "[+] Select your desire path : ";

    // == normal menu ==
    public static final String DEFAULT_MENU_TITLE = "[*] Choose what action you wanna do :";
    public static final String UNLOCK_THE_FOLDER_OPTION = "[1] Unlock folder";
    public static final String LOCK_THE_FOLDER_OPTION = "[1] Lock folder";
    public static final String STILL_VISIBLE_LEAVE_IT = "[2] Terminal";

    // == errors ==
    public static final String WRONG_PASSWORD = "Wrong password. Try again.";
    public static final String USE_TERMINAL = "Please use a terminal";
    public static final String TO_MANY_WRONG_PASSWORD_ENTRY = "Mirosync is locked.";

    // == locked menu ==
    public static final String LOCKED_MENU_TITLE = "[*] Program is locked.";

    // == dynamic counting ==
    // try left counter
    public static String triesLeft(int count) {
        return "Tries left: " + count;
    }
    // till unlock program
    public static String timeLeftToUnlock(long time) {
        return "Program will be unlocked in : " + time + " minutes.\n";
    }
}
