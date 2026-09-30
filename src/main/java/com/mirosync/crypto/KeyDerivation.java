package com.mirosync.crypto;

import com.mirosync.exception.CryptoException;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;

public final class KeyDerivation {
    private static final int ITERATIONS = 310_000;
    private static final int KEY_LENGTH = 256;
    private static final String ALGORITHM = "PBKDF2WithHmacSHA256";

    private final SecretKeyFactory factory;

    public KeyDerivation() throws CryptoException {
        try {
            factory = SecretKeyFactory.getInstance(ALGORITHM);
        }
        catch (NoSuchAlgorithmException e) {
            throw new CryptoException("Algorithm not found: " + ALGORITHM, e);
        }
    }

    public SecretKey derive(char[] password, byte[] salt) throws CryptoException {
        char[] passwordCopy = Arrays.copyOf(password, password.length);
        PBEKeySpec spec = new PBEKeySpec(passwordCopy, salt, ITERATIONS, KEY_LENGTH);
        try {
            byte[] keyBytes = factory.generateSecret(spec).getEncoded();
            return new SecretKeySpec(keyBytes, "AES");
        }
        catch (InvalidKeySpecException e) {
            throw new CryptoException("Failed to derive AES key", e);
        }
        finally {
            spec.clearPassword();
            Arrays.fill(passwordCopy, '\0');
        }
    }
}
