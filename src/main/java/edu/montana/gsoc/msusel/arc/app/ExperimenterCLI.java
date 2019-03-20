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
package edu.montana.gsoc.msusel.arc.app;

import org.apache.commons.cli.*;

import java.io.IOException;
import java.util.List;

public class ExperimenterCLI {

    /**
     * Command line options object associated with this class.
     */
    private static final Options options;

    /**
     * Initializes the command line options object.
     */
    static
    {
        final Option help = Option.builder("h")
                .required(false)
                .longOpt("help")
                .desc("prints this message")
                .hasArg(false)
                .build();
        final Option output = Option.builder("o")
                .required(false)
                .longOpt("output")
                .argName("FILE")
                .desc("Name of the file to output results to.")
                .hasArg(true)
                .numberOfArgs(1)
                .build();
        final Option model = Option.builder("q")
                .required(false)
                .longOpt("quality-model")
                .desc("Prints the results to the console")
                .hasArg()
                .numberOfArgs(1)
                .argName("QM_FILE")
                .build();
        final Option config = Option.builder("c")
                .required(false)
                .longOpt("config")
                .desc("Path to configuration file.")
                .argName("FILE")
                .hasArg()
                .numberOfArgs(1)
                .build();
        options = new Options();
        ExperimenterCLI.options.addOption(help);
        ExperimenterCLI.options.addOption(output);
        ExperimenterCLI.options.addOption(model);
        ExperimenterCLI.options.addOption(config);
    }

    /**
     * Controls the execution of the LoCCounter given the command line object.
     *
     * @param line
     *            The parsed command line arguments.
     * @throws IOException
     *             If the file for output cannot be written to
     */
    public static void execute(final CommandLine line) throws IOException
    {
        //VerifierConfiguration config = null;
        String qualityModel = null;
        String output = null;

        if (line.getOptions().length == 0 || line.hasOption('h'))
        {
            printHelp();
            return;
        }

        List<String> args = line.getArgList();
        for (String arg : args)
        {
            if (arg.startsWith("-D"))
            {

            }
        }

        if (line.hasOption('o'))
        {
            output = line.getOptionValue('o');

        }
        else
        {
            //output = DEFAULT_OUTPUT;
        }

        if (line.hasOption('q'))
        {
            qualityModel = line.getOptionValue('q');
        }

        if (line.hasOption('c'))
        {
            //config = VerifierConfiguration.load(line.getOptionValue('c'));
        }
        else
        {
            //config = VerifierConfiguration.load(DEFAULT_CONFIG);
        }

//        Experimenter experimenter = new Experimenter(outputter);
//        experimenter.process(config, qualityModel, output);
//
//        if (outputter != null)
//        {
//            outputter.flush();
//            outputter.close();
//        }
    }

    /**
     * Starting point of execution.
     *
     * @param args
     *            Raw command line arguments.
     */
    public static void main(final String... args)
    {
        final CommandLineParser parser = new DefaultParser();
        try
        {
            final CommandLine line = parser.parse(ExperimenterCLI.options, args);
            execute(line);
        }
        catch (final ParseException exp)
        {
            printHelp();
        }
        catch (IOException e)
        {

        }
    }

    /**
     * Prints the help message
     */
    private static void printHelp()
    {
        final HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(
                "experimenter",
                "\nValidate and verify a quamoco quality model.\n\n",
                ExperimenterCLI.options,
                "\nMontana State University, Gianforte School of Computing (C) 2015-2017",
                true);
    }
}
