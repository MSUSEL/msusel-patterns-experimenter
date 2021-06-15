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
import java.util.ListIterator;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.WebTestCase;

/**
 * Tests for {@link DomNodeList}.
 *
 * @version $Revision: 5905 $
 * @author <a href="mailto:tom.anderson@univ.oxon.org">Tom Anderson</a>
 */
@RunWith(BrowserRunner.class)
public class DomNodeListTest extends WebTestCase {

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void getElementsByTagName() throws Exception {
        final String firstContent
            = "<html><head><title>First</title></head>\n"
            + "<body>\n"
            + "<form><input type='button' name='button1' value='pushme'></form>\n"
            + "<div>a</div> <div>b</div> <div>c</div>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(firstContent);

        final DomNodeList<HtmlElement> divs = page.getElementsByTagName("div");

        assertEquals(3, divs.getLength());
        validateDomNodeList(divs);

        final HtmlDivision newDiv = new HtmlDivision(null, HtmlDivision.TAG_NAME, page, null);
        page.getBody().appendChild(newDiv);
        assertEquals(4, divs.getLength());
        validateDomNodeList(divs);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void getChildNodes() throws Exception {
        final String firstContent
            = "<html><head><title>First</title></head>\n"
            + "<body>\n"
            + "<form><input type='button' name='button1' value='pushme'></form>\n"
            + "<div>a</div> <div>b</div> <div>c</div>\n"
            + "</body></html>";

        final HtmlPage page = loadPage(firstContent);
        final DomNodeList<DomNode> bodyChildren = page.getBody().getChildNodes();
        validateDomNodeList(bodyChildren);
    }

    private <E extends DomNode> void validateDomNodeList(final DomNodeList<E> nodes) {
        assertEquals(nodes.getLength(), nodes.size());
        final Iterator<E> nodesIterator = nodes.iterator();
        for (int i = 0; i < nodes.getLength(); i++) {
            assertEquals(nodes.item(i), nodes.get(i));
            assertEquals(nodes.item(i), nodesIterator.next());
            assertEquals(i, nodes.indexOf(nodes.item(i)));
        }
        assertEquals(false, nodesIterator.hasNext());
        final ListIterator<E> nodesListIterator = nodes.listIterator();
        assertEquals(nodes.item(0), nodesListIterator.next());
        assertEquals(nodes.item(1), nodesListIterator.next());
        assertEquals(nodes.item(1), nodesListIterator.previous());
    }
}
