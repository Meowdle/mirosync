package com.mirosync;

import com.mirosync.auth.AuthService;
import com.mirosync.auth.LockoutService;
import com.mirosync.config.ConfigPaths;
import com.mirosync.crypto.Pbkdf2PasswordHasher;
import com.mirosync.exception.AuthException;
import com.mirosync.exception.CryptoException;
import com.mirosync.exception.MirosyncException;
import com.mirosync.exception.VaultException;
import com.mirosync.vault.FolderManager;
import com.mirosync.ui.Menu;
import com.mirosync.crypto.FileEncryptor;
import com.mirosync.crypto.KeyDerivation;
import com.mirosync.storage.ConfigStorage;
import com.mirosync.storage.LockoutStorage;
import com.mirosync.vault.Vault;
import com.mirosync.vault.VaultCamouflage;
import com.mirosync.vault.VaultSession;
import com.mirosync.vault.VaultState;

import java.io.IOException;
import java.nio.file.Path;

public class Mirosync {
    private final Vault vault;
    private final AuthService authService;
    private final LockoutService lockoutService;
    private final VaultSession session;
    private final Menu menu;

    public Mirosync(Path vaultBasePath) throws MirosyncException {
        try {
            ConfigPaths.ensureDirectoryExists();

            ConfigStorage  configStorage  = new ConfigStorage();
            LockoutStorage lockoutStorage = new LockoutStorage();

            Pbkdf2PasswordHasher hasher  = new Pbkdf2PasswordHasher();
            KeyDerivation keyDerivation  = new KeyDerivation();
            FileEncryptor fileEncryptor  = new FileEncryptor();

            this.authService    = new AuthService(hasher, configStorage);
            this.lockoutService = new LockoutService(lockoutStorage);

            VaultCamouflage camouflage = new VaultCamouflage();
            FolderManager folderManager = new FolderManager(vaultBasePath, camouflage);

            this.vault   = new Vault(folderManager, fileEncryptor, keyDerivation, authService);
            this.session = new VaultSession();
            this.menu    = new Menu();

        } catch (CryptoException | VaultException e) {
            throw new MirosyncException("Failed to initialize Mirosync", e);
        }
    }

    public void start() throws MirosyncException {
        clearTerminal();
        if (isFirstRun()) setup();
        else run();
    }

    private void setup() throws MirosyncException {
        try {
            Path customPath = menu.askVaultPath();
            if (customPath != null) {
                // reinitialize with custom path
            }
            vault.initialize();
            char[] password = menu.askNewPassword();
            authService.savePassword(password);
            session.clear();
        } catch (VaultException | AuthException e) {
            throw new MirosyncException("Setup failed", e);
        }
    }

    private void run() throws MirosyncException {
        try {
            if (lockoutService.isLocked()) {
                menu.showLocked(lockoutService.remainingMinutes());
                return;
            }
            handleMenu();
        } catch (AuthException e) {
            throw new MirosyncException("Auth error", e);
        }
    }

    private void handleMenu() throws MirosyncException {
        try {
            VaultState state = vault.getState();
            int choice = menu.showMain(state == VaultState.UNLOCKED);

            switch (choice) {
                case 1 -> handleVaultToggle(state);
                case 2 -> { /* TODO: CLI */ }
            }
        } catch (VaultException e) {
            throw new MirosyncException("Vault error", e);
        }
    }

    private void handleVaultToggle(VaultState state) throws MirosyncException {
        try {
            if (state == VaultState.UNLOCKED) {
                char[] password = session.hasPassword()
                        ? session.getPassword()
                        : menu.askPassword();
                vault.lock(password);
                session.clear();
                return;
            }

            int attempts = 3;
            while (attempts > 0) {
                char[] password = menu.askPassword();
                if (authService.verify(password)) {
                    session.setPassword(password);
                    vault.unlock(password);
                    menu.showUnlocked();
                    char[] p = session.getPassword();
                    vault.lock(p);
                    session.clear();
                    return;
                }
                attempts--;
                if (attempts == 0) {
                    lockoutService.lockOut();
                    menu.showLocked(lockoutService.remainingMinutes());
                    return;
                }
                menu.showWrongPassword(attempts);
            }
        } catch (VaultException | AuthException e) {
            throw new MirosyncException("Toggle failed", e);
        }
    }

    private boolean isFirstRun() {
        return !ConfigPaths.CONFIG_FILE.toFile().exists();
    }

    private void clearTerminal() {
        try {
            new ProcessBuilder("cmd", "/c", "cls")
                    .inheritIO().start().waitFor();
        } catch (IOException | InterruptedException ignored) {}
    }
}
