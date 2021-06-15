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
 * String is a native JavaScript object and therefore provided by Rhino but some tests are needed here.
 *
 * @version $Revision: 5698 $
 * @author Marc Guillemot
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class NativeStringTest extends WebDriverTestCase {

    /**
     * Test for bug <a href="http://sourceforge.net/support/tracker.php?aid=2783950">2783950</a>.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("key\\:b_M")
    public void replace() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "function doTest() {\n"
            + "    alert('key:b_M'.replace(':', '\\\\:'));\n"
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
    @Alerts({ "anchor: function", "big: function", "blink: function", "bold: function", "charAt: function",
        "charCodeAt: function", "concat: function", "constructor: function", "equals: undefined",
        "equalsIgnoreCase: undefined", "fixed: function", "fontcolor: function", "fontsize: function",
        "fromCharCode: undefined", "indexOf: function", "italics: function", "lastIndexOf: function",
        "link: function", "localeCompare: function", "match: function", "replace: function", "search: function",
        "slice: function", "small: function", "split: function", "strike: function", "sub: function",
        "substr: function", "substring: function", "sup: function", "toLocaleLowerCase: function",
        "toLocaleUpperCase: function", "toLowerCase: function", "toString: function", "toUpperCase: function",
        "trim: undefined", "valueOf: function" })
    public void methods_common() throws Exception {
        final String[] methods = {"anchor", "big", "blink", "bold", "charAt", "charCodeAt", "concat", "constructor",
            "equals", "equalsIgnoreCase", "fixed", "fontcolor", "fontsize", "fromCharCode", "indexOf", "italics",
            "lastIndexOf", "link", "localeCompare", "match", "replace", "search", "slice", "small", "split",
            "strike", "sub", "substr", "substring", "sup", "toLocaleLowerCase", "toLocaleUpperCase", "toLowerCase",
            "toString", "toUpperCase", "trim", "valueOf"};
        final String html = NativeDateTest.createHTMLTestMethods("'hello'", methods);
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
        final String html = NativeDateTest.createHTMLTestMethods("'hello'", methods);
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("")
    public void trim() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "function doTest() {\n"
            + "    var string = ' hi  ';\n"
            + "    if (''.trim) {\n"
            + "      alert(string.trim().length);\n"
            + "      alert(string.trimRight().length);\n"
            + "      alert(string.trimLeft().length);\n"
            + "    }\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "</body></html>";

        loadPageWithAlerts(html);
    }

}
