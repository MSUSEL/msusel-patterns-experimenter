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

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.FieldPosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;



/**
 * Logger that writes entry on one line with less verbose date.
 * 
 * @author stack
 * @version $Revision: 6329 $, $Date: 2009-06-05 00:51:53 +0000 (Fri, 05 Jun 2009) $
 */
public class OneLineSimpleLogger extends SimpleFormatter {
    
    /**
     * Date instance.
     * 
     * Keep around instance of date.
     */
    private Date date = new Date();
    
    /**
     * Field position instance.
     * 
     * Keep around this instance.
     */
    private FieldPosition position = new FieldPosition(0);
    
    /**
     * MessageFormatter for date.
     */
    private SimpleDateFormat formatter = 
        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    
    /**
     * Persistent buffer in which we conjure the log.
     */
    private StringBuffer buffer = new StringBuffer();
    

    public OneLineSimpleLogger() {
        super();
    }
    
    public synchronized String format(LogRecord record) {
        this.buffer.setLength(0);
        this.date.setTime(record.getMillis());
        this.position.setBeginIndex(0);
        this.formatter.format(this.date, buffer, this.position);
        buffer.append(' ');
        buffer.append(record.getLevel().getLocalizedName());
        buffer.append(" thread-");
        buffer.append(record.getThreadID());
        buffer.append(' ');
        if (record.getSourceClassName() != null) {
            buffer.append(record.getSourceClassName());
        } else {
            buffer.append(record.getLoggerName());
        }
        buffer.append('.');
        String methodName = record.getSourceMethodName();
        methodName = (methodName == null || methodName.length() <= 0)?
            "-": methodName;
        buffer.append(methodName);
        buffer.append("() ");
        buffer.append(formatMessage(record));
        buffer.append(System.getProperty("line.separator"));
        if (record.getThrown() != null) {
    	    try {
    	        StringWriter writer = new StringWriter();
    	        PrintWriter printer = new PrintWriter(writer);
    	        record.getThrown().printStackTrace(printer);
    	        writer.close();
    	        buffer.append(writer.toString());
    	    } catch (Exception e) {
    	        buffer.append("Failed to get stack trace: " + e.getMessage());
    	    }
        }
        return buffer.toString();
    }
    
    public static Logger setConsoleHandler() {
        Logger logger = Logger.getLogger("");
        Handler [] hs = logger.getHandlers();
        for (int i = 0; i < hs.length; i++) {
            Handler h = hs[0];
            if (h instanceof ConsoleHandler) {
                h.setFormatter(new OneLineSimpleLogger());
            }
        }
        return logger;
    }

    /**
     * Test this logger.
     */
    public static void main(String[] args) {
        Logger logger = setConsoleHandler();
        logger = Logger.getLogger("Test");
        logger.severe("Does this come out?");
        logger.severe("Does this come out?");
        logger.severe("Does this come out?");
        logger.log(Level.SEVERE, "hello", new RuntimeException("test"));
    }
}
