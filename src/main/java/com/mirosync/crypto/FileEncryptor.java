package com.mirosync.crypto;

import com.mirosync.exception.CryptoException;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;

public final class FileEncryptor {
    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final int IV_SIZE = 16;

    public void encrypt(Path input, SecretKey key) throws CryptoException {
        Path output = Path.of(input + ".enc");
        byte[] iv = generateIv();

        try (
                InputStream in   = Files.newInputStream(input);
                OutputStream out = Files.newOutputStream(output)
        ) {
            out.write(iv);
            Cipher cipher = initCipher(Cipher.ENCRYPT_MODE, key, iv);
            processStream(in, out, cipher);
            Files.delete(input);
        }
        catch (IOException e) {
            throw new CryptoException("Failed to encrypt: " + input, e);
        }
    }

    public void decrypt(Path input, SecretKey key) throws CryptoException {
        Path output = Path.of(input.toString().replace(".enc", ""));

        try (
                InputStream in   = Files.newInputStream(input);
                OutputStream out = Files.newOutputStream(output)
        ) {
            byte[] iv = in.readNBytes(IV_SIZE);
            Cipher cipher = initCipher(Cipher.DECRYPT_MODE, key, iv);
            processStream(in, out, cipher);
            Files.delete(input);
        }
        catch (IOException e) {
            throw new CryptoException("Failed to decrypt: " + input, e);
        }
    }

    public void encryptAll(Path folder, SecretKey key) throws CryptoException {
        try {
            Files.walk(folder)
                    .filter(Files::isRegularFile)
                    .filter(p -> !p.toString().endsWith(".enc"))
                    .forEach(file -> {
                        try { encrypt(file, key); }
                        catch (CryptoException e) { throw new RuntimeException(e); }
                    });
        }
        catch (IOException e) {
            throw new CryptoException("Failed to walk folder: " + folder, e);
        }
    }

    public void decryptAll(Path folder, SecretKey key) throws CryptoException {
        try {
            Files.walk(folder)
                    .filter(Files::isRegularFile)
                    .filter(p -> p.toString().endsWith(".enc"))
                    .forEach(file -> {
                        try { decrypt(file, key); }
                        catch (CryptoException e) { throw new RuntimeException(e); }
                    });
        }
        catch (IOException e) {
            throw new CryptoException("Failed to walk folder: " + folder, e);
        }
    }

    private byte[] generateIv() {
        byte[] iv = new byte[IV_SIZE];
        new SecureRandom().nextBytes(iv);
        return iv;
    }

    private Cipher initCipher(int mode, SecretKey key, byte[] iv) throws CryptoException {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(mode, key, new IvParameterSpec(iv));
            return cipher;
        }
        catch (GeneralSecurityException e) {
            throw new CryptoException("Failed to initialize cipher", e);
        }
    }

    private void processStream(InputStream in, OutputStream out, Cipher cipher)
            throws CryptoException {
        try (CipherOutputStream cos = new CipherOutputStream(out, cipher)) {
            in.transferTo(cos);
        }
        catch (IOException e) {
            throw new CryptoException("Failed to process stream", e);
        }
    }
}
