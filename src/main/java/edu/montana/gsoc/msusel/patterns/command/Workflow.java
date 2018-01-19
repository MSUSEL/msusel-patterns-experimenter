package edu.montana.gsoc.msusel.patterns.command;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@Builder(buildMethodName = "create")
@EqualsAndHashCode(exclude = {"phases"})
@ToString(exclude = {"phases"})
@AllArgsConstructor
@NoArgsConstructor
public class Workflow {

    @Singular
    private List<Phase> phases;
    @Getter
    private String name;

    public void execute() {

        for (Phase phase : phases) {
            log.info(String.format("Starting phase: %s", phase.getName()));
            phase.execute();
            log.info(String.format("Finished phase: %s", phase.getName()));
        }
    }

    public static void main(String args[])
    {

    }
}
