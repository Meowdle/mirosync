package com.mirosync.security;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

public class LockoutManager {

    private final Properties properties = new Properties();
    private static final String LOCKOUT_FILE = "lockout.properties";
    private static final String LOCKOUT_KEY = "lockout_until";

    public boolean isLocked() {
        String value = load().getProperty(LOCKOUT_KEY);
        if (value == null) return false;
        return System.currentTimeMillis()
                < Long.parseLong(value);
    }

    public void lockOut() {
        properties.setProperty(
                LOCKOUT_KEY,
                String.valueOf(
                        System.currentTimeMillis() + (15 * 60 * 1000)
                )
        );
        save();
    }

    private Properties load() {
        try (FileInputStream fileInputStream
                     = new FileInputStream(LOCKOUT_FILE)) {
            properties.load(fileInputStream);
        }
        catch (Exception _) {}
        return properties;
    }

    private void save() {
        try (FileOutputStream fileOutputStream
                    = new FileOutputStream(LOCKOUT_FILE)) {
            properties.store(fileOutputStream, null);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public long remainingTimeToUnlock() {
        String value = load().getProperty("lockout_until");
        if (value == null) return 0;
        long remaining = Long.parseLong(value) - System.currentTimeMillis();
        return remaining / 1000 / 60;
    }
}
