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
- 15-minute lockout after 3 failed attempts
- Detects unlocked vault on startup
- Interactive terminal menu

## Run
```bash
Get-ChildItem -Recurse -Filter "*.java" src/ | ForEach-Object { $_.FullName } | Out-File sources.txt
javac @sources.txt
java -cp src/main/java com.mirosync.Main
```

## Project Structure
```
com.mirosync/
├── Mirosync.java   → core logic
├── folder/         → FolderManager.java
├── password/       → PasswordManager.java
├── validate/       → Validation.java
├── security/       → LockoutManager.java
├── graphic/        → Menu.java
└── R/              → ProgramMessages.java
```

## Roadmap
- [ ] Mini CLI (`miro ~info:size`, `miro ~info:count`, ...)
- [ ] Cross-platform support


## Status
> v0.3 — Lockout system and interactive menu complete.