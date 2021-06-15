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
package com.gargoylesoftware.htmlunit.html.xpath;

import java.util.Map;

import org.apache.xml.utils.PrefixResolverDefault;
import org.w3c.dom.Node;

import com.gargoylesoftware.htmlunit.html.DomAttr;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.xml.XmlPage;

/**
 * Custom {@link PrefixResolverDefault} extension.
 *
 * @version $Revision: 5301 $
 * @author Ahmed Ashour
 */
final class HtmlUnitPrefixResolver extends PrefixResolverDefault {

    /**
     * Creates a new instance.
     * @param xpathExpressionContext the context from which XPath expression prefixes will be resolved
     */
    HtmlUnitPrefixResolver(final Node xpathExpressionContext) {
        super(xpathExpressionContext);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getNamespaceForPrefix(final String prefix, final Node namespaceContext) {
        String namespace = super.getNamespaceForPrefix(prefix, namespaceContext);
        if (namespace == null) {
            if (namespaceContext instanceof XmlPage) {
                final DomElement documentElement = ((XmlPage) namespaceContext).getDocumentElement();
                if (documentElement != null) {
                    namespace = getNamespace(documentElement, prefix);
                }
            }
            else if (namespaceContext instanceof DomElement) {
                namespace = getNamespace((DomElement) namespaceContext, prefix);
            }
        }
        return namespace;
    }

    private String getNamespace(final DomElement element, final String prefix) {
        final Map<String, DomAttr> attributes = element.getAttributesMap();
        for (final String name : attributes.keySet()) {
            if (name.startsWith("xmlns:")) {
                if (name.substring("xmlns:".length()).equals(prefix)) {
                    return attributes.get(name).getValue();
                }
            }
        }
        for (final DomNode child : element.getChildren()) {
            if (child instanceof DomElement) {
                final String namespace = getNamespace((DomElement) child, prefix);
                if (namespace != null) {
                    return namespace;
                }
            }
        }
        return null;
    }
}
