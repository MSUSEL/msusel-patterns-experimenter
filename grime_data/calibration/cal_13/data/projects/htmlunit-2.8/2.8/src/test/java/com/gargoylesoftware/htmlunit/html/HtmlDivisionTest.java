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
import com.gargoylesoftware.htmlunit.WebTestCase;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;

/**
 * Tests for {@link HtmlDivision}.
 *
 * @version $Revision: 5905 $
 * @author Ahmed Ashour
 * @author Marc Guillemot
 */
@RunWith(BrowserRunner.class)
public class HtmlDivisionTest extends WebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = "[object HTMLDivElement]", IE = "[object]")
    public void simpleScriptable() throws Exception {
        final String html = "<html><head>\n"
            + "<script>\n"
            + "  function test() {\n"
            + "    alert(document.getElementById('myId'));\n"
            + "  }\n"
            + "</script>\n"
            + "</head><body onload='test()'>\n"
            + "  <div id='myId'/>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(html);
        assertTrue(HtmlDivision.class.isInstance(page.getHtmlElementById("myId")));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void asText() throws Exception {
        String expected = "hello" + LINE_SEPARATOR + "world";
        testAsText(expected, "<div>hello</div>world");
        testAsText(expected, "<div>hello<br/></div>world");

        expected = "hello" + LINE_SEPARATOR + LINE_SEPARATOR + "world";
        testAsText(expected, "<div>hello<br/><br/></div>world");
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void asText_contiguousBlocks() throws Exception {
        final String expected = "hello" + LINE_SEPARATOR + "world";
        testAsText(expected, "<div><table><tr><td>hello</td></tr><tr><td>world<br/></td></tr></table></div>");
        testAsText(expected, "<div>hello</div><div>world</div>");
        testAsText(expected, "<div>hello</div><div><div>world</div></div>");
        testAsText(expected, "<div><table><tr><td>hello</td></tr><tr><td>world<br/></td></tr></table></div>");
    }

    private void testAsText(final String expected, final String htmlSnippet) throws Exception {
        final String html = "<html><head></head><body>\n"
            + htmlSnippet
            + "</body></html>";

        final HtmlPage page = loadPage(html);
        assertEquals(expected, page.asText());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void asTextDiv() throws Exception {
        final String html = "<html><head></head><body>\n"
            + "<div id='foo'>\n \n hello </div>"
            + "</body></html>";

        final HtmlPage page = loadPage(html);
        assertEquals("hello", page.asText());
        final HtmlDivision div = page.getHtmlElementById("foo");
        assertEquals("hello", div.asText());
    }
}
