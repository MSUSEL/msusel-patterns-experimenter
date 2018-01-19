package edu.montana.gsoc.msusel.patterns.command;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@Builder(buildMethodName = "create")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"commands"})
@ToString(exclude = {"commands"})
public class Phase {

    @Singular
    private List<Command> commands;
    @Getter
    private String name;

    public void execute() {

        for (Command cmd : commands) {
            log.info(String.format("Starting %s command:", cmd.getName()));
            //cmd.execute();
            log.info(String.format("Finished %s command.", cmd.getName()));
        }
    }

}
