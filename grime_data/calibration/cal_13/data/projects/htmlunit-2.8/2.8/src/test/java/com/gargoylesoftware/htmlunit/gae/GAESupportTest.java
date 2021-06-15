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
package com.gargoylesoftware.htmlunit.gae;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.WebClient;

/**
 * Tests for <a href="http://code.google.com/appengine/">Google App Engine</a>
 * support. Tests are run through the {@link GAETestRunner} which tries to enforce (some of) GAE rules
 * like for instance class white list.
 *
 * @version $Revision: 5652 $
 * @author Marc Guillemot
 */
@RunWith(GAETestRunner.class)
public class GAESupportTest {

    /**
     * Test that the test runner prohibits loading of some classes like
     * {@link java.net.URLStreamHandler}.
     */
    @Test(expected = NoClassDefFoundError.class)
    public void whitelist() {
        new com.gargoylesoftware.htmlunit.protocol.javascript.Handler();
    }

    /**
     * Simulates GAE white list restrictions. Fails as of HtmlUnit-2.7 due to
     * usage of java.net.URLStreamHandler (and problably other classes).
     * @throws Exception if the test fails
     */
    @Test
    public void instantiation() throws Exception {
        new WebClient();
        assertEquals("http://gaeHack_about/blank", WebClient.URL_ABOUT_BLANK.toString());
    }
}
