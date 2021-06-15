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
 * Tests originally in '/js/src/tests/js1_2/regexp/question_mark.js'.
 *
 * @version $Revision: 5767 $
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class QuestionMarkTest extends WebDriverTestCase {

    /**
     * Tests 'abcdef'.match(new RegExp('cd?e')).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("cde")
    public void test1() throws Exception {
        test("'abcdef'.match(new RegExp('cd?e'))");
    }

    /**
     * Tests 'abcdef'.match(new RegExp('cdx?e')).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("cde")
    public void test2() throws Exception {
        test("'abcdef'.match(new RegExp('cdx?e'))");
    }

    /**
     * Tests 'pqrstuvw'.match(new RegExp('o?pqrst')).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("pqrst")
    public void test3() throws Exception {
        test("'pqrstuvw'.match(new RegExp('o?pqrst'))");
    }

    /**
     * Tests 'abcd'.match(new RegExp('x?y?z?')).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void test4() throws Exception {
        test("'abcd'.match(new RegExp('x?y?z?'))");
    }

    /**
     * Tests 'abcd'.match(new RegExp('x?ay?bz?c')).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("abc")
    public void test5() throws Exception {
        test("'abcd'.match(new RegExp('x?ay?bz?c'))");
    }

    /**
     * Tests 'abcd'.match(/x?ay?bz?c/).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("abc")
    public void test6() throws Exception {
        test("'abcd'.match(/x?ay?bz?c/)");
    }

    /**
     * Tests 'abbbbc'.match(new RegExp('b?b?b?b')).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("bbbb")
    public void test7() throws Exception {
        test("'abbbbc'.match(new RegExp('b?b?b?b'))");
    }

    /**
     * Tests '123az789'.match(new RegExp('ab?c?d?x?y?z')).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("az")
    public void test8() throws Exception {
        test("'123az789'.match(new RegExp('ab?c?d?x?y?z'))");
    }

    /**
     * Tests '123az789'.match(/ab?c?d?x?y?z/).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("az")
    public void test9() throws Exception {
        test("'123az789'.match(/ab?c?d?x?y?z/)");
    }

    /**
     * Tests '?????'.match(new RegExp('\\??\\??\\??\\??\\??')).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("?????")
    public void test10() throws Exception {
        test("'?????'.match(new RegExp('\\\\??\\\\??\\\\??\\\\??\\\\??'))");
    }

    /**
     * Tests 'test'.match(new RegExp('.?.?.?.?.?.?.?')).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("test")
    public void test11() throws Exception {
        test("'test'.match(new RegExp('.?.?.?.?.?.?.?'))");
    }

    private void test(final String script) throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  alert(" + script + ");\n"
            + "</script></head><body>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }
}
