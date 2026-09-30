package com.mirosync.auth;

import com.mirosync.exception.AuthException;
import com.mirosync.exception.StorageException;
import com.mirosync.storage.LockoutStorage;

import java.util.Properties;

public final class LockoutService {

    private static final long LOCKOUT_DURATION = 15 * 60 * 1000L;
    private static final String LOCKOUT_KEY = "lockout_until";
    private final LockoutStorage storage;

    public LockoutService(LockoutStorage storage) {
        this.storage = storage;
    }

    public boolean isLocked() throws AuthException {
        try {
            Properties props = storage.load();
            String value = props.getProperty(LOCKOUT_KEY);
            if (value == null) return false;

            return System.currentTimeMillis() < Long.parseLong(value);
        }
        catch (StorageException | NumberFormatException e) {
            reset();
            return false;
        }
    }

    public void lockOut() throws AuthException {
        try {
            Properties props = new Properties();
            props.setProperty(LOCKOUT_KEY,
                    String.valueOf(System.currentTimeMillis() + LOCKOUT_DURATION));

            storage.save(props);
        }
        catch (StorageException e) {
            throw new AuthException("Failed to lock out", e);
        }
    }

    public long remainingMinutes() throws AuthException {
        try {
            Properties props = storage.load();
            String value = props.getProperty(LOCKOUT_KEY);
            if (value == null) return 0;
            long remaining = Long.parseLong(value) - System.currentTimeMillis();

            return Math.max(0, remaining / 1000 / 60);
        }
        catch (StorageException | NumberFormatException e) {
            reset();
            return 0;
        }
    }

    private void reset() {
        try {storage.save(new Properties());}
        catch (StorageException _) { }
    }
}
