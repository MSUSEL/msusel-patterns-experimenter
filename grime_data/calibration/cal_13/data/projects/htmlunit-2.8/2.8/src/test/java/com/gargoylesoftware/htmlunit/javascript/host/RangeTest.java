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
package com.gargoylesoftware.htmlunit.javascript.host;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browser;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browsers;

/**
 * Tests for {@link Range}.
 *
 * @version $Revision: 5301 $
 * @author Marc Guillemot
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class RangeTest extends WebTestCase {

    private static final String contentStart = "<html><head><title>Range Test</title>\n"
        + "<script>\n"
        + "function safeTagName(o) {\n"
        + "  return o ? (o.tagName ? o.tagName : o) : undefined;\n"
        + "}\n"
        + "function alertRange(r) {\n"
        + "  alert(r.collapsed);\n"
        + "  alert(safeTagName(r.commonAncestorContainer));\n"
        + "  alert(safeTagName(r.startContainer));\n"
        + "  alert(r.startOffset);\n"
        + "  alert(safeTagName(r.endContainer));\n"
        + "  alert(r.endOffset);\n"
        + "}\n"
        + "function test() {\n"
        + "var r = document.createRange();\n";

    private static final String contentEnd = "\n}\n</script></head>\n"
        + "<body onload='test()'>\n"
        + "<div id='theDiv'>Hello, <span id='theSpan'>this is a test for"
        + "<a  id='theA' href='http://htmlunit.sf.net'>HtmlUnit</a> support"
        +  "</div>\n"
        + "<p id='theP'>for Range</p>\n"
        + "</body></html>";

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(Browser.FF)
    @Alerts({ "true", "[object HTMLDocument]", "[object HTMLDocument]", "0", "[object HTMLDocument]", "0" })
    public void testEmptyRange() throws Exception {
        loadPageWithAlerts(contentStart + "alertRange(r);" + contentEnd);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(Browser.FF)
    @Alerts({ "false", "BODY", "BODY", "1", "BODY", "2" })
    public void testSelectNode() throws Exception {
        final String script = "r.selectNode(document.getElementById('theDiv'));"
            + "alertRange(r);";

        loadPageWithAlerts(contentStart + script + contentEnd);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(Browser.FF)
    @Alerts({ "false", "DIV", "DIV", "0", "DIV", "2" })
    public void testSelectNodeContents() throws Exception {
        final String script = "r.selectNodeContents(document.getElementById('theDiv'));"
            + "alertRange(r);";

        loadPageWithAlerts(contentStart + script + contentEnd);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(Browser.FF)
    @Alerts("<div id=\"myDiv2\"></div><div>harhar</div><div id=\"myDiv3\"></div>")
    public void testCreateContextualFragment() throws Exception {
        final String html = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var element = document.getElementById('myDiv2');\n"
            + "    var range = element.ownerDocument.createRange();\n"
            + "    range.setStartAfter(element);\n"
            + "    var fragment = range.createContextualFragment('<div>harhar</div>');\n"
            + "    element.parentNode.insertBefore(fragment, element.nextSibling);\n"
            + "    alert(element.parentNode.innerHTML);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "  <div id='myDiv'><div id='myDiv2'></div><div id='myDiv3'></div></div>\n"
            + "</body></html>";

        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(Browser.FF)
    @Alerts({ "qwerty", "tyxy", "[object DocumentFragment]", "[object HTMLSpanElement] [object Text]", "qwer",
        "[object HTMLSpanElement]" })
    public void testExtractContents() throws Exception {
        final String html =
              "<html><body><div id='d'>abc<span id='s'>qwerty</span>xyz</div><script>\n"
            + "var d = document.getElementById('d');\n"
            + "var s = document.getElementById('s');\n"
            + "var r = document.createRange();\n"
            + "r.setStart(s.firstChild, 4);\n"
            + "r.setEnd(d.childNodes[2], 2);\n"
            + "alert(s.innerHTML);\n"
            + "alert(r);\n"
            + "var fragment = r.extractContents();\n"
            + "alert(fragment);\n"
            + "alert(fragment.childNodes[0] + ' ' + fragment.childNodes[1]);\n"
            + "alert(s.innerHTML);\n"
            + "alert(document.getElementById('s'));\n"
            + "</script></body></html>";
        loadPageWithAlerts(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Browsers(Browser.FF)
    @Alerts({
        "1 <p><b id=\"b\">text1<span id=\"s\">inner</span>text2</b></p>",
        "2 text1",
        "3 [object DocumentFragment]",
        "4 1: [object HTMLParagraphElement]: <b id=\"b\">text1</b>",
        "5 <p><b id=\"b\"><span id=\"s\">inner</span>text2</b></p>",
        "6 1: [object HTMLParagraphElement]: <b id=\"b\"><span id=\"s\"></span>text2</b>",
        "7 <p><b id=\"b\"><span id=\"s\">inner</span></b></p>" })
    public void testExtractContents2() throws Exception {
        final String html =
              "<html><body><div id='d'><p><b id='b'>text1<span id='s'>inner</span>text2</b></p></div><script>\n"
            + "var d = document.getElementById('d');\n"
            + "var b = document.getElementById('b');\n"
            + "var s = document.getElementById('s');\n"
            + "var r = document.createRange();\n"
            + "r.setStart(d, 0);\n"
            + "r.setEnd(b, 1);\n"
            + "alert('1 ' + d.innerHTML);\n"
            + "alert('2 ' + r);\n"
            + "var f = r.extractContents();\n"
            + "alert('3 ' + f);\n"
            + "alert('4 ' + f.childNodes.length + ': ' + f.childNodes[0] + ': ' + f.childNodes[0].innerHTML);\n"
            + "alert('5 ' + d.innerHTML);\n"
            + "var r2 = document.createRange();\n"
            + "r2.setStart(s, 1);\n"
            + "r2.setEnd(d, 1);\n"
            + "var f2 = r2.extractContents();\n"
            + "alert('6 ' + f2.childNodes.length + ': ' + f2.childNodes[0] + ': ' + f2.childNodes[0].innerHTML);\n"
            + "alert('7 ' + d.innerHTML);\n"
            + "</script></body></html>";
        loadPageWithAlerts(html);
    }

}
