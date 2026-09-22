package com.mirosync.password;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

public final class PasswordHasher {

    private final SecureRandom secureRandom;
    private final SecretKeyFactory secretKeyFactory;
    private static final int ITERATIONS = 310_000;
    private static final int KEY_LENGTH = 256;
    private static final int SALT_LENGTH = 16;

    public PasswordHasher() {
        secureRandom = new SecureRandom();
        try {
            // select the algorithm we wanna use
            secretKeyFactory = SecretKeyFactory.getInstance(
                    "PBKDF2WithHmacSHA256"
            );
        } // ain't no way
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public HashResults generateHash(String password) {
        char[] passwordToChar = password.toCharArray();
        byte[] salt = new byte[SALT_LENGTH];
        secureRandom.nextBytes(salt);

        final PBEKeySpec pbeKeySpec = new PBEKeySpec(
                passwordToChar,
                salt,
                ITERATIONS,
                KEY_LENGTH
        );
        try {
            byte[] hash = secretKeyFactory.generateSecret(
                    pbeKeySpec
            ).getEncoded();

            return new HashResults(hash, salt);
        }
        catch (InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
        finally {
            pbeKeySpec.clearPassword();
        }
    }

    public boolean verify(
            String password,
            byte[] storedHash,
            byte[] storedSalt
    ) {

        final PBEKeySpec pbeKeySpec = new PBEKeySpec(
                password.toCharArray(),
                storedSalt,
                ITERATIONS,
                KEY_LENGTH
        );
        try {
            byte[] newHash = secretKeyFactory.generateSecret(
                    pbeKeySpec
            ).getEncoded();

            return MessageDigest.isEqual(storedHash, newHash);
        }
        catch (InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
        finally {
            pbeKeySpec.clearPassword();
        }
    }

    public record HashResults(
            byte[] hash,
            byte[] salt
    ) {}
}
