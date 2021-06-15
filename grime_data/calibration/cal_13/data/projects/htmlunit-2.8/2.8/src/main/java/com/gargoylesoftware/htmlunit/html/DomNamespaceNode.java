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

import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.WebAssert;
import com.gargoylesoftware.htmlunit.html.xpath.XPathUtils;

/**
 * Intermediate base class for DOM Nodes that have namespaces. That includes HtmlElement and HtmlAttr.
 *
 * @version $Revision: 5301 $
 * @author David K. Taylor
 * @author Ahmed Ashour
 */
public abstract class DomNamespaceNode extends DomNode {

    private static final long serialVersionUID = 4121331154432606647L;
    private final String namespaceURI_;
    private String qualifiedName_;
    private final String localName_;
    private String prefix_;

    /**
     * Creates an instance of a DOM node that can have a namespace.
     *
     * @param namespaceURI the URI that identifies an XML namespace
     * @param qualifiedName the qualified name of the element type to instantiate
     * @param page the page that contains this element
     */
    protected DomNamespaceNode(final String namespaceURI, final String qualifiedName, final SgmlPage page) {
        super(page);
        WebAssert.notNull("qualifiedName", qualifiedName);
        qualifiedName_ = qualifiedName;

        if (qualifiedName.indexOf(':') != -1) {
            namespaceURI_ = namespaceURI;
            final int colonPosition = qualifiedName_.indexOf(':');
            localName_ = qualifiedName_.substring(colonPosition + 1);
            prefix_ = qualifiedName_.substring(0, colonPosition);
        }
        else {
            namespaceURI_ = namespaceURI;
            localName_ = qualifiedName_;
            prefix_ = null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getNamespaceURI() {
        return namespaceURI_;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getLocalName() {
        final boolean caseSensitive = getPage().hasCaseSensitiveTagNames();
        if (!caseSensitive && XPathUtils.isProcessingXPath()) { // and this method was called from Xalan
            return localName_.toLowerCase();
        }
        return localName_;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPrefix() {
        return prefix_;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPrefix(final String prefix) {
        prefix_ = prefix;
        if (prefix_ != null && localName_ != null) {
            qualifiedName_ = prefix_ + ":" + localName_;
        }
    }

    /**
     * Returns this node's qualified name.
     * @return this node's qualified name
     */
    public String getQualifiedName() {
        return qualifiedName_;
    }
}
