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
 * Tests originally in '/js/src/tests/js1_2/regexp/compile.js'.
 *
 * @version $Revision: 5766 $
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class CompileTest extends WebDriverTestCase {

    /**
     * Tests '234X456X7890'.match(regularExpression).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("456X7890")
    public void test1() throws Exception {
        final String initialScript = "var regularExpression = new RegExp();\n"
            + "regularExpression.compile('[0-9]{3}x[0-9]{4}', 'i');";
        test(initialScript, "'234X456X7890'.match(regularExpression)");
    }

    /**
     * Tests regularExpression.source.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("[0-9]{3}x[0-9]{4}")
    public void test2() throws Exception {
        final String initialScript = "var regularExpression = new RegExp();\n"
            + "regularExpression.compile('[0-9]{3}x[0-9]{4}', 'i');";
        test(initialScript, "regularExpression.source");
    }

    /**
     * Tests regularExpression.global.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void test3() throws Exception {
        final String initialScript = "var regularExpression = new RegExp();\n"
            + "regularExpression.compile('[0-9]{3}x[0-9]{4}', 'i');";
        test(initialScript, "regularExpression.global");
    }

    /**
     * Tests regularExpression.ignoreCase.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void test4() throws Exception {
        final String initialScript = "var regularExpression = new RegExp();\n"
            + "regularExpression.compile('[0-9]{3}x[0-9]{4}', 'i');";
        test(initialScript, "regularExpression.ignoreCase");
    }

    /**
     * Tests '234X456X7890'.match(regularExpression).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("234X456")
    public void test5() throws Exception {
        final String initialScript = "var regularExpression = new RegExp();\n"
            + "regularExpression.compile('[0-9]{3}X[0-9]{3}', 'g')";
        test(initialScript, "'234X456X7890'.match(regularExpression)");
    }

    /**
     * Tests regularExpression.source.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("[0-9]{3}X[0-9]{3}")
    public void test6() throws Exception {
        final String initialScript = "var regularExpression = new RegExp();\n"
            + "regularExpression.compile('[0-9]{3}X[0-9]{3}', 'g')";
        test(initialScript, "regularExpression.source");
    }

    /**
     * Tests regularExpression.global.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void test7() throws Exception {
        final String initialScript = "var regularExpression = new RegExp();\n"
            + "regularExpression.compile('[0-9]{3}X[0-9]{3}', 'g')";
        test(initialScript, "regularExpression.global");
    }

    /**
     * Tests regularExpression.ignoreCase.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("false")
    public void test8() throws Exception {
        final String initialScript = "var regularExpression = new RegExp();\n"
            + "regularExpression.compile('[0-9]{3}X[0-9]{3}', 'g')";
        test(initialScript, "regularExpression.ignoreCase");
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
