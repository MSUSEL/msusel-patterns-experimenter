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
package com.ivata.groupware.admin.script;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.ivata.groupware.admin.security.server.SecurityServerException;
import com.ivata.mask.util.CollectionHandling;
import com.ivata.mask.util.SystemException;
import com.ivata.mask.validation.ValidationError;
import com.ivata.mask.validation.ValidationErrors;
import com.ivata.mask.validation.ValidationException;
import com.ivata.mask.web.format.URLFormat;

/**
 * Simple class to let you run scripts which exist outside the JVM world.
 *
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 * @since   Apr 6, 2004
 * @version $Revision: 1.2 $
 */
public class ExternalScriptExecutor implements ScriptExecutor {

    /**
     * Refer to {@link Logger}.
     */
    private static Logger log = Logger.getLogger(ExternalScriptExecutor.class);
    /**
     * <p>
     * All environment variables to be set in the script environment.
     * </p>
     */
    String [] environmentVariables;
    /**
     * <p>
     * Full path to the location where the scripts are stored for this executor.
     * </p>
     */
    private String scriptPath;
    /**
     * <p>
     * This is used to format variables to avoid problems with command line
     * spaces.
     * </p>
     */
    URLFormat uRLFormat;
    /**
     * <p>
     * Construct a script executor to execute external scripts.
     * </p>
     *
     * @param scriptPath full path to the location where the scripts are stored
     * for this executor.
     * @param environmentVariables all environment variables to be set in the
     * script environemnt, separated by line feeds.
     * @param uRLFormat used to format variables to avoid problems with command
     * line spaces.
     */
    public ExternalScriptExecutor(URLFormat URLFormat, String scriptPath,
            String environmentVariables) {
        this.scriptPath = scriptPath;
        this.environmentVariables = (String[])
            CollectionHandling.convertFromLines(environmentVariables).toArray(new String[]{});
        this.uRLFormat = URLFormat;
    }
    /**
     * <p>Execute a command and handle any error that occurs.</p>
     *
     * @param scriptName name of the script to be executed.
     * @param argv command name and all arguments of to be executed. The first
     *     argument should always be the script name
     * @throws SecurityServerException if the command returns non-zero, or if
     * there is
     * an input/output exception.
     * @return all lines of the program output as a <code>String</code>.
     */
    public String exec(final String scriptName,
            final String[] arguments) throws SystemException {
        String [] externalArguments = new String[arguments.length + 1];

        try {
            externalArguments[0] = scriptPath
                + File.separator
                + scriptName;

            // put quotes round each of the arguments
            // TODO: this will probably not work in an Windows environment -
            // possible solutions are replacing spaces somehow here and
            // (better) providing perl/other script wrappers to parse arguments
            for (int index = 0; index < arguments.length; ++index) {
                if ((arguments[index] == null)
                        || (arguments[index].length() == 0)) {
                    externalArguments[index + 1] = "%00";
                } else {
                    externalArguments[index + 1] = uRLFormat.format(arguments[index]);
                }
            }

            Process process;
            try {
                process = Runtime.getRuntime().exec(externalArguments,
                        environmentVariables, new File(scriptPath));
            } catch (IOException e) {
                log.error(e);
                String argumentsString = CollectionHandling.convertToLines(
                        Arrays.asList(arguments), ',');
                throw new ValidationException(new ValidationError(
                        "errors.admin.script",
                        Arrays.asList(new Object[] {
                                scriptName,
                                argumentsString,
                                "IOException: "
                                    + e.getMessage()
                        })));
            }

            if (process.waitFor() != 0) {
                String errorText = extractText(process.getErrorStream());
                List lines  = CollectionHandling.convertFromLines(errorText);
                ValidationErrors errors = new ValidationErrors();
                Iterator linesIterator = lines.iterator();
                while (linesIterator.hasNext()) {
                    String line = (String) linesIterator.next();
                    // if it timed out waiting for a password, that is almost
                    // definitely a sudo issue - add a comment to help whoever
                    // installed ivata groupware.
                    if ((lines.size() == 1)
                            && "Password:".equals(line)) {
                        line += " (This looks like a user rights issue. Check "
                            + "visudo is installed properly and is set up for "
                            + "the user who is running the program. If you "
                            + "used the install script to install ivata "
                            + "groupware, change the value of USER_APP_SERVER "
                            + "at the start of the script and run "
                            + "setup.pl again.)";
                    }
                    // some scripts have been tuned to give out error message
                    // keys when they fail - see if this is one of those
                    if ((line.indexOf("error.") != -1)
                                || (line.indexOf("errors.") != -1)) {
                            List errorArguments = new Vector();
                        errorArguments.add(scriptName);
                        errorArguments.addAll(Arrays.asList(arguments));
                        errors.add(new ValidationError(
                                line,
                                errorArguments));
                    } else {
                        // nothing for it - we'll just have to use a generic
                        // 'script failed' error message
                        String argumentsString = CollectionHandling.convertToLines(
                                Arrays.asList(arguments), ',');
                        errors.add(new ValidationError(
                                "errors.admin.script",
                                Arrays.asList(new Object[] {
                                        scriptName,
                                        argumentsString,
                                        line
                                })));
                    }
                }
                throw new ValidationException(errors);
            }

            return extractText(process.getInputStream());
        } catch (IOException e) {
            throw new SystemException("There was an input/output exception:",
                e);
        } catch (InterruptedException e) {
            throw new SystemException("The script process was interrupted",
                e);
        }
    }

    /**
     * <p>Called internally to evaluate the text from the stream
     * provided.</p>
     *
     * @param stream stream containing text to extract.
     * @throws IOException thrown by <code>BufferedReader</code>.
     * @return the textual contents of the stream provided.
     */
    private String extractText(final InputStream stream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(stream));
        StringBuffer messageBuffer = new StringBuffer();
        char[] chbuf = new char[1024];
        int count;

        while ((count = in.read(chbuf)) != -1) {
            messageBuffer.append(chbuf, 0, count);
        }

        return messageBuffer.toString();
    }
}
