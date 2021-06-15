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

import static org.junit.Assert.assertSame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.WebTestCase;

/**
 * Tests for {@link DomAttr}.
 *
 * @version $Revision: 5905 $
 * @author Denis N. Antonioli
 * @author Ahmed Ashour
 * @author David K. Taylor
 */
@RunWith(BrowserRunner.class)
public class HtmlAttrTest extends WebTestCase {

    /** Test object. */
    private final DomAttr htmlAttr_ = new DomAttr(null, null, ENTRY_KEY, ENTRY_VALUE, false);

    /** Single test key value. */
    private static final String ENTRY_KEY = "key";

    /** Single test attribute value. */
    private static final String ENTRY_VALUE = "value";

    /** A single dummy HtmlElement. Necessary, because HtmlAttr's constructor calls the method getPage(). */
    static final HtmlElement HTML_ELEMENT;

    static {
        final Map<String, DomAttr> emptyMap = Collections.emptyMap();
        HTML_ELEMENT = new HtmlElement(null, "dummy", null, emptyMap) {
            private static final long serialVersionUID = -3099722791571459332L;

            @Override
            public HtmlPage getPage() {
                return null;
            }
        };
    }

    /**
     * Constructor.
     */
    public HtmlAttrTest() {
        htmlAttr_.setParentNode(HTML_ELEMENT);
    }

    /**
     * Tests {@link DomAttr#getName()}.
     */
    @Test
    public void testGetName() {
        assertEquals(ENTRY_KEY, htmlAttr_.getName());
    }

    /**
     * Tests {@link DomAttr#getNodeName()}.
     */
    @Test
    public void testGetNodeName() {
        assertEquals(ENTRY_KEY, htmlAttr_.getNodeName());
    }

    /**
     * Tests {@link DomAttr#getNodeType()}.
     */
    @Test
    public void testGetNodeType() {
        assertEquals(org.w3c.dom.Node.ATTRIBUTE_NODE, htmlAttr_.getNodeType());
    }

    /**
     * Tests {@link DomAttr#getNodeValue()}.
     */
    @Test
    public void testGetNodeValue() {
        assertEquals(ENTRY_VALUE, htmlAttr_.getNodeValue());
    }

    /**
     * Tests {@link DomAttr#getValue()}.
     */
    @Test
    public void testGetValue() {
        assertEquals(ENTRY_VALUE, htmlAttr_.getValue());
    }

    /**
     * Tests {@link DomAttr#setValue(String)}.
     */
    @Test
    public void testSetValue() {
        htmlAttr_.setValue("foo");
        assertEquals("foo", htmlAttr_.getValue());
    }

    /**
     * Tests {@link DomAttr#getParentNode()}.
     */
    @Test
    public void testGetParent() {
        assertSame(HTML_ELEMENT, htmlAttr_.getParentNode());
    }

    /**
     * Test nodeType of {@link DomAttr}.
     *
     * @throws Exception if the test fails
     */
    @Test
    public void testNodeType() throws Exception {
        final String content = "<html><head><title>foo</title><script>\n"
            + "  function test() {\n"
            + "    var attr = document.createAttribute('myAttrib');\n"
            + "    alert(attr.nodeType);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";
        final String[] expectedAlerts = {"2"};
        final List<String> collectedAlerts = new ArrayList<String>();
        loadPage(content, collectedAlerts);
        assertEquals(expectedAlerts, collectedAlerts);
    }
}
