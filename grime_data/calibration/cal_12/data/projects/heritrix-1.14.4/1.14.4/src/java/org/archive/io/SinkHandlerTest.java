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

import java.io.ByteArrayInputStream;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import junit.framework.TestCase;

public class SinkHandlerTest extends TestCase {
    private static final Logger LOGGER =
        Logger.getLogger(SinkHandlerTest.class.getName());
    
    protected void setUp() throws Exception {
        super.setUp();
        String logConfig = "handlers = " +
            "org.archive.io.SinkHandler\n" +
            "org.archive.io.SinkHandler.level = ALL";
        ByteArrayInputStream bais =
            new ByteArrayInputStream(logConfig.getBytes());
        LogManager.getLogManager().readConfiguration(bais);
    }
    
    public void testLogging() throws Exception {
        LOGGER.severe("Test1");
        LOGGER.severe("Test2");
        LOGGER.warning("Test3");
        RuntimeException e = new RuntimeException("Nothing exception");
        LOGGER.log(Level.SEVERE, "with exception", e);
        SinkHandler h = SinkHandler.getInstance();
        assertEquals(4, h.getAllUnread().size());
//        SinkHandlerLogRecord shlr = h.get(3);
//        h.remove(3);
        SinkHandlerLogRecord shlr = h.getAllUnread().get(3);
        h.remove(shlr.getSequenceNumber());
        assertEquals(3, h.getAllUnread().size());
        h.publish(shlr);
        assertEquals(4, h.getAllUnread().size());
    }
    /*
    public void testToString() throws Exception {
        RuntimeException e = new RuntimeException("Some-Message");
        LOGGER.log(Level.SEVERE, "With-Exception", e);
        SinkHandler h = SinkHandler.getInstance();
        System.out.print(((SeenLogRecord)h.getSink().get(0)).toString());
        LOGGER.log(Level.SEVERE, "No-Exception");
        System.out.print(((SeenLogRecord)h.getSink().get(1)).toString());
    }*/
}
