package com.mirosync.password;

import com.mirosync.config.ConfigPaths;
import org.jetbrains.annotations.NotNull;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Properties;

public class PasswordStorage {

    public void save(PasswordHasher.@NotNull HashResults results) {
        ConfigPaths.ensureDirectoryExists();
        String hash = Base64
                .getEncoder()
                .encodeToString(results.hash());
        String salt = Base64
                .getEncoder()
                .encodeToString(results.salt());

        Properties properties = new Properties();
        properties.setProperty("password_hash", hash);
        properties.setProperty("password_salt", salt);

        try (FileOutputStream fileOutputStream =
                new FileOutputStream(ConfigPaths.CONFIG_FILE)) {
            properties.store(fileOutputStream, null);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private @NotNull Properties loadProperties() {
        Properties properties = new Properties();
        try (FileInputStream fileInputStream =
                     new FileInputStream(ConfigPaths.CONFIG_FILE)) {
            properties.load(fileInputStream);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        return properties;
    }

    public byte[] loadHash() {
        return Base64.getDecoder()
                .decode(
                        loadProperties().getProperty("password_hash")
                );
    }

    public byte[] loadSalt() {
        return Base64.getDecoder()
                .decode(
                        loadProperties().getProperty("password_salt")
                );
    }
}
