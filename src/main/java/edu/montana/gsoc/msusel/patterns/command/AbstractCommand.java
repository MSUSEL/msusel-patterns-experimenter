package edu.montana.gsoc.msusel.patterns.command;

import lombok.Getter;

public abstract class AbstractCommand implements Command {

    @Getter
    protected String name;
}
