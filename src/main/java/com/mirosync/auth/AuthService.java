package com.mirosync.auth;

import com.mirosync.crypto.PasswordHasher;
import com.mirosync.exception.AuthException;
import com.mirosync.exception.CryptoException;
import com.mirosync.exception.StorageException;
import com.mirosync.storage.ConfigStorage;

import java.util.Base64;
import java.util.Properties;

public final class AuthService {
    private final PasswordHasher hasher;
    private final ConfigStorage storage;

    public AuthService(PasswordHasher hasher, ConfigStorage storage) {
        this.hasher  = hasher;
        this.storage = storage;
    }

    public void savePassword(char[] password) throws AuthException {
        try {
            PasswordHasher.HashResult result = hasher.hash(password);
            Properties props = new Properties();
            props.setProperty("password_hash",
                    Base64.getEncoder().encodeToString(result.hash()));
            props.setProperty("password_salt",
                    Base64.getEncoder().encodeToString(result.salt()));

            storage.save(props);
        }
        catch (CryptoException | StorageException e) {
            throw new AuthException("Failed to save password", e);
        }
    }

    public boolean verify(char[] password) throws AuthException {
        try {
            Properties props = storage.load();
            byte[] hash = Base64.getDecoder()
                    .decode(props.getProperty("password_hash"));
            byte[] salt = Base64.getDecoder()
                    .decode(props.getProperty("password_salt"));

            return hasher.verify(password, new PasswordHasher.HashResult(hash, salt));
        }
        catch (CryptoException | StorageException e) {
            throw new AuthException("Failed to verify password", e);
        }
    }

    public byte[] loadSalt() throws AuthException {
        try {
            Properties props = storage.load();
            return Base64.getDecoder()
                    .decode(props.getProperty("password_salt"));
        }
        catch (StorageException e) {
            throw new AuthException("Failed to load salt", e);
        }
    }
}
