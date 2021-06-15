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
package com.gargoylesoftware.htmlunit.html;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browser;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;

/**
 * Tests for {@link HtmlTable}.
 *
 * @version $Revision: 5301 $
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class HtmlTable2Test extends WebDriverTestCase {

    /**
     * Table can have multiple children of &lt;thead&gt;, &lt;tbody&gt; and &lt;tfoot&gt;.
     * Also, IE adds TR between THEAD and TD if missing.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = { "TBODY->TR->TD->Two", "THEAD->TR->TD->One", "THEAD->TR->TD->Three" },
            FF = { "TBODY->TR->TD->Two", "THEAD->TD->One", "THEAD->TR->TD->Three" })
    @NotYetImplemented(Browser.FF)
    public void two_theads() throws Exception {
        final String html = "<html><head><script>\n"
            + "  function test() {\n"
            + "    for (var child = myTable1.firstChild; child != null; child = child.nextSibling) {\n"
            + "      alert(debug(child));\n"
            + "    }\n"
            + "  }\n"
            + "  function debug(node) {\n"
            + "    return node.nodeValue != null ? node.nodeValue : (node.nodeName + '->' + debug(node.firstChild));\n"
            + "  }\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "<table id='myTable1'>"
            + "<td>Two</td>"
            + "<thead><td>One</td></thead>"
            + "<thead><tr><td>Three</td></tr></thead>"
            + "</table>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }
}
