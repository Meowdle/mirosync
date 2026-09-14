package com.mirosync;

import java.io.FileInputStream;
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

    public void savePassword(String hashedPassword) {
        /*
         * A standard class is a place designed for storing and reading [key-value] pairs.
         * [OUTPUT] -> simple text file
         *
         * The `final` keyword is used to prevent an object from undergoing sudden changes.
         */
        final Properties properties = new Properties();
        // It stores a key-value pair in memory; it hasn't been written to disk yet.
        properties.setProperty("hashed_password", hashedPassword);
        try (
                FileOutputStream fileOutputStream = new FileOutputStream
                        ("config.properties")
        ) {
            /*
             * It writes the information stored in memory to a file.
             * properties.store(x,y) -> [x] Where to write (a stream to the file)
             *                       -> [y] An optional comment at the top of the file
             */
            properties.store(fileOutputStream, null);

        } // If an error occurs during the file creation process, it will be handled.
        catch (IOException e) {
            /*
             * TODO:
             *  - Full handling of file creation errors
             *  - If there is an error, try creating the file again
             */
            throw new RuntimeException(e);
        }
    }
    public String loadPassword() {
        final Properties properties = new Properties();
        /*
         * With try-with-resource, the streams are automatically closed
         * once the task is complete.
         */
        try (
                FileInputStream fileInputStream = new FileInputStream
                        ("config.properties")
        ) {
            // It reads the file and loads it into memory.
            properties.load(fileInputStream);

            // After loading, it returns the value of a key.
            return properties.getProperty("hashed_password");

        } // To handle file errors
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
