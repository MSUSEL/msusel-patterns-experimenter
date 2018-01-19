package edu.montana.gsoc.msusel.patterns.command.impl;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CommandUtils {

    public static String normalizePathString(String path) {
        String sep = File.separator;
        if (!path.endsWith(sep)) {
            path += sep;
        }
        return path;
    }

    public static boolean verifyFileExists(String path, String file) {
        Path p = Paths.get(path, file);
        return Files.exists(p);
    }
}
