package com.mirosync.vault;

import java.util.Arrays;

public final class VaultSession {
    private char[] password;

    public void setPassword(char[] password) {
        this.password = Arrays.copyOf(password, password.length);
    }

    public char[] getPassword() {
        if (password == null) return null;
        return Arrays.copyOf(password, password.length);
    }

    public boolean hasPassword() {
        return password != null;
    }

    public void clear() {
        if (password != null) {
            Arrays.fill(password, '\0');
            password = null;
        }
    }
}
