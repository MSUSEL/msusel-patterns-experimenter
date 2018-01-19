package edu.montana.gsoc.msusel.patterns.sikulix;

import lombok.Setter;

public abstract class AbstractCommandRunner {

    @Setter
    protected String toolName;
    @Setter
    protected String toolHome;
    @Setter
    protected String projectName;
    @Setter
    protected String reportFile;
    @Setter
    protected String sourceDirectory;
    @Setter
    protected String binaryDirectory;

    public abstract void execute();
}
