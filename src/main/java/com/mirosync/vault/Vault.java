package com.mirosync.vault;

import com.mirosync.auth.AuthService;
import com.mirosync.crypto.FileEncryptor;
import com.mirosync.crypto.KeyDerivation;
import com.mirosync.exception.AuthException;
import com.mirosync.exception.CryptoException;
import com.mirosync.exception.VaultException;

import javax.crypto.SecretKey;
import java.util.Arrays;

public final class Vault {

    private final FolderManager folderManager;
    private final FileEncryptor fileEncryptor;
    private final KeyDerivation keyDerivation;
    private final AuthService authService;

    public Vault(
            FolderManager folderManager,
            FileEncryptor fileEncryptor,
            KeyDerivation keyDerivation,
            AuthService authService
    ) {
        this.folderManager = folderManager;
        this.fileEncryptor = fileEncryptor;
        this.keyDerivation = keyDerivation;
        this.authService   = authService;
    }

    public void initialize() throws VaultException {
        folderManager.create();
        folderManager.lock();
    }

    public void unlock(char[] password) throws VaultException {
        try {

            byte[] salt = authService.loadSalt();
            SecretKey key = keyDerivation.derive(password, salt);
            fileEncryptor.decryptAll(folderManager.getVaultPath(), key);

            folderManager.unlock();
        }
        catch (AuthException | CryptoException e) {
            throw new VaultException("Failed to unlock vault", e);
        }
        finally {
            Arrays.fill(password, '\0');
        }
    }

    public void lock(char[] password) throws VaultException {
        try {

            byte[] salt = authService.loadSalt();
            SecretKey key = keyDerivation.derive(password, salt);
            fileEncryptor.encryptAll(folderManager.getVaultPath(), key);

            folderManager.lock();
        }
        catch (AuthException | CryptoException e) {
            throw new VaultException("Failed to lock vault", e);
        }
        finally {
            Arrays.fill(password, '\0');
        }
    }

    public VaultState getState() throws VaultException {
        return folderManager.getState();
    }
}
