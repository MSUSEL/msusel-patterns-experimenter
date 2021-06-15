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
import com.gargoylesoftware.htmlunit.BrowserRunner.Browser;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;

/**
 * Tests originally in '/js/src/tests/js1_2/regexp/string_split.js'.
 *
 * @version $Revision: 5902 $
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class StringSplitTest extends WebDriverTestCase {

    /**
     * Tests 'a b c de f'.split(/\s/).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a,b,c,de,f")
    public void test1() throws Exception {
        test("'a b c de f'.split(/\\s/)");
    }

    /**
     * Tests 'a b c de f'.split(/\s/,3).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a,b,c")
    public void test2() throws Exception {
        test("'a b c de f'.split(/\\s/,3)");
    }

    /**
     * Tests 'a b c de f'.split(/X/).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("a b c de f")
    public void test3() throws Exception {
        test("'a b c de f'.split(/X/)");
    }

    /**
     * Tests 'dfe23iu 34 =+65--'.split(/\d+/).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("dfe,iu , =+,--")
    public void test4() throws Exception {
        test("'dfe23iu 34 =+65--'.split(/\\d+/)");
    }

    /**
     * Tests 'dfe23iu 34 =+65--'.split(new RegExp('\\d+')).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("dfe,iu , =+,--")
    public void test5() throws Exception {
        test("'dfe23iu 34 =+65--'.split(new RegExp('\\\\d+'))");
    }

    /**
     * Tests 'abc'.split(/[a-z]/).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = ",,,", IE = "")
    @NotYetImplemented(Browser.IE)
    public void test6() throws Exception {
        test("'abc'.split(/[a-z]/)");
    }

    /**
     * Tests 'abc'.split(/[a-z]/).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = ",,,", IE = "")
    @NotYetImplemented(Browser.IE)
    public void test7() throws Exception {
        test("'abc'.split(/[a-z]/)");
    }

    /**
     * Tests 'abc'.split(new RegExp('[a-z]')).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = ",,,", IE = "")
    @NotYetImplemented(Browser.IE)
    public void test8() throws Exception {
        test("'abc'.split(new RegExp('[a-z]'))");
    }

    /**
     * Tests 'abc'.split(new RegExp('[a-z]')).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = ",,,", IE = "")
    @NotYetImplemented(Browser.IE)
    public void test9() throws Exception {
        test("'abc'.split(new RegExp('[a-z]'))");
    }

    private void test(final String script) throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  alert(" + script + ");\n"
            + "</script></head><body>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }
}
