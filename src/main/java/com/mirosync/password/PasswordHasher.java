package com.mirosync.password;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;

public class PasswordHasher {

    private final SecureRandom secureRandom;
    private final SecretKeyFactory secretKeyFactory;

    public PasswordHasher() {
        secureRandom = new SecureRandom();
        try {
            secretKeyFactory = SecretKeyFactory.getInstance(
                    "PBKDF2WithHmacSHA256"
            );
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public HashResults generateHash(String password) {
        char[] passwordToChar = password.toCharArray();
        byte[] salt = new byte[16];
        secureRandom.nextBytes(salt);

        try {
            byte[] hash = secretKeyFactory.generateSecret(
                    new PBEKeySpec(
                            passwordToChar,
                            salt,
                            310_000,
                            256
                    )
            ).getEncoded();

            return new HashResults(hash, salt);
        }
        catch (InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean verify(
            String password,
            byte[] storedHash,
            byte[] storedSalt
    ) {
        try {
            byte[] newHash = secretKeyFactory.generateSecret(
                    new PBEKeySpec(
                            password.toCharArray(),
                            storedSalt,
                            310_000,
                            256
                    )
            ).getEncoded();

            return Arrays.equals(storedHash, newHash);
        }
        catch (InvalidKeySpecException  e) {
            throw new RuntimeException(e);
        }
    }

    public record HashResults(
            byte[] hash,
            byte[] salt
    ) {}
}
