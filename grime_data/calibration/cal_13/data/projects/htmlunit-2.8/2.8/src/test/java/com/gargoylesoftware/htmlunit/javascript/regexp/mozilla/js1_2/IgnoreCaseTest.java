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
 * Tests originally in '/js/src/tests/js1_2/regexp/ignoreCase.js'.
 *
 * @version $Revision: 5766 $
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class IgnoreCaseTest extends WebDriverTestCase {

    /**
     * Tests /xyz/i.ignoreCase.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void test1() throws Exception {
        test("/xyz/i.ignoreCase");
    }

    /**
     * Tests /xyz/.ignoreCase.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void test2() throws Exception {
        test("/xyz/.ignoreCase");
    }

    /**
     * Tests 'ABC def ghi'.match(/[a-z]+/ig).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("ABC,def,ghi")
    public void test3() throws Exception {
        test("'ABC def ghi'.match(/[a-z]+/ig)");
    }

    /**
     * Tests 'ABC def ghi'.match(/[a-z]+/i).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("ABC")
    public void test4() throws Exception {
        test("'ABC def ghi'.match(/[a-z]+/i)");
    }

    /**
     * Tests 'ABC def ghi'.match(/([a-z]+)/ig).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("ABC,def,ghi")
    public void test5() throws Exception {
        test("'ABC def ghi'.match(/([a-z]+)/ig)");
    }

    /**
     * Tests 'ABC def ghi'.match(/([a-z]+)/i).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("ABC,ABC")
    public void test6() throws Exception {
        test("'ABC def ghi'.match(/([a-z]+)/i)");
    }

    /**
     * Tests 'ABC def ghi'.match(/[a-z]+/).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("def")
    public void test7() throws Exception {
        test("'ABC def ghi'.match(/[a-z]+/)");
    }

    /**
     * Tests (new RegExp('xyz','i')).ignoreCase.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void test8() throws Exception {
        test("(new RegExp('xyz','i')).ignoreCase");
    }

    /**
     * Tests (new RegExp('xyz')).ignoreCase.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void test9() throws Exception {
        test("(new RegExp('xyz')).ignoreCase");
    }

    /**
     * Tests 'ABC def ghi'.match(new RegExp('[a-z]+','ig')).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("ABC,def,ghi")
    public void test10() throws Exception {
        test("'ABC def ghi'.match(new RegExp('[a-z]+','ig'))");
    }

    /**
     * Tests 'ABC def ghi'.match(new RegExp('[a-z]+','i')).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("ABC")
    public void test11() throws Exception {
        test("'ABC def ghi'.match(new RegExp('[a-z]+','i'))");
    }

    /**
     * Tests 'ABC def ghi'.match(new RegExp('([a-z]+)','ig')).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("ABC,def,ghi")
    public void test12() throws Exception {
        test("'ABC def ghi'.match(new RegExp('([a-z]+)','ig'))");
    }

    /**
     * Tests 'ABC def ghi'.match(new RegExp('([a-z]+)','i')).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("ABC,ABC")
    public void test13() throws Exception {
        test("'ABC def ghi'.match(new RegExp('([a-z]+)','i'))");
    }

    /**
     * Tests 'ABC def ghi'.match(new RegExp('[a-z]+')).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("def")
    public void test14() throws Exception {
        test("'ABC def ghi'.match(new RegExp('[a-z]+'))");
    }

    private void test(final String script) throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  alert(" + script + ");\n"
            + "</script></head><body>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }
}
