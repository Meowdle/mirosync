package com.mirosync.validate;

import com.mirosync.folder.FolderManager;
import com.mirosync.password.PasswordHasher;
import com.mirosync.password.PasswordStorage;

import java.io.IOException;

public class Validation {

    private final FolderManager folderManager;
    private final PasswordHasher passwordHasher;
    private final PasswordStorage passwordStorage;

    public Validation(FolderManager folderManager,
                      PasswordHasher passwordHasher,
                      PasswordStorage passwordStorage) {

        this.folderManager   = folderManager;
        this.passwordHasher  = passwordHasher;
        this.passwordStorage = passwordStorage;
    }
    public boolean passwordValidator(String password) {
        return passwordHasher.verify(
                password,
                passwordStorage.loadHash(),
                passwordStorage.loadSalt()
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
