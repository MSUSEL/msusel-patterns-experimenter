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

/**
 * Tests for HtmlUnit's support of IE conditional comments.
 * @see <a href="http://msdn.microsoft.com/en-us/library/ms537512.asp">MSDN documentation</a>
 *
 * @version $Revision: 5301 $
 * @author Marc Guillemot
 */
@RunWith(BrowserRunner.class)
public class IEConditionalCommentsTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = { "hello", "IE" }, FF = "hello")
    public void ifIE() throws Exception {
        final String html = "<html><head>"
            + "<script>alert('hello')</script>\n"
            + "<!--[if IE]><script>alert('IE')</script><![endif]-->\n"
            + "</head><body></body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = "hello", IE6 = { "hello", "IE6" }, FF = "hello")
    public void if_lte_IE6() throws Exception {
        final String html = "<html><head>"
            + "<script>alert('hello')</script>\n"
            + "<!--[if lte IE 6]><script>alert('IE6')</script><![endif]-->\n"
            + "</head><body></body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = { "hello", "IE up to 7" }, IE8 = "hello", FF = "hello")
    public void if_lte_IE_7() throws Exception {
        final String html = "<html><head>"
            + "<script>alert('hello')</script>\n"
            + "<!--[if lte IE 7]><script>alert('IE up to 7')</script><![endif]-->\n"
            + "</head><body></body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = { "hello", "lt mso 9" }, FF = "hello")
    public void if_lte_mso_9() throws Exception {
        final String html = "<html><head>"
            + "<script>alert('hello')</script>\n"
            + "<!--[if gte mso 9]><script>alert('gte mso 9')</script><![endif]-->\n"
            + "<!--[if lt mso 9]><script>alert('lt mso 9')</script><![endif]-->\n"
            + "</head><body></body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = { "<!--[if gte IE]>hello<![endif]-->", "world" }, FF = { "undefined", "undefined" })
    public void incorrectExpression() throws Exception {
        final String html = "<html><head></head><body>"
            + "<div id='div1'><!--[if gte IE]>hello<![endif]--></div>\n"
            + "<div id='div2'><!--[if gte IE 5]>world<![endif]--></div>\n"
            + "<script>\n"
            + "alert(document.getElementById('div1').innerText);\n"
            + "alert(document.getElementById('div2').innerText);\n"
            + "</script>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }
}
