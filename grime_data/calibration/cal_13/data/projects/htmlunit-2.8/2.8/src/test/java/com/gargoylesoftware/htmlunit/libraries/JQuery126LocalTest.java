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
package com.gargoylesoftware.htmlunit.libraries;

import java.net.URL;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browser;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.BrowserRunner.Tries;

/**
 * Tests for compatibility with local loading of
 * version 1.2.6 of the <a href="http://jquery.com/">jQuery JavaScript library</a>.
 *
 * @version $Revision: 5633 $
 * @author Daniel Gredler
 * @author Ahmed Ashour
 * @see JQuery126Test
 */
@RunWith(BrowserRunner.class)
public class JQuery126LocalTest extends JQueryTestBase {

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getExpectedPath() throws Exception {
        final String v = getVersion();
        final String resource = "libraries/jquery/" + v + "/local." + getBrowserVersion().getNickname() + ".txt";
        final URL url = getClass().getClassLoader().getResource(resource);
        return url.toURI().getPath();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getUrl() {
        final String v = getVersion();
        return getClass().getClassLoader().getResource("libraries/jquery/" + v + "/test/index.html").toExternalForm();
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @NotYetImplemented(Browser.IE8)
    @Tries(3)
    public void test() throws Exception {
        runTest();
    }
}
