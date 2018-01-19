package edu.montana.gsoc.msusel.patterns.command.impl;

public class BadPathToToolException extends Throwable {
    public BadPathToToolException(String format) {
        super(format);
    }
}
