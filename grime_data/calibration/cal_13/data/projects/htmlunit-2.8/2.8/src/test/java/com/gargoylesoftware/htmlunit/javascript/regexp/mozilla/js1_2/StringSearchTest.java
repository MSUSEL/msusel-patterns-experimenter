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
 * Tests originally in '/js/src/tests/js1_2/regexp/string_search.js'.
 *
 * @version $Revision: 5902 $
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class StringSearchTest extends WebDriverTestCase {

    /**
     * Tests 'abcdefg'.search(/d/).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("3")
    public void test1() throws Exception {
        test("'abcdefg'.search(/d/)");
    }

    /**
     * Tests 'abcdefg'.search(/x/).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("-1")
    public void test2() throws Exception {
        test("'abcdefg'.search(/x/)");
    }

    /**
     * Tests 'abcdefg123456hijklmn'.search(/\d+/).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("7")
    public void test3() throws Exception {
        test("'abcdefg123456hijklmn'.search(/\\d+/)");
    }

    /**
     * Tests 'abcdefg123456hijklmn'.search(new RegExp()).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void test4() throws Exception {
        test("'abcdefg123456hijklmn'.search(new RegExp())");
    }

    /**
     * Tests 'abc'.search(new RegExp('$')).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("3")
    public void test5() throws Exception {
        test("'abc'.search(new RegExp('$'))");
    }

    /**
     * Tests 'abc'.search(new RegExp('^')).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("0")
    public void test6() throws Exception {
        test("'abc'.search(new RegExp('^'))");
    }

    /**
     * Tests 'abc1'.search(/.\d/).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("2")
    public void test7() throws Exception {
        test("'abc1'.search(/.\\d/)");
    }

    /**
     * Tests 'abc1'.search(/\d{2}/).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("-1")
    public void test8() throws Exception {
        test("'abc1'.search(/\\d{2}/)");
    }

    private void test(final String script) throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  alert(" + script + ");\n"
            + "</script></head><body>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }
}
