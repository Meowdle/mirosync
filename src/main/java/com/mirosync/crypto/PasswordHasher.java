package com.mirosync.crypto;

import com.mirosync.exception.CryptoException;

public interface PasswordHasher {

    HashResult hash(char[] password) throws CryptoException;

    boolean verify(char[] password, HashResult stored) throws CryptoException;

    record HashResult(byte[] hash, byte[] salt) {}
}
