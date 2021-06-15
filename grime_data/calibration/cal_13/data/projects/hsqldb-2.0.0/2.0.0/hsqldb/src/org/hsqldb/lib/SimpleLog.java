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

package org.hsqldb.lib;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

import org.hsqldb.HsqlDateTime;

/**
 * Simple log for recording abnormal events in persistence<p>
 * Log levels, LOG_NONE, LOG_ERROR, and LOG_NORMAL are currently supported.<p>
 * LOG_ERROR corresponds to property value 1 and logs main database events plus
 * any major errors encountered in operation.
 * LOG_NORMAL corresponds to property value 2 and logs additional normal events
 * and minor errors.
 *
 * @author Fred Toussi (fredt@users dot sourceforge.net)
 * @version 1.9.0
 * @since 1.8.0
 */
public class SimpleLog {

    public static final int LOG_NONE   = 0;
    public static final int LOG_ERROR  = 1;
    public static final int LOG_NORMAL = 2;
    private PrintWriter     writer;
    private int             level;
    private boolean         isSystem;
    private String          filePath;

    public SimpleLog(String path, int level) {

        this.isSystem = path == null;
        this.filePath = path;

        setLevel(level);
    }

    private void setupWriter() {

        if (level == LOG_NONE) {
            close();

            return;
        }

        if (writer == null) {
            if (isSystem) {
                writer = new PrintWriter(System.out);
            } else {
                File file = new File(filePath);

                setupLog(file);
            }
        }
    }

    private void setupLog(File file) {

        try {
            FileUtil.getFileUtil().makeParentDirectories(file);

            writer = new PrintWriter(new FileWriter(file.getPath(), true),
                                     true);
        } catch (Exception e) {
            isSystem = true;
            writer   = new PrintWriter(System.out);
        }
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {

        this.level = level;

        setupWriter();
    }

    public PrintWriter getPrintWriter() {
        return writer;
    }

    public synchronized void sendLine(int atLevel, String message) {

        if (level >= atLevel) {
            writer.println(HsqlDateTime.getSytemTimeString() + " " + message);
        }
    }

    public synchronized void logContext(int atLevel, String message) {

        if (level < atLevel) {
            return;
        }

        String info = HsqlDateTime.getSytemTimeString();

//#ifdef JAVA4
        Throwable           temp     = new Throwable();
        StackTraceElement[] elements = temp.getStackTrace();

        if (elements.length > 1) {
            info += " " + elements[1].getClassName() + "."
                    + elements[1].getMethodName();
        }

//#endif JAVA4
        writer.println(info + " " + message);
    }

    public synchronized void logContext(Throwable t, String inMessage) {

        String message = inMessage;    // We may change this

        if (level == LOG_NONE) {
            return;
        }

        String info = HsqlDateTime.getSytemTimeString();

//#ifdef JAVA4
        Throwable           temp     = new Throwable();
        StackTraceElement[] elements = temp.getStackTrace();

        if (elements.length > 1) {
            info += " " + elements[1].getClassName() + "."
                    + elements[1].getMethodName();
        }

        elements = t.getStackTrace();

        if (elements.length > 0) {
            info += " " + elements[0].getClassName() + "."
                    + elements[0].getMethodName();
        }

//#endif JAVA4
        if (message == null) {
            message = "";
        }

        if (writer != null) {
            writer.println(info + " " + t.toString() + " " + message);
        }
    }

    public void flush() {

        if (writer != null) {
            writer.flush();
        }
    }

    public void close() {

        if (writer != null && !isSystem) {
            writer.close();
        }

        writer = null;
    }
}
