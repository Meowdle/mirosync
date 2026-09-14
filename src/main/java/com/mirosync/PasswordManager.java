package com.mirosync;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;

public class PasswordManager {

    public String hashPassword(String password) {
        try {
            // The required algorithm is called.
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            // Each individual character is converted into a byte,
            // and they are arranged side-by-side in a byte array.
            byte[] hashBytes = messageDigest.digest(
                    password.getBytes(StandardCharsets.UTF_8) // Convert based on UTF-8.
            );
            // StringBuilder is used to facilitate string formatting.
            StringBuilder stringBuilder = new StringBuilder();
            /*
             * The entire expression cannot be converted from bytes to a text string
             * all at once. A loop is required to iterate through each element of
             * the array and convert it from a byte to a string.
             */
            for (byte singleByte : hashBytes) {
                /*
                 * We convert the bytes into a string using the String object and the format method.
                 * %02x -> [x]  Converts the numbers into hashes.
                 *      -> [02] If the number is a single digit, it places a zero next to it.
                 */
                stringBuilder.append(String.format("%02x", singleByte));
            }
            // Strings are called within the class.
            return stringBuilder.toString();

        } // For error handling in case the intended algorithm does not exist.
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
    public void savePassword() {
        final Properties properties = new Properties();
        properties.setProperty("key", "value");
        try {
            properties.store(new FileOutputStream("config.properties"), null);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
