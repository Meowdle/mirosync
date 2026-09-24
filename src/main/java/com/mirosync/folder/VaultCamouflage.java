package com.mirosync.folder;

import java.io.IOException;

public class VaultCamouflage {

    private static final String CAMOUFLAGED_NAME = "$SysCache.tmp";

    public String getCamouflagedName() {
        return CAMOUFLAGED_NAME;
    }

    public void applySystemAttributes(String path) {
        try {
            Process process = new ProcessBuilder(
                    "attrib", "+h", "+s", path
            ).inheritIO().start();

            process.waitFor();
        }
        catch (RuntimeException
               | IOException
               | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void removeSystemAttributes(String path) {
        try {
            Process process = new ProcessBuilder(
                    "attrib", "-h", "-s", path
            ).inheritIO().start();

            process.waitFor();
        }
        catch (RuntimeException
               | IOException
               | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
