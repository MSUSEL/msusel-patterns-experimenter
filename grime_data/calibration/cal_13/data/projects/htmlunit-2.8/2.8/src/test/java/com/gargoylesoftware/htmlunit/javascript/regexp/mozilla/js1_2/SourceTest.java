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
package com.gargoylesoftware.htmlunit.javascript.regexp.mozilla.js1_2;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;

/**
 * Tests originally in '/js/src/tests/js1_2/regexp/source.js'.
 *
 * @version $Revision: 5767 $
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class SourceTest extends WebDriverTestCase {

    /**
     * Tests /xyz/g.source.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("xyz")
    public void test1() throws Exception {
        test("/xyz/g.source");
    }

    /**
     * Tests /xyz/.source.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("xyz")
    public void test2() throws Exception {
        test("/xyz/.source");
    }

    /**
     * Tests /abc\\def/.source.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("abc\\\\def")
    public void test3() throws Exception {
        test("/abc\\\\def/.source");
    }

    /**
     * Tests /abc[\b]def/.source.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("abc[\\b]def")
    public void test4() throws Exception {
        test("/abc[\\b]def/.source");
    }

    /**
     * Tests (new RegExp('xyz')).source.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("xyz")
    public void test5() throws Exception {
        test("(new RegExp('xyz')).source");
    }

    /**
     * Tests (new RegExp('xyz','g')).source.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("xyz")
    public void test6() throws Exception {
        test("(new RegExp('xyz','g')).source");
    }

    /**
     * Tests (new RegExp('abc\\\\def')).source.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("abc\\\\def")
    public void test7() throws Exception {
        test("(new RegExp('abc\\\\\\\\def')).source");
    }

    /**
     * Tests (new RegExp('abc[\\b]def')).source.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("abc[\\b]def")
    public void test8() throws Exception {
        test("(new RegExp('abc[\\\\b]def')).source");
    }

    private void test(final String script) throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  alert(" + script + ");\n"
            + "</script></head><body>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }
}
