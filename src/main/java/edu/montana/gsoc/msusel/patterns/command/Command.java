package edu.montana.gsoc.msusel.patterns.command;

import edu.montana.gsoc.msusel.patterns.command.impl.BadPathToToolException;

public interface Command {

    void execute();

    String getName();
}
