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
package com.gargoylesoftware.htmlunit.javascript.host.xml;

import org.w3c.dom.NamedNodeMap;

import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.html.DomAttr;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.host.Document;
import com.gargoylesoftware.htmlunit.javascript.host.DocumentFragment;
import com.gargoylesoftware.htmlunit.javascript.host.Element;
import com.gargoylesoftware.htmlunit.javascript.host.Node;
import com.gargoylesoftware.htmlunit.util.StringUtils;

/**
 * A JavaScript object for XMLSerializer.
 *
 * @version $Revision: 5864 $
 * @author Ahmed Ashour
 * @author Darrell DeBoer
 */
public class XMLSerializer extends SimpleScriptable {

    private static final long serialVersionUID = -934136191731299896L;

    /**
     * JavaScript constructor.
     */
    public void jsConstructor() {
        // Empty.
    }

    /**
     * The subtree rooted by the specified element is serialized to a string.
     * @param root the root of the subtree to be serialized (this may be any node, even a document)
     * @return the serialized string
     */
    public String jsxFunction_serializeToString(Node root) {
        if (root instanceof Document) {
            root = ((Document) root).jsxGet_documentElement();
        }
        else if (root instanceof DocumentFragment) {
            root = root.jsxGet_firstChild();
        }
        if (root instanceof Element) {
            final StringBuilder buffer = new StringBuilder();
            final boolean isIE = getBrowserVersion().hasFeature(BrowserVersionFeatures.GENERATED_139);
            toXml(1, root.getDomNodeOrDie(), buffer, isIE);
            if (isIE) {
                buffer.append('\r').append('\n');
            }
            return buffer.toString();
        }
        if (root == null) {
            return "";
        }
        return root.<DomNode>getDomNodeOrDie().asXml();
    }

    private void toXml(final int indent, final DomNode node, final StringBuilder buffer, final boolean isIE) {
        String nodeName = node.getNodeName();
        if (!isIE && (node.getPage() instanceof HtmlPage)) {
            nodeName = nodeName.toUpperCase();
        }
        buffer.append('<').append(nodeName);
        if (node.getNamespaceURI() != null && node.getPrefix() != null) {
            boolean sameNamespace = false;
            for (DomNode parentNode = node.getParentNode(); parentNode instanceof DomElement;
                    parentNode = parentNode.getParentNode()) {
                if (node.getNamespaceURI().equals(parentNode.getNamespaceURI())) {
                    sameNamespace = true;
                }
            }
            if (node.getParentNode() == null || !sameNamespace) {
                ((DomElement) node).setAttribute("xmlns:" + node.getPrefix(), node.getNamespaceURI());
            }
        }
        final NamedNodeMap attributesMap = node.getAttributes();
        for (int i = 0; i < attributesMap.getLength(); i++) {
            final DomAttr attrib = (DomAttr) attributesMap.item(i);
            buffer.append(' ').append(attrib.getQualifiedName()).append('=')
                .append('"').append(attrib.getValue()).append('"');
        }
        boolean startTagClosed = false;
        for (final DomNode child : node.getChildren()) {
            if (!startTagClosed) {
                buffer.append('>');
                startTagClosed = true;
            }
            switch (child.getNodeType()) {
                case Node.ELEMENT_NODE:
                    toXml(indent + 1, child, buffer, isIE);
                    break;

                case Node.TEXT_NODE:
                    String value = child.getNodeValue();
                    value = StringUtils.escapeXmlChars(value);
                    if (isIE && value.trim().length() == 0) {
                        buffer.append('\r').append('\n');
                        final DomNode sibling = child.getNextSibling();
                        if (sibling != null && sibling.getNodeType() == Node.ELEMENT_NODE) {
                            for (int i = 0; i < indent; i++) {
                                buffer.append('\t');
                            }
                        }
                    }
                    else {
                        buffer.append(value);
                    }
                    break;

                case Node.CDATA_SECTION_NODE:
                case Node.COMMENT_NODE:
                    buffer.append(child.asXml());
                    break;

                default:

            }
        }
        if (!startTagClosed) {
            buffer.append('/').append('>');
        }
        else {
            buffer.append('<').append('/').append(node.getNodeName()).append('>');
        }
    }

}
