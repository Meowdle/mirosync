package com.mirosync.config;

import java.io.File;

public class ConfigPaths {
    private static final String FOLDER_NAME = "Mirosync";
    private static final String BASE_DIR
            = System.getenv("APPDATA")
            + "\\"
            + FOLDER_NAME
            + "\\";
    public static final String CONFIG_FILE = BASE_DIR + "config.properties";
    public static final String LOCKOUT_FILE = BASE_DIR + "lockout.properties";

    public static void ensureDirectoryExists() {
        new File(BASE_DIR).mkdirs();
    }
}
