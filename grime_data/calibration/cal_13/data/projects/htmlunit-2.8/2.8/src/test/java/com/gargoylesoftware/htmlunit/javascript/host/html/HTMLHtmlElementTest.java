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
package com.gargoylesoftware.htmlunit.javascript.host.html;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.html.HtmlHtml;

/**
 * Unit tests for {@link HTMLHtmlElement}.
 *
 * @version $Revision: 5904 $
 * @author Daniel Gredler
 * @author Marc Guillemot
 */
@RunWith(BrowserRunner.class)
public class HTMLHtmlElementTest extends WebDriverTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = "[object HTMLHtmlElement]", IE = "[object]")
    public void simpleScriptable() throws Exception {
        final String html = "<html id='myId'><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    alert(document.getElementById('myId'));\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        final WebDriver driver = loadPageWithAlerts2(html);
        if (driver instanceof HtmlUnitDriver) {
            final WebElement element = driver.findElement(By.id("myId"));
            assertTrue(toHtmlElement(element) instanceof HtmlHtml);
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(IE = { "[object]", "exception" },
            FF2 = { "[object HTMLHtmlElement]", "[HTMLHtmlElement]" },
            FF3 = { "[object HTMLHtmlElement]", "[object HTMLHtmlElement]" })
    public void HTMLHtmlElement_toString() throws Exception {
        final String html = "<html id='myId'><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    try {\n"
            + "      alert(document.getElementById('myId'));\n"
            + "      alert(HTMLHtmlElement);\n"
            + "    } catch (e) {\n"
            + "      alert('exception');\n"
            + "    }\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({ "2", "HEAD", "BODY", "null", "null" })
    public void childNodes_1() throws Exception {
        // The whitespace in this HTML is very important, because we're verifying
        // that it doesn't get included in the childNodes collection.
        final String html = "<html> \n <body> \n <script>\n"
            + "var nodes = document.documentElement.childNodes;\n"
            + "alert(nodes.length);\n"
            + "alert(nodes[0].nodeName);\n"
            + "alert(nodes[1].nodeName);\n"
            + "alert(nodes[0].previousSibling);\n"
            + "alert(nodes[1].nextSibling);\n"
            + "</script> \n </body> \n </html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts({ "1", "HEAD" })
    public void childNodes_2() throws Exception {
        // The whitespace in this HTML is very important, because we're verifying
        // that it doesn't get included in the childNodes collection.
        final String html = "<html> \n <head> \n <script>\n"
            + "var nodes = document.documentElement.childNodes;\n"
            + "alert(nodes.length);\n"
            + "alert(nodes[0].nodeName);\n"
            + "</script> \n </head> \n </html>";
        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = { "true", "true", "true", "true" },
            IE = { "true", "false", "true", "false" })
    public void clientWidth() throws Exception {
        final String html = "<!DOCTYPE HTML PUBLIC '-//W3C//DTD HTML 4.01 Transitional//EN'"
            + " 'http://www.w3.org/TR/html4/loose.dtd'>" // important for IE6!!!
            + "<html><head>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  var elt = document.body.parentNode;\n"
            + "  alert(elt.clientWidth > 0);\n"
            + "  alert(elt.clientWidth == window.innerWidth);\n"
            + "  alert(elt.clientHeight > 0);\n"
            + "  alert(elt.clientHeight == window.innerHeight);\n"
            + "}\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }
}
