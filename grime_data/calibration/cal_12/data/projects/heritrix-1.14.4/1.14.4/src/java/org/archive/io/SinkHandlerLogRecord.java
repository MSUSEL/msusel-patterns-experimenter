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
package org.archive.io;

import java.io.StringWriter;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import org.archive.crawler.framework.ToeThread;

/**
 * Version of LogRecord used by SinkHandler.
 * Adds being able to mark the LogRecord as already-read and timestamping time
 * of creation. Also adds a different {@link #toString()} implementation.
 * Delegates all other calls to the passed LogRecord.
 * @author stack
 * @version $Date: 2006-08-15 04:39:00 +0000 (Tue, 15 Aug 2006) $ $Version$
 */
public class SinkHandlerLogRecord extends LogRecord {
    private static final long serialVersionUID = -7782942650334713560L;
    boolean read = false;
    private final LogRecord delegatee;
    private final Date creationTime = new Date();
    private static final int SHORT_MSG_LENGTH = 80;
    
    protected SinkHandlerLogRecord() {
        this(null);
    }

    public SinkHandlerLogRecord(final LogRecord record) {
        super(record.getLevel(), record.getMessage());
        // if available, append current processor name to message
        // [ 1108006 ] alerts should show current processor
        // http://sourceforge.net/tracker/index.php?func=detail&aid=1108006&group_id=73833&atid=539102
        if(Thread.currentThread() instanceof ToeThread) {
            String newMessage = this.getMessage();
            ToeThread tt = (ToeThread) Thread.currentThread();
            newMessage = newMessage + " (in thread '"+tt.getName()+"'";
            if(tt.getCurrentProcessorName().length()>0) {
                newMessage = newMessage + "; in processor '"
                    +tt.getCurrentProcessorName() + "'";
            }
            newMessage = newMessage + ")";
            this.setMessage(newMessage);
        }
        this.delegatee = record;
    }
    
    public boolean equals(final long id) {
        return id == getSequenceNumber();
    }
    
    public boolean equals(final SinkHandlerLogRecord compare) {
        return equals(compare.getSequenceNumber());
    }
    
    public boolean isRead() {
        return this.read;
    }

    /**
     * Mark alert as seen (That is, isNew() no longer returns true).
     */
    public void setRead() {
        this.read = true;
    }
    
    /**
     * @return Time of creation
     */
    public Date getCreationTime() {
        return this.creationTime;
    }
    
    public Level getLevel() {
        return this.delegatee.getLevel();
    }
    
    public String getLoggerName() {
        return this.delegatee.getLoggerName();
    }
    
    public String getShortMessage() {
        String msg = getMessage();
        return msg == null || msg.length() < SHORT_MSG_LENGTH?
                msg: msg.substring(0, SHORT_MSG_LENGTH) + "...";
    }
    
    public Throwable getThrown() {
        return this.delegatee.getThrown();
    }
    
    public String getThrownToString() {
        StringWriter sw = new StringWriter();
        Throwable t = getThrown();
        if (t == null) {
            sw.write("No associated exception.");
        } else {
            String tStr = t.toString();
            sw.write(tStr);
            if (t.getMessage() != null && t.getMessage().length() > 0 &&
                    !tStr.endsWith(t.getMessage())) {
                sw.write("\nMessage: ");
                sw.write(t.getMessage());
            }
            if (t.getCause() != null) {
                sw.write("\nCause: ");
                t.getCause().printStackTrace(new java.io.PrintWriter(sw));
            }
            sw.write("\nStacktrace: ");
            t.printStackTrace(new java.io.PrintWriter(sw));
        }
        return sw.toString();
    }
    
    public String toString() {
        StringWriter sw = new StringWriter();
        sw.write(getLevel().toString());
        sw.write(" ");
        sw.write(getMessage());
        sw.write(getThrownToString());
        return sw.toString();
    }
}
