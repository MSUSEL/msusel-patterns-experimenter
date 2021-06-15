/**
 * The MIT License (MIT)
 *
 * MSUSEL Arc Framework
 * Copyright (c) 2015-2019 Montana State University, Gianforte School of Computing,
 * Software Engineering Laboratory and Idaho State University, Informatics and
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
package org.archive.crawler;

import java.io.PrintWriter;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.commons.cli.UnrecognizedOptionException;


/**
 * Print Heritrix command-line usage message.
 *
 * @author stack
 * @version $Id: CommandLineParser.java 4551 2006-08-29 00:47:56Z stack-sf $
 */
public class CommandLineParser {
    private static final String USAGE = "Usage: ";
    private static final String NAME = "heritrix";
    private Options options = null;
    private CommandLine commandLine = null;
    private PrintWriter out = null;
    private String version = null;

    /**
     * Block default construction.
     *
     */
    private CommandLineParser() {
        super();
    }

    /**
     * Constructor.
     *
     * @param args Command-line arguments to process.
     * @param out PrintStream to write on.
     * @param version Heritrix version
     *
     * @throws ParseException Failied parse of command line.
     */
    public CommandLineParser(String [] args, PrintWriter out, String version)
    throws ParseException {
        super();

        this.out = out;
        this.version = version;

        this.options = new Options();
        this.options.addOption(new Option("h","help", false,
            "Prints this message and exits."));
        this.options.addOption(new Option("b", "bind", true,
            "Comma-separated list of IP addresses or hostnames for web server "
            + "to listen on.  Set to / to listen on all available\nnetwork "
            + "interfaces.  Default is 127.0.0.1."));
        this.options.addOption(new Option("p","port", true,
            "Port to run web user interface on.  Default: 8080."));
        this.options.addOption(new Option("a", "admin", true,
            "Login and password for web user interface administration. " +
            "Required (unless passed via the 'heritrix.cmdline.admin'\n" +
            "system property).  Pass value of the form 'LOGIN:PASSWORD'."));
        this.options.addOption(new Option("r", "run", false,
            "Put heritrix into run mode. If ORDER.XML begin crawl."));
        this.options.addOption(new Option("n", "nowui", false,
            "Put heritrix into run mode and begin crawl using ORDER.XML." +
            " Do not put up web user interface."));
        Option option = new Option("s", "selftest", true,
            "Run the integrated selftests. Pass test name to test it only" +
            " (Case sensitive: E.g. pass 'Charset' to run charset selftest).");
        option.setOptionalArg(true);
        this.options.addOption(option);

        PosixParser parser = new PosixParser();
        try {
            this.commandLine = parser.parse(this.options, args, false);
        } catch (UnrecognizedOptionException e) {
            usage(e.getMessage(), 1);
        }
    }

    /**
     * Print usage then exit.
     */
    public void usage() {
        usage(0);
    }

    /**
     * Print usage then exit.
     *
     * @param exitCode
     */
    public void usage(int exitCode) {
        usage(null, exitCode);
    }

    /**
     * Print message then usage then exit.
     *
     * The JVM exits inside in this method.
     *
     * @param message Message to print before we do usage.
     * @param exitCode Exit code to use in call to System.exit.
     */
    public void usage(String message, int exitCode) {
        outputAndExit(message, true, exitCode);
    }

    /**
     * Print message and then exit.
     *
     * The JVM exits inside in this method.
     *
     * @param message Message to print before we do usage.
     * @param exitCode Exit code to use in call to System.exit.
     */
    public void message(String message, int exitCode) {
        outputAndExit(message, false, exitCode);
    }

    /**
     * Print out optional message an optional usage and then exit.
     *
     * Private utility method.  JVM exits from inside in this method.
     *
     * @param message Message to print before we do usage.
     * @param doUsage True if we are to print out the usage message.
     * @param exitCode Exit code to use in call to System.exit.
     */
    private void outputAndExit(String message, boolean doUsage, int exitCode) {
        if (message !=  null) {
            this.out.println(message);
        }

        if (doUsage) {
            HeritrixHelpFormatter formatter =
                new HeritrixHelpFormatter();
            formatter.printHelp(this.out, 80, NAME, "Options:", this.options,
                1, 2, "Arguments:", false);
            this.out.println(" ORDER.XML       Crawl order to run.\n");
        }

        // Close printwriter so stream gets flushed.
        this.out.close();
        System.exit(exitCode);
    }

    /**
     * @return Options passed on the command line.
     */
    public Option [] getCommandLineOptions() {
        return this.commandLine.getOptions();
    }

    /**
     * @return Arguments passed on the command line.
     */
    public List getCommandLineArguments() {
        return this.commandLine.getArgList();
    }

    /**
     * @return Command line.
     */
    public CommandLine getCommandLine() {
        return this.commandLine;
    }

    /**
     * @return Returns the version.
     */
    public String getVersion() {
        return this.version;
    }
    
    /**
     * Override so can customize usage output.
     *
     * @author stack
     * @version $Id: CommandLineParser.java 4551 2006-08-29 00:47:56Z stack-sf $
     */
    public class HeritrixHelpFormatter
    extends HelpFormatter {
        public HeritrixHelpFormatter() {
            super();
        }

        public void printUsage(PrintWriter pw, int width,
                String cmdLineSyntax) {
            out.println(USAGE + NAME + " --help");
            out.println(USAGE + NAME + " --nowui ORDER.XML");
            out.println(USAGE + NAME + " [--port=#]" +
                " [--run] [--bind=IP,IP...] " +
                "--admin=LOGIN:PASSWORD \\\n\t[ORDER.XML]");
            out.println(USAGE + NAME + " [--port=#] --selftest[=TESTNAME]");
            out.println("Version: " + getVersion());
        }

        public void printUsage(PrintWriter pw, int width,
            String app, Options options) {
            this.printUsage(pw, width, app);
        }
    }
}