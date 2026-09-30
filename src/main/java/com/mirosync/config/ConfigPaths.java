package com.mirosync.config;

import com.mirosync.exception.VaultException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class ConfigPaths {

    private ConfigPaths() {}

    private static final String APP_NAME  = "Mirosync";

    private static final Path BASE_DIR = Path.of(
            System.getenv("APPDATA"), APP_NAME
    );

    public static final Path CONFIG_FILE = BASE_DIR.resolve("config.properties");
    public static final Path LOCKOUT_FILE = BASE_DIR.resolve("lockout.properties");

    public static void ensureDirectoryExists() throws VaultException {
        try {
            Files.createDirectories(BASE_DIR);
        }
        catch (IOException e) {
            throw new VaultException("Failed to create config directory", e);
        }
    }
}
