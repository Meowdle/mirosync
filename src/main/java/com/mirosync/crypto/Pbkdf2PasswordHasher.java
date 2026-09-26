package com.mirosync.crypto;

import com.mirosync.exception.CryptoException;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;

public final class Pbkdf2PasswordHasher implements PasswordHasher {

    private static final int ITERATIONS  = 310_000;
    private static final int KEY_LENGTH  = 256;
    private static final int SALT_LENGTH = 16;
    private static final String ALGORITHM = "PBKDF2WithHmacSHA256";

    private final SecureRandom secureRandom = new SecureRandom();
    private final SecretKeyFactory factory;

    public Pbkdf2PasswordHasher() throws CryptoException {
        try {
            factory = SecretKeyFactory.getInstance(ALGORITHM);
        }
        catch (NoSuchAlgorithmException e) {
            throw new CryptoException("Algorithm not found: " + ALGORITHM, e);
        }
    }

    @Contract("_ -> new")
    @Override public @NotNull HashResult hash(char[] password) throws CryptoException {
        byte[] salt = new byte[SALT_LENGTH];
        secureRandom.nextBytes(salt);
        byte[] hash = derive(password, salt);
        return new HashResult(hash, salt);
    }

    @Override public boolean verify(char[] password, @NotNull HashResult stored) throws CryptoException {
        byte[] hash = derive(password, stored.salt());
        return java.security.MessageDigest.isEqual(hash, stored.hash());
    }

    private byte[] derive(char[] password, byte[] salt) throws CryptoException {
        PBEKeySpec spec = new PBEKeySpec(password, salt, ITERATIONS, KEY_LENGTH);
        try {
            return factory.generateSecret(spec).getEncoded();
        }
        catch (InvalidKeySpecException e) {
            throw new CryptoException("Failed to derive key", e);
        }
        finally {
            spec.clearPassword();
            Arrays.fill(password, '\0');
        }
    }
}
