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

import java.io.StringWriter;
import java.io.PrintWriter;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * An implementation of java.util.logging.Formatter very close to
 * SimpleFormatter.
 *
 * The features here are optional timestamping, sortable numeric time stamp
 * text, and no indication of invoking source code location (logger ID,
 * class name, method name, etc.).
 *
 * @see Formatter
 * @see SimpleFormatter
 * @author Blaine Simpson (blaine dot simpson at admc dot com)
 */
public class BasicTextJdkLogFormatter extends Formatter {
    protected boolean withTime = true;
    public static String LS = System.getProperty("line.separator");

    protected SimpleDateFormat sdf =
            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");


    public BasicTextJdkLogFormatter(boolean withTime) {
        this.withTime = withTime;
    }

    public BasicTextJdkLogFormatter() {
        // Intentionally empty
    }

    public String format(LogRecord record) {
        StringBuilder sb = new StringBuilder();
        if (withTime) {
            sb.append(sdf.format(new Date(record.getMillis())) + "  ");
        }
        sb.append(record.getLevel() + "  " + formatMessage(record));
        if (record.getThrown() != null) {
            StringWriter sw = new StringWriter();
            record.getThrown().printStackTrace(new PrintWriter(sw));
            sb.append(LS + sw);
        }
        return sb.toString() + LS;
        // This uses platform-specific line-separator, the same as
        // SimpleLogger does.
    }
}
