package edu.montana.gsoc.msusel.patterns.command;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.Executor;

import java.io.IOException;

@NoArgsConstructor
@AllArgsConstructor
public abstract class PrimaryAnalysisCommand extends AbstractCommand {

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

}
