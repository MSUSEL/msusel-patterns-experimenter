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
package com.gargoylesoftware.htmlunit.javascript;

import java.io.StringWriter;
import java.net.URL;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Appender;
import org.apache.log4j.Layout;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.WriterAppender;
import org.junit.After;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.MockWebConnection;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebConnection;
import com.gargoylesoftware.htmlunit.WebTestCase;

/**
 * Tests for {@link DebugFrameImpl}.
 *
 * @version $Revision: 5569 $
 * @author Marc Guillemot
 */
public class DebugFrameImplTest extends WebTestCase {

    private final Logger loggerDebugFrameImpl_ = Logger.getLogger(DebugFrameImpl.class);

    private Level originalLogLevel_;
    private WebClient client_;

    /**
     * Constructor.
     * @throws Exception if an exception occurs
     */
    public DebugFrameImplTest() throws Exception {
        client_ = new WebClient(BrowserVersion.FIREFOX_3);
        client_.getJavaScriptEngine().getContextFactory().setDebugger(new DebuggerImpl());
        originalLogLevel_ = loggerDebugFrameImpl_.getLevel();
        loggerDebugFrameImpl_.setLevel(Level.TRACE);
    }

    /**
     * Cleans up the client, and resets the log to its original state.
     * @throws Exception when a problem occurs
     */
    @After
    public void tearDown() throws Exception {
        client_.getJavaScriptEngine().getContextFactory().setDebugger(null);
        client_.closeAllWindows();
        loggerDebugFrameImpl_.setLevel(originalLogLevel_);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void withCallable() throws Exception {
        final String content = "<html><head><title>debug test</title>"
            + "<script>"
            + "var counter = 0;"
            + "window.__defineGetter__('foo', function(a) { return counter++ });"
            + "alert(window.foo);"
            + "</script></head><body></body></html>";
        final WebConnection old = client_.getWebConnection();
        try {
            final MockWebConnection mock = new MockWebConnection();
            mock.setDefaultResponse(content);
            client_.setWebConnection(mock);
            client_.getPage(URL_FIRST);
        }
        finally {
            client_.setWebConnection(old);
        }
    }

    /**
     * @throws Exception if the test fails
     */
    void loggedCalls() throws Exception {
        final URL url = getClass().getResource("debugFrameImplTest.html");
        final String expectedLog = IOUtils.toString(getClass().getResourceAsStream("debugFrameImplTest.txt"));

        final StringWriter sw = new StringWriter();
        final Layout layout = new PatternLayout("%m%n");
        final Appender appender = new WriterAppender(layout, sw);
        loggerDebugFrameImpl_.addAppender(appender);
        try {
            client_.getPage(url);
        }
        finally {
            loggerDebugFrameImpl_.removeAppender(appender);
        }

        assertEquals(expectedLog, sw.toString());
    }
}
