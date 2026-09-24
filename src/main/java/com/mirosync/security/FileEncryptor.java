package com.mirosync.security;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

public class FileEncryptor {
    public void encryptFile(Path inputFile, SecretKey secretKey) {

        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);

        try {
            Cipher cipher = Cipher.getInstance(
                    "AES/CBC/PKCS5Padding"
            );
            cipher.init(Cipher.ENCRYPT_MODE,
                    secretKey,
                    new IvParameterSpec(iv)
            );
            byte[] fileContent = Files.readAllBytes(inputFile);
            byte[] encrypted = cipher.doFinal(fileContent);

            Path outputFile = Path.of(inputFile + ".enc");
            try (FileOutputStream fileOutputStream
                    = new FileOutputStream(outputFile.toFile())) {
                fileOutputStream.write(iv);
                fileOutputStream.write(encrypted);
            }

            Files.delete(inputFile);
        }
        // longest catch exception I have ever written
        catch (NoSuchAlgorithmException
               | NoSuchPaddingException
               | InvalidKeyException
               | InvalidAlgorithmParameterException
               | IOException
               | IllegalBlockSizeException
               | BadPaddingException e) {
            throw new RuntimeException(e);
        }
    }

    public void decryptFile(Path inputFile, SecretKey secretKey) {
        try {

            byte[] allBytes = Files.readAllBytes(inputFile);
            byte[] iv = Arrays.copyOfRange(allBytes, 0, 16);
            byte[] encrypted = Arrays.copyOfRange(allBytes, 16, allBytes.length);

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(iv));

            byte[] decrypted = cipher.doFinal(encrypted);

            Path originalFile = Path.of(
                    inputFile.toString().replace(".enc", "")
            );
            Files.write(originalFile, decrypted);

            Files.delete(inputFile);
        }
        catch (IOException
               | BadPaddingException
               | IllegalBlockSizeException
               | InvalidKeyException
               | InvalidAlgorithmParameterException
               | NoSuchPaddingException
               | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public void encryptAll(Path folder, SecretKey key) throws Exception {
        Files.walk(folder)
                .filter(Files::isRegularFile)
                .filter(p -> !p.toString().endsWith(".enc"))
                .forEach(file -> {
                    try { encryptFile(file, key); }
                    catch (Exception e) { throw new RuntimeException(e); }
                });
    }

    public void decryptAll(Path folder, SecretKey key) throws Exception {
        Files.walk(folder)
                .filter(Files::isRegularFile)
                .filter(p -> p.toString().endsWith(".enc"))
                .forEach(file -> {
                    try { decryptFile(file, key); }
                    catch (Exception e) { throw new RuntimeException(e); }
                });
    }
}
