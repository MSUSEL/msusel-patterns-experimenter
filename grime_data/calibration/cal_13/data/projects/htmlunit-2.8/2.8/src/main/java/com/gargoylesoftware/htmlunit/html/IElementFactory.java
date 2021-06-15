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

import org.xml.sax.Attributes;

import com.gargoylesoftware.htmlunit.SgmlPage;

/**
 * Specification of a factory capable of creating {@link HtmlElement} objects.
 *
 * @version $Revision: 5301 $
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author Ahmed Ashour
 */
public interface IElementFactory {
    /**
     * Creates an element according to this factory's specification. Note that even though this method
     * takes a page parameter, the element is <em>not</em> automatically added to the page's DOM tree.
     *
     * @param page the enclosing page for the new element
     * @param tagName the tag name (most factories will be responsible for a specific tag, but this
     *        parameter is passed in for factories that don't follow this rule)
     * @param attributes the attributes encountered during XML/HTML parsing (possibly <tt>null</tt>
     *        if no attributes specified
     * @return the newly created and initialized element
     */
    HtmlElement createElement(final SgmlPage page, final String tagName, final Attributes attributes);

    /**
     * Creates an element according to this factory's specification. Note that even though this method
     * takes a page parameter, the element is <em>not</em> automatically added to the page's DOM tree.
     *
     * @param page the enclosing page for the new element
     * @param namespaceURI the URI that identifies an XML namespace
     * @param qualifiedName the qualified name of the element type to instantiate
     * @param attributes the attributes encountered during XML/HTML parsing (possibly <tt>null</tt>
     *        if no attributes specified
     * @return the newly created and initialized element
     */
    HtmlElement createElementNS(final SgmlPage page, final String namespaceURI, final String qualifiedName,
        final Attributes attributes);
}
