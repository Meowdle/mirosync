package com.mirosync.storage;

import com.mirosync.exception.StorageException;

import java.util.Properties;

public interface Storage {
    void save(Properties data) throws StorageException;
    Properties load() throws StorageException;
}
