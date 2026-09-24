# Mirosync

A password-protected folder that hides itself when locked.

## How It Works
- **First run:** set a password → vault is created and hidden
- **Next runs:** enter password → vault unlocks
- **When done:** vault locks and disappears
- **Unexpected close:** detects if vault is still open on next launch

## Core Features
- PBKDF2 password hashing with salt
- AES-256 file encryption on lock/unlock
- Vault camouflage via `attrib +h +s` (hidden + system)
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
├── password/       → PasswordHasher.java, PasswordStorage.java
├── validate/       → Validation.java
├── security/       → FileEncryptor.java, KeyDerivation.java, LockoutManager.java
├── graphic/        → Menu.java
└── R/              → ProgramMessages.java
```


## Roadmap

### Security & Stability
- [x] PBKDF2 password hashing
- [x] AES-256 file encryption
- [x] Vault camouflage
- [ ] Secure config storage in `AppData`
- [ ] Proper exception handling

### Features
- [ ] Mini CLI (`miro ~info:size`, `miro ~info:count`, ...)
- [ ] Cross-platform support (Linux / macOS)
- [ ] Multiple vault support

## Status
> v0.5 — AES-256 encryption and vault camouflage complete.