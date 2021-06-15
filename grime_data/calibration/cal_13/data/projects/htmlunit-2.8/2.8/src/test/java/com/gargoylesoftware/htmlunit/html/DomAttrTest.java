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

/**
 * Tests for {@link DomAttr}.
 *
 * @version $Revision: 5905 $
 * @author Marc Guillemot
 */
@RunWith(BrowserRunner.class)
public class DomAttrTest extends WebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void getCanonicalXPath() throws Exception {
        final String html = "<html id='foo'><body></body></html>";
        final HtmlPage page = loadPage(html);
        final DomAttr attr = page.<HtmlElement>getHtmlElementById("foo").getAttributeNode("id");

        assertEquals("/html/@id", attr.getCanonicalXPath());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void textContent() throws Exception {
        final String html = "<html id='foo'><body></body></html>";
        final HtmlPage page = loadPage(html);
        final DomAttr attr = page.getDocumentElement().getAttributeNode("id");

        assertEquals("foo", attr.getTextContent());
        attr.setTextContent("hello");
        assertEquals("hello", attr.getTextContent());

        assertEquals(page.getDocumentElement(), page.getHtmlElementById("hello"));
    }
}
