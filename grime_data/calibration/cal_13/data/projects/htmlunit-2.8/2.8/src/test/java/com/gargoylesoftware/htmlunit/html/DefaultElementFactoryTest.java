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

import java.util.Iterator;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.xml.sax.helpers.AttributesImpl;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.WebTestCase;

/**
 * Tests for {@link DefaultElementFactory}.
 *
 * @version $Revision: 5905 $
 * @author <a href="mailto:marvin.java@gmail.com">Marcos Vinicius B. de Souza</a>
 * @author Marc Guillemot
 * @author Ahmed Ashour
 * @since 1.2
 */
@RunWith(BrowserRunner.class)
public class DefaultElementFactoryTest extends WebTestCase {
    /**
     * Test that the attribute order is the same as the provided one.
     * @throws Exception if the test fails
     */
    @Test
    public void attributeOrder() throws Exception {
        // Construct the test page.
        final String html = "<html><head><title>test page</title></head>\n"
                + "<body><div>test message</div></body></html>";

        // Load the test page.
        final HtmlPage htmlPage = loadPage(html);

        // Creates the attributes of the 'anchor'.
        final AttributesImpl atts = new AttributesImpl();
        atts.addAttribute(null, "href", "href", null, "http://www.google.com");
        atts.addAttribute(null, "tabindex", "tabindex", null, "2");
        atts.addAttribute(null, "accesskey", "accesskey", null, "F");

        // Access the factory.
        final DefaultElementFactory defaultElementFactory = new DefaultElementFactory();

        // Create a anchor element
        final HtmlAnchor anchor = (HtmlAnchor) defaultElementFactory.createElement(htmlPage, "a", atts);

        verifyAttributes(anchor);
    }

    /**
     * @param anchor the anchor which attributes should be checked
     */
    private void verifyAttributes(final HtmlAnchor anchor) {
        // Get the attributes iterator
        final Iterator<DomAttr> attributeEntriesIterator = anchor.getAttributesMap().values().iterator();

        // Verify if the attributes are in ascending order of name.
        DomAttr htmlAttr = attributeEntriesIterator.next();
        assertEquals("href", htmlAttr.getNodeName());
        assertEquals("http://www.google.com", htmlAttr.getValue());

        htmlAttr = attributeEntriesIterator.next();
        assertEquals("tabindex", htmlAttr.getNodeName());
        assertEquals("2", htmlAttr.getValue());

        htmlAttr = attributeEntriesIterator.next();
        assertEquals("accesskey", htmlAttr.getNodeName());
        assertEquals("F", htmlAttr.getValue());
    }

    /**
     * Test the order of attributes.
     * @throws Exception if the test fails
     */
    @Test
    public void attributeOrderLive() throws Exception {
        final String html = "<html><body>\n"
            + "<a href='http://www.google.com' tabindex='2' accesskey='F'>foo</a>\n"
            + "</body></html>";
        final HtmlPage page = loadPage(html);
        final HtmlAnchor anchor = page.getAnchorByText("foo");

        verifyAttributes(anchor);
    }
}
