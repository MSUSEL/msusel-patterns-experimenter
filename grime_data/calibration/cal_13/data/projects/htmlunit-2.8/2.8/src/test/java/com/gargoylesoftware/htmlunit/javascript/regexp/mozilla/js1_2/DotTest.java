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
 * Tests originally in '/js/src/tests/js1_2/regexp/dot.js'.
 *
 * @version $Revision: 5766 $
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class DotTest extends WebDriverTestCase {

    /**
     * Tests 'abcde'.match(new RegExp('ab.de')).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("abcde")
    public void test1() throws Exception {
        test("'abcde'.match(new RegExp('ab.de'))");
    }

    /**
     * Tests 'line 1\nline 2'.match(new RegExp('.+')).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("line 1")
    public void test2() throws Exception {
        test("'line 1\\nline 2'.match(new RegExp('.+'))");
    }

    /**
     * Tests 'this is a test'.match(new RegExp('.*a.*')).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("this is a test")
    public void test3() throws Exception {
        test("'this is a test'.match(new RegExp('.*a.*'))");
    }

    /**
     * Tests 'this is a *&^%$# test'.match(new RegExp('.+')).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("this is a *&^%$# test")
    public void test4() throws Exception {
        test("'this is a *&^%$# test'.match(new RegExp('.+'))");
    }

    /**
     * Tests '....'.match(new RegExp('.+')).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("....")
    public void test5() throws Exception {
        test("'....'.match(new RegExp('.+'))");
    }

    /**
     * Tests 'abcdefghijklmnopqrstuvwxyz'.match(new RegExp('.+')).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("abcdefghijklmnopqrstuvwxyz")
    public void test6() throws Exception {
        test("'abcdefghijklmnopqrstuvwxyz'.match(new RegExp('.+'))");
    }

    /**
     * Tests 'ABCDEFGHIJKLMNOPQRSTUVWXYZ'.match(new RegExp('.+')).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("ABCDEFGHIJKLMNOPQRSTUVWXYZ")
    public void test7() throws Exception {
        test("'ABCDEFGHIJKLMNOPQRSTUVWXYZ'.match(new RegExp('.+'))");
    }

    /**
     * Tests '`1234567890-=~!@#$%^&*()_+'.match(new RegExp('.+')).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("`1234567890-=~!@#$%^&*()_+")
    public void test8() throws Exception {
        test("'`1234567890-=~!@#$%^&*()_+'.match(new RegExp('.+'))");
    }

    /**
     * Tests '|\\[{]};:\"\',<>.?/'.match(new RegExp('.+')).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("|\\[{]};:\"\',<>.?/")
    public void test9() throws Exception {
        test("'|\\\\[{]};:\\\"\\',<>.?/'.match(new RegExp('.+'))");
    }

    /**
     * Tests '|\\[{]};:\"\',<>.?/'.match(/.+/).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("|\\[{]};:\"\',<>.?/")
    public void test10() throws Exception {
        test("'|\\\\[{]};:\\\"\\',<>.?/'.match(/.+/)");
    }

    private void test(final String script) throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  alert(" + script + ");\n"
            + "</script></head><body>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }
}
