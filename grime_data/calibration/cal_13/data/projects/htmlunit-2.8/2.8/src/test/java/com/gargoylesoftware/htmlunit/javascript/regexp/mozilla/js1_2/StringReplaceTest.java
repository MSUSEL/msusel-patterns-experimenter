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
 * Tests originally in '/js/src/tests/js1_2/regexp/string_replace.js'.
 *
 * @version $Revision: 5902 $
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class StringReplaceTest extends WebDriverTestCase {

    /**
     * Tests 'adddb'.replace(/ddd/,'XX').
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("aXXb")
    public void test1() throws Exception {
        test("'adddb'.replace(/ddd/,'XX')");
    }

    /**
     * Tests 'adddb'.replace(/eee/,'XX').
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("adddb")
    public void test2() throws Exception {
        test("'adddb'.replace(/eee/,'XX')");
    }

    /**
     * Tests '34 56 78b 12'.replace(new RegExp('[0-9]+b'),'**').
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("34 56 ** 12")
    public void test3() throws Exception {
        test("'34 56 78b 12'.replace(new RegExp('[0-9]+b'),'**')");
    }

    /**
     * Tests '34 56 78b 12'.replace(new RegExp('[0-9]+c'),'XX').
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("34 56 78b 12")
    public void test4() throws Exception {
        test("'34 56 78b 12'.replace(new RegExp('[0-9]+c'),'XX')");
    }

    /**
     * Tests 'original'.replace(new RegExp(),'XX').
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("XXoriginal")
    public void test5() throws Exception {
        test("'original'.replace(new RegExp(),'XX')");
    }

    /**
     * Tests 'qwe ert x\t\n 345654AB'.replace(new RegExp('x\\s*\\d+(..)$'),'****').
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("qwe ert ****")
    public void test6() throws Exception {
        test("'qwe ert x\\t\\n 345654AB'.replace(new RegExp('x\\\\s*\\\\d+(..)$'),'****')");
    }

    private void test(final String script) throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  alert(" + script + ");\n"
            + "</script></head><body>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }
}
