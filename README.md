# Mirosync

A secure folder manager that creates a hidden, password-protected folder on your system.

## How It Works
- First run: set a password → folder is created and hidden
- Next runs: enter password → folder unlocks
- Press Enter when done → folder locks and hides again

## Core Features
- SHA-256 password hashing
- Hidden folder via Windows `attrib`
- Password stored securely in `config.properties`

## Run
```bash
javac src/main/java/com/mirosync/*.java
java -cp src/main/java com.mirosync.Main
```

## Status
> v0.1 — Core system complete. Expansion planned.