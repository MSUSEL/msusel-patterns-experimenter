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
package org.archive.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Logger;


/**
 * Write a message and stack trace to the 'org.archive.util.DevUtils' logger.
 *
 * @author gojomo
 * @version $Revision: 4644 $ $Date: 2006-09-20 22:40:21 +0000 (Wed, 20 Sep 2006) $
 */
public class DevUtils {
    public static Logger logger =
        Logger.getLogger(DevUtils.class.getName());

    /**
     * Log a warning message to the logger 'org.archive.util.DevUtils' made of
     * the passed 'note' and a stack trace based off passed exception.
     *
     * @param ex Exception we print a stacktrace on.
     * @param note Message to print ahead of the stacktrace.
     */
    public static void warnHandle(Throwable ex, String note) {
        logger.warning(TextUtils.exceptionToString(note, ex));
    }

    /**
     * @return Extra information gotten from current Thread.  May not
     * always be available in which case we return empty string.
     */
    public static String extraInfo() {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw); 
        final Thread current = Thread.currentThread();
        if (current instanceof Reporter) {
            Reporter tt = (Reporter)current;
            try {
                tt.reportTo(pw);
            } catch (IOException e) {
                // Not really possible w/ a StringWriter
                e.printStackTrace();
            } 
        }
        if (current instanceof ProgressStatisticsReporter) {
            ProgressStatisticsReporter tt = (ProgressStatisticsReporter)current;
            try {
                tt.progressStatisticsLegend(pw);
                tt.progressStatisticsLine(pw);
            } catch (IOException e) {
                // Not really possible w/ a StringWriter
                e.printStackTrace();
            }
        }
        pw.flush();
        return sw.toString();
    }

    /**
     * Nothing to see here, move along.
     * @deprecated  This method was never used.
     */
    @Deprecated
    public static void betterPrintStack(RuntimeException re) {
        re.printStackTrace(System.err);
    }
    
    /**
     * Send this JVM process a SIGQUIT; giving a thread dump and possibly
     * a heap histogram (if using -XX:+PrintClassHistogram).
     * 
     * Used to automatically dump info, for example when a serious error
     * is encountered. Would use 'jmap'/'jstack', but have seen JVM
     * lockups -- perhaps due to lost thread wake signals -- when using
     * those against Sun 1.5.0+03 64bit JVM. 
     */
    public static void sigquitSelf() {
        try {
            Process p = Runtime.getRuntime().exec(
                    new String[] {"perl", "-e", "print getppid(). \"\n\";"});
            BufferedReader br =
                new BufferedReader(new InputStreamReader(p.getInputStream()));
            String ppid = br.readLine();
            Runtime.getRuntime().exec(
                    new String[] {"sh", "-c", "kill -3 "+ppid}).waitFor();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
