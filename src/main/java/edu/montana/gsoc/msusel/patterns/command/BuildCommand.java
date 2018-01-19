package edu.montana.gsoc.msusel.patterns.command;

import java.io.File;
import java.nio.file.*;

public abstract class BuildCommand extends AbstractCommand {

    public static void changeWorkingDirectory(String newWorkingDirectory) {
        Path p = Paths.get(newWorkingDirectory);
        if (Files.exists(p) && Files.isDirectory(p)) {
            System.setProperty("user.dir", p.toAbsolutePath().toString());
        } else {
            System.out.println("Fail");
        }
    }

    public static void main(String args[]) {
        File f = new File(".");
        System.out.println("Current Working Directory: " + f.getAbsolutePath());
        changeWorkingDirectory("/home/git");
        System.out.println("Current Working Directory: " + f.getAbsolutePath());
    }
}
