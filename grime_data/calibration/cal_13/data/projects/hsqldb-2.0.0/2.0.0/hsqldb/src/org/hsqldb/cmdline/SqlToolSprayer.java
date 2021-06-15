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

package org.hsqldb.cmdline;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

/* $Id: SqlToolSprayer.java 3481 2010-02-26 18:05:06Z fredt $ */

/**
 * Sql Tool Sprayer.
 * Invokes SqlTool.objectMain() multiple times with the same SQL.
 * Invokes for multiple urlids and/or retries.
 *
 * See JavaDocs for the main method for syntax of how to run.
 *
 * System properties used if set:
 * <UL>
 *      <LI>sqltoolsprayer.period (in ms.)</LI>
 *      <LI>sqltoolsprayer.maxtime (in ms.)</LI>
 *      <LI>sqltoolsprayer.rcfile (filepath)</LI>
 * </UL>
 *
 * @see #main(String[])
 * @version $Revision: 3481 $, $Date: 2010-02-26 13:05:06 -0500 (Fri, 26 Feb 2010) $
 * @author Blaine Simpson (blaine dot simpson at admc dot com)
 */
public class SqlToolSprayer {

    public static String LS = System.getProperty("line.separator");
    private static String SYNTAX_MSG =
        "SYNTAX:  java [-D...] SqlToolSprayer 'SQL;' [urlid1 urlid2...]\n"
        + "System properties you may use [default values]:\n"
        + "    sqltoolsprayer.period (in ms.) [500]\n"
        + "    sqltoolsprayer.maxtime (in ms.) [0]\n"
        + "    sqltoolsprayer.monfile (filepath) [none]\n"
        + "    sqltoolsprayer.rcfile (filepath) [none.  SqlTool default used.]";
    static {
        if (!LS.equals("\n")) {
            SYNTAX_MSG = SYNTAX_MSG.replaceAll("\n", LS);
        }
    }

    public static void main(String[] sa) {

        if (sa.length < 1) {
            System.err.println(SYNTAX_MSG);
            System.exit(4);
        }

        long period = ((System.getProperty("sqltoolsprayer.period") == null)
                       ? 500
                       : Integer.parseInt(
                           System.getProperty("sqltoolsprayer.period")));
        long maxtime = ((System.getProperty("sqltoolsprayer.maxtime") == null)
                        ? 0
                        : Integer.parseInt(
                            System.getProperty("sqltoolsprayer.maxtime")));
        String rcFile   = System.getProperty("sqltoolsprayer.rcfile");
        File monitorFile =
            (System.getProperty("sqltoolsprayer.monfile") == null) ? null
                                                                   : new File(
                                                                       System.getProperty(
                                                                           "sqltoolsprayer.monfile"));
        ArrayList<String> urlids = new ArrayList<String>();

        for (int i = 1; i < sa.length; i++) {
            urlids.add(sa[i]);
        }

        if (urlids.size() < 1) {
            System.err.println("No urlids specified.  Nothing to spray.");
            System.exit(5);
        }

        boolean[] status = new boolean[urlids.size()];

        String[] withRcArgs    = {
            "--sql=" +  sa[0], "--rcfile=" + rcFile, null
        };
        String[] withoutRcArgs = {
            "--sql=" + sa[0], null
        };
        String[] sqlToolArgs   = (rcFile == null) ? withoutRcArgs
                                                  : withRcArgs;
        boolean  onefailed     = false;
        long     startTime     = (new Date()).getTime();

        while (true) {
            if (monitorFile != null && !monitorFile.exists()) {
                System.err.println("Required file is gone:  " + monitorFile);
                System.exit(2);
            }

            onefailed = false;

            for (int i = 0; i < status.length; i++) {
                if (status[i]) {
                    continue;
                }

                sqlToolArgs[sqlToolArgs.length - 1] = urlids.get(i);
                // System.err.println("ARGS:"
                //      + java.util.Arrays.asList(sqlToolArgs));

                try {
                    SqlTool.objectMain(sqlToolArgs);

                    status[i] = true;

                    System.err.println("Success for instance '"
                                       + urlids.get(i) + "'");
                } catch (SqlTool.SqlToolException se) {
                    onefailed = true;
                }
            }

            if (!onefailed) {
                break;
            }

            if (maxtime == 0 || (new Date()).getTime() > startTime + maxtime) {
                break;
            }

            try {
                Thread.sleep(period);
            } catch (InterruptedException ie) {
                // Purposefully doing nothing
            }
        }

        ArrayList<String> failedUrlids = new ArrayList<String>();

        // If all statuses true, then System.exit(0);
        for (int i = 0; i < status.length; i++) {
            if (status[i] != true) {
                failedUrlids.add(urlids.get(i));
            }
        }

        if (failedUrlids.size() > 0) {
            System.err.println("Failed instances:   " + failedUrlids);
            System.exit(1);
        }

        System.exit(0);
    }
}
