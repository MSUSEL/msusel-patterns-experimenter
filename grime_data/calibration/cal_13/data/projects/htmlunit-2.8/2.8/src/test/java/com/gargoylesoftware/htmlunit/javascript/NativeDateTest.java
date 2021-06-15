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
 * Date is a native JavaScript object and therefore provided by Rhino but behavior should be
 * different depending on the simulated browser.
 *
 * @version $Revision: 5301 $
 * @author Marc Guillemot
 */
@RunWith(BrowserRunner.class)
public class NativeDateTest extends WebDriverTestCase {

    /**
     * Test for bug <a href="http://sourceforge.net/support/tracker.php?aid=2615817">2615817</a>.
     * @see <a href="http://www.w3schools.com/jsref/jsref_getYear.asp">this doc</a>
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = { "-13", "84", "109" }, IE = { "1887", "84", "2009" })
    public void getYear() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "function doTest() {\n"
            + "    alert(new Date(1887, 2, 1).getYear());\n"
            + "    alert(new Date(1984, 2, 1).getYear());\n"
            + "    alert(new Date(2009, 2, 1).getYear());\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * Test for the methods with the same expectations for all browsers.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "constructor: function", "getDate: function", "getDay: function", "getFullYear: function",
            "getHours: function", "getMilliseconds: function", "getMinutes: function", "getMonth: function",
            "getSeconds: function", "getTime: function", "getTimezoneOffset: function", "getUTCDate: function",
            "getUTCDay: function", "getUTCFullYear: function", "getUTCHours: function", "getUTCMilliseconds: function",
            "getUTCMinutes: function", "getUTCMonth: function", "getUTCSeconds: function", "getYear: function",
            "now: undefined", "parse: undefined", "setDate: function", "setFullYear: function", "setHours: function",
            "setMilliseconds: function", "setMinutes: function", "setMonth: function", "setSeconds: function",
            "setTime: function", "setUTCDate: function", "setUTCFullYear: function", "setUTCHours: function",
            "setUTCMilliseconds: function", "setUTCMinutes: function", "setUTCMonth: function",
            "setUTCSeconds: function", "setYear: function", "toDateString: function", "toISOString: undefined",
            "toJSON: undefined", "toLocaleDateString: function", "toLocaleString: function",
            "toLocaleTimeString: function", "toString: function", "toTimeString: function",
            "toUTCString: function", "valueOf: function", "UTC: undefined" })
    public void methods_common() throws Exception {
        final String[] methods = {"constructor", "getDate", "getDay", "getFullYear", "getHours", "getMilliseconds",
            "getMinutes", "getMonth", "getSeconds", "getTime", "getTimezoneOffset", "getUTCDate", "getUTCDay",
            "getUTCFullYear", "getUTCHours", "getUTCMilliseconds", "getUTCMinutes", "getUTCMonth", "getUTCSeconds",
            "getYear", "now", "parse", "setDate", "setFullYear", "setHours", "setMilliseconds", "setMinutes",
            "setMonth", "setSeconds", "setTime", "setUTCDate", "setUTCFullYear", "setUTCHours",
            "setUTCMilliseconds", "setUTCMinutes", "setUTCMonth", "setUTCSeconds", "setYear", "toDateString",
            "toISOString", "toJSON", "toLocaleDateString", "toLocaleString", "toLocaleTimeString", "toString",
            "toTimeString", "toUTCString", "valueOf", "UTC"};
        final String html = createHTMLTestMethods("new Date()", methods);
        loadPageWithAlerts2(html);
    }

    /**
     * Test for the methods with different expectations depending on the browsers.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = "toSource: function", IE = "toSource: undefined")
    public void methods_differences() throws Exception {
        final String[] methods = {"toSource"};
        final String html = createHTMLTestMethods("new Date()", methods);
        loadPageWithAlerts2(html);
    }

    static String createHTMLTestMethods(final String object, final String... methodNames) throws Exception {
        final StringBuilder methodList = new StringBuilder();
        for (final String methodName : methodNames) {
            methodList.append(", '").append(methodName).append("'");
        }
        final String html = "<html><head><title>foo</title><script>\n"
            + "function doTest() {\n"
            + "  var o = " + object + ";\n"
            + "  var props = [" + methodList.substring(2) + "];\n"
            + "  for (var i=0; i<props.length; ++i) {\n"
            + "    var p = props[i];\n"
            + "    alert(p + ': ' + typeof(o[p]));\n"
            + "  }\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "</body></html>";

        return html;
    }
}
