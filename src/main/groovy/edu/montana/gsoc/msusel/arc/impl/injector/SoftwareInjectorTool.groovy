package edu.montana.gsoc.msusel.arc.impl.injector

import edu.montana.gsoc.msusel.arc.ArcContext
import edu.montana.gsoc.msusel.arc.tool.CommandOnlyTool

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class SoftwareInjectorTool extends CommandOnlyTool {

    SoftwareInjectorTool(ArcContext context) {
        super(context)
    }

    @Override
    void init() {
        context.registerCommand(new GrimeInjectorCommand())
    }
}
