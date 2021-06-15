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

import java.util.Map;

import org.xml.sax.Attributes;

import com.gargoylesoftware.htmlunit.SgmlPage;

/**
 * A factory for elements encountered in parsing the input which are not represented
 * by dedicated element classes.
 *
 * @version $Revision: 5301 $
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author Ahmed Ashour
 * @author David K. Taylor
 */
public final class UnknownElementFactory implements IElementFactory {

    /** The singleton instance. */
    public static final UnknownElementFactory instance = new UnknownElementFactory();

    /** Private singleton constructor. */
    private UnknownElementFactory() {
    }

    /**
     * {@inheritDoc}
     */
    public HtmlElement createElement(final SgmlPage page, final String tagName, final Attributes attributes) {
        String namespace = null;
        if (page instanceof HtmlPage && tagName.indexOf(':') != -1) {
            final HtmlPage htmlPage = (HtmlPage) page;
            final String prefix = tagName.substring(0, tagName.indexOf(':'));
            final Map<String, String> namespaces = htmlPage.getNamespaces();
            if (namespaces.containsKey(prefix)) {
                namespace = namespaces.get(prefix);
            }
        }
        return createElementNS(page, namespace, tagName, attributes);
    }

    /**
     * {@inheritDoc}
     */
    public HtmlElement createElementNS(final SgmlPage page, final String namespaceURI,
            final String qualifiedName, final Attributes attributes) {
        final Map<String, DomAttr> attributeMap = DefaultElementFactory.setAttributes(page, attributes);
        return new HtmlUnknownElement(page, namespaceURI, qualifiedName, attributeMap);
    }
}
