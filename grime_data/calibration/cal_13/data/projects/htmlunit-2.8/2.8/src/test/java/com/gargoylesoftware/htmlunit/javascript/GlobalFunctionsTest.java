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
package com.gargoylesoftware.htmlunit.javascript;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;

/**
 * Test for functions/properties of the global object.
 *
 * @version $Revision: 5319 $
 * @author Marc Guillemot
 */
@RunWith(BrowserRunner.class)
public class GlobalFunctionsTest extends WebDriverTestCase {

    /**
     * Test for bug <a href="http://sourceforge.net/support/tracker.php?aid=2815674">2815674</a>
     * due to Rhino bug <a href="https://bugzilla.mozilla.org/show_bug.cgi?id=501972">501972</a>
     * and for bug <a href="http://sourceforge.net/support/tracker.php?aid=2903514">2903514</a>
     * due to Rhino bug <a href="https://bugzilla.mozilla.org/show_bug.cgi?id=531436">531436</a>.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "7.89", "7.89" })
    public void parseFloat() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "function doTest() {\n"
            + "    alert(parseFloat('\\n 7.89 '));\n"
            + "    alert(parseFloat('7.89em'));\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "</body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * Test for the methods with the same expectations for all browsers.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "decodeURI: function", "decodeURIComponent: function", "encodeURI: function",
        "encodeURIComponent: function", "escape: function", "eval: function", "isFinite: function", "isNaN: function",
        "parseFloat: function", "parseInt: function", "unescape: function" })
    public void methods_common() throws Exception {
        final String[] methods = {"decodeURI", "decodeURIComponent", "encodeURI", "encodeURIComponent", "escape",
            "eval", "isFinite", "isNaN", "parseFloat", "parseInt", "unescape"};
        final String html = NativeDateTest.createHTMLTestMethods("this", methods);
        loadPageWithAlerts2(html);
    }

    /**
     * Test for the methods with the different expectations depending on the browsers.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = { "isXMLName: function", "uneval: function" },
            IE = { "isXMLName: undefined", "uneval: undefined" })
    public void methods_different() throws Exception {
        final String[] methods = {"isXMLName", "uneval"};
        final String html = NativeDateTest.createHTMLTestMethods("this", methods);
        loadPageWithAlerts2(html);
    }
}
