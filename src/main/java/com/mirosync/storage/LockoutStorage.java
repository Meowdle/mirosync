package com.mirosync.storage;

import com.mirosync.config.ConfigPaths;
import com.mirosync.exception.StorageException;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Properties;

public class LockoutStorage implements Storage {

    @Override public void save(@NotNull Properties data) throws StorageException {
        try (
                var out = Files.newOutputStream(ConfigPaths.LOCKOUT_FILE)
        ) {
            data.store(out, null);
        }
        catch (IOException e) {
            throw new StorageException("Failed to save config", e);
        }
    }

    @Override public Properties load() throws StorageException {
        Properties props = new Properties();
        try (
                var in = Files.newInputStream(ConfigPaths.LOCKOUT_FILE)
        ) {
            props.load(in);
        }
        catch (IOException e) {
            throw new StorageException("Failed to load config", e);
        }
        return props;
    }
}
