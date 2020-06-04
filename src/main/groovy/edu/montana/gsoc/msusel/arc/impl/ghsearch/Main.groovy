/**
 * The MIT License (MIT)
 *
 * ISUESE Repo Research Tools
 * Copyright (c) 2015-2019 Idaho State University, Informatics and
 * Computer Science, Empirical Software Engineering Laboratory
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package edu.montana.gsoc.msusel.arc.impl.ghsearch

import com.google.common.collect.Maps
import org.reflections.Reflections

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class Main {

    static final String GHSEARCH_PKG = "edu.isu.cs.isuese.arc.impl.ghsearch"
    private Map<String, CLIProcessor> processors

    private Main() {}

    static void main(String[] args) {
        Main main = Main.instance()
        main.initialize()
        main.execute(args)
    }

    static Main instance() { return MainHolder.INSTANCE }

    private static class MainHolder {
        private static final Main INSTANCE = new Main()
    }

    void initialize() {
        Reflections reflections = new Reflections(GHSEARCH_PKG)
        Set<Class<? extends CLIProcessor>> subtypes = reflections.getSubTypesOf(CLIProcessor.class)

        for (Class<? extends CLIProcessor> subtype : subtypes) {
            try {
                CLIProcessor proc = subtype.newInstance()

                registerProcessor(proc)
            } catch (InstantiationException | IllegalAccessException ex) {

            }
        }
    }

    void execute(String args[]) {
        // Expects args to be {cmd, option...}

        if (args.length < 1) {
            printHelp()
            System.exit(-1)
        } else {
            String[] newArgs = new String[args.length - 1]
            if (newArgs.length > 0)
                System.arraycopy(args, 1, newArgs, 0, newArgs.length)
            if (processors.containsKey(args[0])) {
                processors.get(args[0]).process(newArgs)
            } else {
                System.out.println("Unknown command: " + args[0] + "...")
                System.out.println()
            }
        }
    }

    void printHelp() {
        StringBuilder builder = new StringBuilder()
        builder.append("usage: ghrtools <CMD> [options]\n")
        builder.append("\n")
        builder.append("Provides tools to process projects from git hub. The following list of commands executes known tools")
        builder.append("\n")
        for (CLIProcessor proc : processors.values()) {
            builder.append(" " + proc.getCmdDescription())
        }
        builder.append("\n")
        builder.append("Copyright (c) 2019 Idaho State University Empirical Software Engineering Lab")
    }

    void registerProcessor(CLIProcessor proc) {
        if (proc != null) {
            if (processors == null)
                processors = Maps.newHashMap()
            processors.put(proc.getCmdName(), proc)
        }
    }
}
