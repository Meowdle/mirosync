package com.mirosync.validate;

import com.mirosync.password.PasswordManager;

import java.util.Objects;

public class Validation {
    private final PasswordManager passwordManager;
    public Validation(PasswordManager passwordManager) {
        this.passwordManager = passwordManager;
    }
    public boolean passwordValidator(String password) {
        return Objects.equals(
                passwordManager.hashPassword(password),
                passwordManager.loadPassword()
        );
    }
}
