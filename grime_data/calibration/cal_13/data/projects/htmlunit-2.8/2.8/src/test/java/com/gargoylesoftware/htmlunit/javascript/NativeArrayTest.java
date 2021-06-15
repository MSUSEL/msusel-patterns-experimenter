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
import com.gargoylesoftware.htmlunit.BrowserRunner.Browser;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;

/**
 * Array is a native JavaScript object and therefore provided by Rhino but behavior should be
 * different depending on the simulated browser.
 *
 * @version $Revision: 5301 $
 * @author Marc Guillemot
 */
@RunWith(BrowserRunner.class)
public class NativeArrayTest extends WebDriverTestCase {

    /**
     * Test for sort algorithm used (when sort is called with callback).
     * @throws Exception if the test fails
     */
    @Test
    @NotYetImplemented({ Browser.IE, Browser.FF2 })
    @Alerts(FF2 = { "2<>1", "1<>2", "2<>5", "9<>5", "2<>5", "2<>1", "1<>1" },
            FF3 = { "1<>5", "5<>2", "1<>2", "5<>1", "2<>1", "1<>1", "5<>9" },
            IE = { "1<>9", "9<>5", "9<>2", "9<>1", "1<>5", "5<>1", "5<>2", "5<>1", "1<>1", "1<>2", "2<>1", "1<>1" })
    public void sort() throws Exception {
        final String html
            = "<html><head><title>foo</title><script>\n"
            + "function compare(x, y) {\n"
            + "  alert('' + x + '<>' + y);\n"
            + "  return x - y;\n"
            + "}\n"
            + "function doTest() {\n"
            + "    var t = [1, 5, 2, 1, 9];\n"
            + "    t.sort(compare);\n"
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
    @Alerts({ "concat: function", "constructor: function", "isArray: undefined", "join: function", "pop: function",
        "push: function", "reverse: function", "shift: function", "slice: function", "sort: function",
        "splice: function", "toLocaleString: function", "toString: function", "unshift: function" })
    public void methods_common() throws Exception {
        final String[] methods = {"concat", "constructor", "isArray", "join", "pop", "push", "reverse", "shift",
            "slice", "sort", "splice", "toLocaleString", "toString", "unshift"};
        final String html = NativeDateTest.createHTMLTestMethods("[]", methods);
        loadPageWithAlerts2(html);
    }

    /**
     * Test for the methods with the different expectations depending on the browsers.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF3 = { "every: function", "filter: function", "forEach: function", "indexOf: function",
            "lastIndexOf: function", "map: function", "reduce: function", "reduceRight: function", "some: function",
            "toSource: function" },
            FF2 = { "every: function", "filter: function", "forEach: function", "indexOf: function",
            "lastIndexOf: function", "map: function", "reduce: undefined", "reduceRight: undefined", "some: function",
            "toSource: function" },
            IE = { "every: undefined", "filter: undefined", "forEach: undefined", "indexOf: undefined",
            "lastIndexOf: undefined", "map: undefined", "reduce: undefined", "reduceRight: undefined",
            "some: undefined", "toSource: undefined" })
    public void methods_different() throws Exception {
        final String[] methods = {"every", "filter", "forEach", "indexOf", "lastIndexOf", "map", "reduce",
            "reduceRight", "some", "toSource"};
        final String html = NativeDateTest.createHTMLTestMethods("[]", methods);
        loadPageWithAlerts2(html);
    }

    /**
     * Rhino version used in HtmlUnit incorrectly walked the prototype chain while deleting property.
     * @see <a href="http://sf.net/support/tracker.php?aid=2834335">Bug 2834335</a>
     * @see <a href="https://bugzilla.mozilla.org/show_bug.cgi?id=510504">corresponding Rhino bug</a>
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "hello", "foo", "hello" })
    public void deleteShouldNotWalkPrototypeChain() throws Exception {
        final String html = "<html><body><script>\n"
            + "Array.prototype.foo = function() { alert('hello')};\n"
            + "[].foo();\n"
            + "var x = [];\n"
            + "for (var i in x) {\n"
            + "  alert(i);\n"
            + "  delete x[i];\n"
            + "}\n"
            + "[].foo();\n"
            + "</script></body>";

        loadPageWithAlerts2(html);
    }
}
