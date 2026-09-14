package com.mirosync;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordManager {

    public String hashPassword(String password) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = messageDigest.digest(
                    password.getBytes(StandardCharsets.UTF_8)
            );
            StringBuilder stringBuilder = new StringBuilder();
            for (byte bytes : hashBytes) {
                stringBuilder.append(String.format("%02x", bytes));
            }
            return stringBuilder.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
