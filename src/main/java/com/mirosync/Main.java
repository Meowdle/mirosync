package com.mirosync;

import com.mirosync.exception.MirosyncException;

public class Main {
    public static void main(String[] args) {
        try {
            new Mirosync(null).start();
        }
        catch (MirosyncException e) {
            System.err.println("Fatal error: "
                    + e.getMessage()
            );
        }
    }
}