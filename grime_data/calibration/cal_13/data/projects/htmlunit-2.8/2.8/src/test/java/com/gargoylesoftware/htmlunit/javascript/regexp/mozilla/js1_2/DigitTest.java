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
 * Tests originally in '/js/src/tests/js1_2/regexp/digit.js'.
 *
 * @version $Revision: 5845 $
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class DigitTest extends WebDriverTestCase {

    private static final String non_digits = "_abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
        + "\\f\\n\\r\\t\\v~`!@#$%^&*()-+={[}]|\\\\:;\\'<,>./? \"";

    private static final String non_digits_expected = "_abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
        + "\f\n\r\t\u000B~`!@#$%^&*()-+={[}]|\\:;\'<,>./? \"";

    private static final String non_digits_expected_ie = "_abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
        + "\f\n\r\tv~`!@#$%^&*()-+={[}]|\\:;\'<,>./? \"";

    private static final String digits = "1234567890";

    /**
     * Tests digits.match(new RegExp('\\d+')).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(digits)
    public void test1() throws Exception {
        final String initialScript = "var digits = '" + digits + "'";
        test(initialScript, "digits.match(new RegExp('\\\\d+'))");
    }

    /**
     * Tests non_digits.match(new RegExp('\\D+')).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = non_digits_expected, IE = non_digits_expected_ie)
    @NotYetImplemented(Browser.IE)
    public void test2() throws Exception {
        final String initialScript = "var non_digits = '" + non_digits + "'";
        test(initialScript, "non_digits.match(new RegExp('\\\\D+'))");
    }

    /**
     * Tests non_digits.match(new RegExp('\\d')).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("null")
    public void test3() throws Exception {
        final String initialScript = "var non_digits = '" + non_digits + "'";
        test(initialScript, "non_digits.match(new RegExp('\\\\d'))");
    }

    /**
     * Tests digits.match(new RegExp('\\D')).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("null")
    public void test4() throws Exception {
        final String initialScript = "var digits = '" + digits + "'";
        test(initialScript, "digits.match(new RegExp('\\\\D'))");
    }

    /**
     * Tests s.match(new RegExp('\\d+')).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(digits)
    public void test5() throws Exception {
        final String initialScript = "var s = '" + non_digits + digits + "'";
        test(initialScript, "s.match(new RegExp('\\\\d+'))");
    }

    /**
     * Tests s.match(new RegExp('\\D+')).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = non_digits_expected, IE = non_digits_expected_ie)
    @NotYetImplemented(Browser.IE)
    public void test6() throws Exception {
        final String initialScript = "var s = '" + digits + non_digits + "'";
        test(initialScript, "s.match(new RegExp('\\\\D+'))");
    }

    /**
     * Tests s.match(new RegExp('\\d')).
     * @throws Exception if the test fails
     */
    @Test
    public void test7() throws Exception {
        for (int i = 0; i < digits.length(); i++) {
            final String initialScript = "var s = 'ab" + digits.charAt(i) + "cd'";
            setExpectedAlerts(String.valueOf(digits.charAt(i)));
            test(initialScript, "s.match(new RegExp('\\\\d'))");
            test(initialScript, "s.match(/\\d/)");
        }
    }

    /**
     * Tests s.match(new RegExp('\\D')).
     * @throws Exception if the test fails
     */
    @Test
    public void test8() throws Exception {
        for (int i = 0; i < non_digits.length() - 1; i++) {
            final char ch = non_digits.charAt(i);
            String expected = String.valueOf(ch);
            String input = expected;
            switch (ch) {
                case '\\':
                    input = "\\" + ch;
                    break;

                case '\'':
                    input = "\\" + ch;
                    break;

                case 'f':
                    expected = "\f";
                    input = "\\" + ch;
                    break;

                case 'n':
                    expected = "\n";
                    input = "\\" + ch;
                    break;

                case 'r':
                    expected = "\r";
                    input = "\\" + ch;
                    break;

                case 't':
                    expected = "\t";
                    input = "\\" + ch;
                    break;

                case 'v':
                    if (!getBrowserVersion().isIE()) {
                        expected = "\u000B";
                        input = "\\" + ch;
                    }
                    break;

                default:
            }

            setExpectedAlerts(expected);

            final String initialScript = "var s = '12" + input + "34'";
            test(initialScript, "s.match(new RegExp('\\\\D'))");
            test(initialScript, "s.match(/\\D/)");
        }
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
