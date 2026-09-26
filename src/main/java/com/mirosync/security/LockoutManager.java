package com.mirosync.security;

import com.mirosync.config.ConfigPaths;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

public class LockoutManager {

    private final Properties properties = new Properties();
    private static final String LOCKOUT_KEY = "lockout_until";

    public boolean isLocked() {

        // get the 'key' value form property
        String value = load().getProperty(LOCKOUT_KEY);

        // checks if the kay value is null or file has not been created
        // all these means the file is not locked
        if (value == null) return false;

        return System.currentTimeMillis()   // ms time passed
                < Long.parseLong(value);    // convert string to long format
    }

    public void lockOut() {
        properties.setProperty(
                LOCKOUT_KEY,
                String.valueOf(
                        System.currentTimeMillis() + (15 * 60 * 1000)
                ) // convert long to string format
        );
        // after setting in cache, file will be saved
        save();
    }

    private Properties load() {
        // opens a file stream for reading a file
        try (FileInputStream fileInputStream
                     = new FileInputStream(ConfigPaths.LOCKOUT_FILE)) { // close stream after
            properties.load(fileInputStream);
        } // ignore the exception
        catch (Exception _) {}
        return properties;
    }

    private void save() {
        // opens a file stream for writing on files
        try (FileOutputStream fileOutputStream
                    = new FileOutputStream(ConfigPaths.LOCKOUT_FILE)) { // close stream after
            // store the information
            properties.store(fileOutputStream, null);
        } // if it didn't save
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
