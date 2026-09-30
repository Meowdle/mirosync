package com.mirosync.vault;

import com.mirosync.exception.VaultException;

import java.io.IOException;
import java.nio.file.Path;

public final class VaultCamouflage {
    private static final String CAMOUFLAGED_NAME = "$SysCache.tmp";

    public String getName() {
        return CAMOUFLAGED_NAME;
    }

    public void apply(Path path) throws VaultException {
        runAttrib("+h", "+s", path);
    }

    public void remove(Path path) throws VaultException {
        runAttrib("-h", "-s", path);
    }

    private void runAttrib(String h, String s, Path path) throws VaultException {
        try {
            int exit = new ProcessBuilder("attrib", h, s, path.toString())
                    .inheritIO()
                    .start()
                    .waitFor();

            if (exit != 0)
                throw new VaultException("attrib failed with exit code: " + exit, null);

        }
        catch (IOException | InterruptedException e) {
            throw new VaultException("Failed to run attrib", e);
        }
    }
}
