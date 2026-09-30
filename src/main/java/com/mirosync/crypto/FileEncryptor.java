package com.mirosync.crypto;

import com.mirosync.exception.CryptoException;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.util.Arrays;

public final class FileEncryptor {
    private static final String ALGORITHM = "AES/GCM/NoPadding";
    private static final int NONCE_SIZE = 12;
    private static final int TAG_LENGTH = 128;
    private static final byte[] MAGIC = {'M', 'I', 'O', 'E'};
    private static final byte FORMAT_VERSION = 1;

    private final SecureRandom secureRandom = new SecureRandom();

    public void encrypt(Path input, SecretKey key) throws CryptoException {
        Path output = Path.of(input + ".enc");
        Path temporary = null;
        byte[] nonce = new byte[NONCE_SIZE];
        secureRandom.nextBytes(nonce);
        byte[] header = createHeader(nonce);

        try {
            temporary = createTemporaryFile(output);
            Cipher cipher = initCipher(Cipher.ENCRYPT_MODE, key, nonce);
            cipher.updateAAD(header);

            try (
                    InputStream in = Files.newInputStream(input);
                    OutputStream fileOut = Files.newOutputStream(temporary);
                    CipherOutputStream out = new CipherOutputStream(fileOut, cipher)
            ) {
                out.write(header);
                in.transferTo(out);
            }

            moveAtomically(temporary, output);
            temporary = null;
            Files.delete(input);
        }
        catch (IOException | GeneralSecurityException e) {
            throw new CryptoException("Failed to encrypt: " + input, e);
        }
        finally {
            deleteTemporaryFile(temporary);
        }
    }

    public void decrypt(Path input, SecretKey key) throws CryptoException {
        if (!input.toString().endsWith(".enc")) {
            throw new CryptoException("Encrypted file must end with .enc: " + input);
        }

        Path output = Path.of(input.toString().substring(0, input.toString().length() - 4));
        Path temporary = null;

        try {
            temporary = createTemporaryFile(output);

            try (InputStream fileIn = Files.newInputStream(input)) {
                byte[] header = fileIn.readNBytes(MAGIC.length + 1 + NONCE_SIZE);
                validateHeader(header);

                byte[] nonce = Arrays.copyOfRange(header, MAGIC.length + 1, header.length);
                Cipher cipher = initCipher(Cipher.DECRYPT_MODE, key, nonce);
                cipher.updateAAD(header);

                try (
                        CipherInputStream in = new CipherInputStream(fileIn, cipher);
                        OutputStream out = Files.newOutputStream(temporary)
                ) {
                    in.transferTo(out);
                }
            }

            moveAtomically(temporary, output);
            temporary = null;
            Files.delete(input);
        }
        catch (IOException | GeneralSecurityException e) {
            throw new CryptoException("Failed to decrypt: " + input, e);
        }
        finally {
            deleteTemporaryFile(temporary);
        }
    }

    public void encryptAll(Path folder, SecretKey key) throws CryptoException {
        try (var files = Files.walk(folder)) {
            files.filter(Files::isRegularFile)
                    .filter(p -> !p.toString().endsWith(".enc"))
                    .forEach(file -> {
                        try {
                            encrypt(file, key);
                        }
                        catch (CryptoException e) {
                            throw new EncryptionRuntimeException(e);
                        }
                    });
        }
        catch (IOException e) {
            throw new CryptoException("Failed to walk folder: " + folder, e);
        }
        catch (EncryptionRuntimeException e) {
            throw e.cause;
        }
    }

    public void decryptAll(Path folder, SecretKey key) throws CryptoException {
        try (var files = Files.walk(folder)) {
            files.filter(Files::isRegularFile)
                    .filter(p -> p.toString().endsWith(".enc"))
                    .forEach(file -> {
                        try {
                            decrypt(file, key);
                        }
                        catch (CryptoException e) {
                            throw new EncryptionRuntimeException(e);
                        }
                    });
        }
        catch (IOException e) {
            throw new CryptoException("Failed to walk folder: " + folder, e);
        }
        catch (EncryptionRuntimeException e) {
            throw e.cause;
        }
    }

    private Cipher initCipher(int mode, SecretKey key, byte[] nonce)
            throws GeneralSecurityException {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(mode, key, new GCMParameterSpec(TAG_LENGTH, nonce));
        return cipher;
    }

    private byte[] createHeader(byte[] nonce) {
        byte[] header = new byte[MAGIC.length + 1 + NONCE_SIZE];
        System.arraycopy(MAGIC, 0, header, 0, MAGIC.length);
        header[MAGIC.length] = FORMAT_VERSION;
        System.arraycopy(nonce, 0, header, MAGIC.length + 1, NONCE_SIZE);
        return header;
    }

    private void validateHeader(byte[] header) throws CryptoException {
        if (header.length != MAGIC.length + 1 + NONCE_SIZE) {
            throw new CryptoException("Invalid encrypted file header");
        }

        if (!Arrays.equals(MAGIC, Arrays.copyOf(header, MAGIC.length))) {
            throw new CryptoException("Invalid encrypted file magic");
        }

        if (header[MAGIC.length] != FORMAT_VERSION) {
            throw new CryptoException("Unsupported encrypted file version: "
                    + header[MAGIC.length]);
        }
    }

    private Path createTemporaryFile(Path target) throws IOException {
        Path parent = target.toAbsolutePath().getParent();
        String fileName = target.getFileName().toString();
        return Files.createTempFile(parent, "." + fileName + ".", ".tmp");
    }

    private void moveAtomically(Path source, Path target) throws IOException {
        try {
            Files.move(
                    source,
                    target,
                    StandardCopyOption.ATOMIC_MOVE,
                    StandardCopyOption.REPLACE_EXISTING
            );
        }
        catch (AtomicMoveNotSupportedException e) {
            Files.move(source, target, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    private void deleteTemporaryFile(Path temporary) {
        if (temporary == null) {
            return;
        }

        try {
            Files.deleteIfExists(temporary);
        }
        catch (IOException ignored) {
            // Best-effort cleanup; the original/target file is left untouched.
        }
    }

    private static final class EncryptionRuntimeException extends RuntimeException {
        private final CryptoException cause;

        private EncryptionRuntimeException(CryptoException cause) {
            this.cause = cause;
        }
    }
}
