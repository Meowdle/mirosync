package com.mirosync.vault;

import com.mirosync.exception.VaultException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class FolderManager {
    private static final Path DEFAULT_BASE = Path.of(
            System.getProperty("user.home"), "Mirosync"
    );

    private final Path vaultPath;
    private final VaultCamouflage camouflage;

    public FolderManager(Path basePath, VaultCamouflage camouflage) {
        this.camouflage = camouflage;
        this.vaultPath  = (basePath != null
                ? basePath
                : DEFAULT_BASE
        ).resolve(camouflage.getName());
    }

    public Path getVaultPath() {
        return vaultPath;
    }

    public void create() throws VaultException {
        try {
            if (Files.exists(vaultPath)) return;

            Files.createDirectories(vaultPath);
        }
        catch (IOException e) {
            throw new VaultException("Failed to create vault folder", e);
        }
    }

    public void lock() throws VaultException {
        camouflage.apply(vaultPath);
    }

    public void unlock() throws VaultException {
        camouflage.remove(vaultPath);
    }

    public VaultState getState() throws VaultException {
        try {
            if (!Files.exists(vaultPath)) return VaultState.UNINITIALIZED;
            boolean hidden = (boolean) Files.getAttribute(vaultPath, "dos:hidden");

            return hidden
                    ? VaultState.LOCKED
                    : VaultState.UNLOCKED;
        }
        catch (IOException e) {
            throw new VaultException("Failed to get vault state", e);
        }
    }
}
