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
 * Tests originally in '/js/src/tests/js1_2/regexp/exec.js'.
 *
 * @version $Revision: 5766 $
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class ExecTest extends WebDriverTestCase {

    /**
     * Tests /[0-9]{3}/.exec('23 2 34 678 9 09').
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("678")
    public void test1() throws Exception {
        test("/[0-9]{3}/.exec('23 2 34 678 9 09')");
    }

    /**
     * Tests /3.{4}8/.exec('23 2 34 678 9 09').
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("34 678")
    public void test2() throws Exception {
        test("/3.{4}8/.exec('23 2 34 678 9 09')");
    }

    /**
     * Tests re.exec('23 2 34 678 9 09').
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("34 678")
    public void test3() throws Exception {
        final String initialScript = "var re = new RegExp('3.{4}8');";
        test(initialScript, "re.exec('23 2 34 678 9 09')");
    }

    /**
     * Tests (/3.{4}8/.exec('23 2 34 678 9 09')).length.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void test4() throws Exception {
        test("(/3.{4}8/.exec('23 2 34 678 9 09')).length");
    }

    /**
     * Tests (re.exec('23 2 34 678 9 09')).length.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("1")
    public void test5() throws Exception {
        final String initialScript = "re = new RegExp('3.{4}8');";
        test(initialScript, "(re.exec('23 2 34 678 9 09')).length");
    }

    private void test(final String script) throws Exception {
        test(null, script);
    }

    private void test(final String initialScript, final String script) throws Exception {
        String html = "<html><head><title>foo</title><script>\n";
        if (initialScript != null) {
            html += initialScript + ";\n";
        }
        html += "  alert(" + script + ");\n"
            + "</script></head><body>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }
}
