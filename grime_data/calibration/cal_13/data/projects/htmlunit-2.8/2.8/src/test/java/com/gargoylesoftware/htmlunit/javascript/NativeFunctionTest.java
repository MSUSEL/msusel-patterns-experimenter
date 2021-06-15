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
 * Function is a native JavaScript object and therefore provided by Rhino but some tests are needed here
 * to be sure that we have the expected results (for instance "bind" is an EcmaScript 5 method that is not
 * available in FF2 or FF3).
 *
 * @version $Revision: 5316 $
 * @author Marc Guillemot
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class NativeFunctionTest extends WebDriverTestCase {

    /**
     * Test for the methods with the same expectations for all browsers.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "apply: function", "arguments: object", "bind: undefined", "call: function", "constructor: function",
            "toString: function" })
    public void methods_common() throws Exception {
        final String[] methods = {"apply", "arguments", "bind", "call", "constructor", "toString"};
        final String html = NativeDateTest.createHTMLTestMethods("function() {}", methods);
        loadPageWithAlerts2(html);
    }

    /**
     * Test for the methods with the different expectations depending on the browsers.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = "toSource: function", IE = "toSource: undefined")
    public void methods_different() throws Exception {
        final String html = NativeDateTest.createHTMLTestMethods("function() {}", "toSource");
        loadPageWithAlerts2(html);
    }

    /**
     * Ensure that "arguments" object doesn't see anything from Array's prototype.
     * This was a bug in Rhino from Head as of 06.01.2010 due to adaptation to ES5 (or to some early state
     * of the draft).
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("true")
    public void arguments_prototype() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "var f1 = function(){};\n"
            + "var f2 = function(){};\n"
            + "Object.prototype.myFunction = f1;\n"
            + "Array.prototype.myFunction = f2;\n"
            + "var a = (function() { return arguments;})();\n"
            + "alert(a.myFunction == f1);\n"
            + "</script></head><body>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }
}
