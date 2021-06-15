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
 * Tests for {@link DomComment}.
 *
 * @version $Revision: 5905 $
 * @author Karel Kolman
 * @author Ahmed Ashour
 * @author Philip Graf
 */
@RunWith(BrowserRunner.class)
public class DomCommentTest extends WebTestCase {

    /**
     * Test the comment not visible when viewed by user.
     * @throws Exception if the test fails
     */
    @Test
    public void asText() throws Exception {
        final String content = "<html><body><!-- a comment --></body></html>";
        final HtmlPage page = loadPage(content);
        assertEquals("", page.asText());
    }

     /**
     * Test the comment correctness.
     * @throws Exception if the test fails
     */
    @Test
    public void asXml() throws Exception {
        final String comment = "<!-- a comment -->";
        final String content = "<html><body><span id='foo'>" + comment + "</span></body></html>";
        final HtmlPage page = loadPage(content);
        final HtmlElement elt = page.getHtmlElementById("foo");
        final DomNode node = elt.getFirstChild();
        assertEquals(comment, node.asXml());
    }

     /**
     * Test comment and character data sibling correctness.
     * @throws Exception if the test fails
     */
    @Test
    public void textSibling() throws Exception {
        final String content = "<html><body id='body'><!-- c1 -->text<!-- c2 --></body></html>";
        final HtmlPage page = loadPage(content);
        final DomNode node = page.<HtmlElement>getHtmlElementById("body").getFirstChild();
        assertEquals(DomText.NODE_NAME, node.getNextSibling().getNodeName());
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    public void setTextContent() throws Exception {
        final String html = "<html><body><span id='s'><!--abc--></span></body></html>";
        final HtmlPage page = loadPage(html);
        final DomComment comment = (DomComment) page.getElementById("s").getFirstChild();
        assertEquals("abc", comment.getTextContent());
        comment.setTextContent("xyz");
        assertEquals("xyz", comment.getTextContent());
    }

    /**
     * Tests if {@code getCanonicalXPath()} returns the correct XPath for a
     * comment node.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void getCanonicalXPath_withoutCommentSiblings() throws Exception {
        final String html = "<html><body><span id='s'><!--abc--></span></body></html>";
        final HtmlPage page = loadPage(html);
        final DomComment comment = (DomComment) page.getElementById("s").getFirstChild();
        assertEquals("/html/body/span/comment()", comment.getCanonicalXPath());
        assertEquals(comment, page.getFirstByXPath(comment.getCanonicalXPath()));
    }

    /**
     * Tests if {@code getCanonicalXPath()} returns the correct XPath for a
     * comment node with other comment node siblings.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void getCanonicalXPath_withCommentSiblings() throws Exception {
        final String html = "<html><body><span id='s'><!--abc--><br/><!--def--></span></body></html>";
        final HtmlPage page = loadPage(html);

        final DomComment comment1 = (DomComment) page.getElementById("s").getFirstChild();
        assertEquals("abc", comment1.getData());
        assertEquals("/html/body/span/comment()[1]", comment1.getCanonicalXPath());
        assertEquals(comment1, page.getFirstByXPath(comment1.getCanonicalXPath()));

        final DomComment comment2 = (DomComment) page.getElementById("s").getChildNodes().get(2);
        assertEquals("def", comment2.getData());
        assertEquals("/html/body/span/comment()[2]", comment2.getCanonicalXPath());
        assertEquals(comment2, page.getFirstByXPath(comment2.getCanonicalXPath()));
    }

}
