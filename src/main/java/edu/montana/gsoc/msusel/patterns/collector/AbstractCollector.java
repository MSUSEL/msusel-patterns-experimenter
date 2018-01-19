package edu.montana.gsoc.msusel.patterns.collector;

import edu.montana.gsoc.msusel.patterns.command.Command;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
public abstract class AbstractCollector implements Collector, Command {

    @Setter
    protected String resultsFile;
    @Getter
    @Setter
    protected String name;

}
