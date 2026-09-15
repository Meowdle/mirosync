# Mirosync

A password-protected folder that hides itself when locked.

## How It Works
- **First run:** set a password → vault is created and hidden
- **Next runs:** enter password → vault unlocks
- **When done:** vault locks and disappears
- **Unexpected close:** detects if vault is still open on next launch

## Core Features
- SHA-256 password hashing
- Hidden folder via Windows `attrib`
- Password stored securely in `config.properties`
- 3 retry attempts on wrong password
- Detects unlocked vault on startup

## Run
```bash
Get-ChildItem -Recurse -Filter "*.java" src/ | ForEach-Object { $_.FullName } | Out-File sources.txt
javac @sources.txt
java -cp src/main/java com.mirosync.Main
```

## Project Structure
```
com.mirosync/
├── core/       → Mirosync.java
├── folder/     → FolderManager.java
├── password/   → PasswordManager.java
├── validate/   → Validation.java
└── R/          → ProgramMessages.java
```

## Status
> v0.2 — Refactored. Menu and custom path selection coming next.