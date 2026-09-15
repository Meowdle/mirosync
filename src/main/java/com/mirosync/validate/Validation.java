package com.mirosync.validate;

import com.mirosync.folder.FolderManager;
import com.mirosync.password.PasswordManager;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;

public class Validation {

    private final PasswordManager passwordManager;
    private final FolderManager folderManager;

    public Validation(PasswordManager passwordManager,
                      FolderManager folderManager) {

        this.passwordManager = passwordManager;
        this.folderManager = folderManager;
    }
    public boolean passwordValidator(String password) {
        return Objects.equals(
                passwordManager.hashPassword(password),
                passwordManager.loadPassword()
        );
    }
    public boolean isVaultOpen() {
        try {
            return folderManager.isVisible();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
