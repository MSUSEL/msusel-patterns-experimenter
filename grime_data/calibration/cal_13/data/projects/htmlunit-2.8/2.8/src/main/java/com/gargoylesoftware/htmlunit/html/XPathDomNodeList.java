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

import java.io.Serializable;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.NOPTransformer;
import org.w3c.dom.Node;

import com.gargoylesoftware.htmlunit.html.xpath.XPathUtils;

/**
 * An XPath implementation of {@link org.w3c.dom.NodeList}.
 *
 * @version $Revision: 5301 $
 * @author Daniel Gredler
 * @author <a href="mailto:tom.anderson@univ.oxon.org">Tom Anderson</a>
 */
class XPathDomNodeList<E extends DomNode> extends AbstractList<E> implements DomNodeList<E>, Serializable {

    private static final long serialVersionUID = 1541399090797298342L;

    /** The XPath expression which dictates the contents of this node list. */
    private String xpath_;

    /** This node list's root node. */
    private DomNode node_;

    /** The transformer used to transform elements of this node list, if any. */
    private Transformer transformer_;

    /** Element cache, used to avoid XPath expression evaluation as much as possible. */
    private List<Object> cachedElements_;

    /**
     * Creates a new node list. The elements will be "calculated" using the specified XPath
     * expression applied on the specified node.
     * @param node the node to serve as root for the XPath expression
     * @param xpath the XPath expression which determines the elements of the node list
     */
    public XPathDomNodeList(final DomNode node, final String xpath) {
        this(node, xpath, NOPTransformer.INSTANCE);
    }

    /**
     * Creates a new node list. The elements will be "calculated" using the specified XPath
     * expression applied on the specified node, and using the specified transformer.
     * @param node the node to serve as root for the XPath expression
     * @param xpath the XPath expression which determines the elements of the node list
     * @param transformer the transformer used to transform elements of this node list
     */
    public XPathDomNodeList(final DomNode node, final String xpath, final Transformer transformer) {
        if (node != null) {
            node_ = node;
            xpath_ = xpath;
            transformer_ = transformer;
            final DomHtmlAttributeChangeListenerImpl listener = new DomHtmlAttributeChangeListenerImpl();
            node_.addDomChangeListener(listener);
            if (node_ instanceof HtmlElement) {
                ((HtmlElement) node_).addHtmlAttributeChangeListener(listener);
                cachedElements_ = null;
            }
        }
    }

    /**
     * Returns the nodes in this node list, caching as necessary.
     * @return the nodes in this node list
     */
    private List<Object> getNodes() {
        if (cachedElements_ == null) {
            if (node_ != null) {
                cachedElements_ = XPathUtils.getByXPath(node_, xpath_);
            }
            else {
                cachedElements_ = new ArrayList<Object>();
            }
        }
        return cachedElements_;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int size() {
        return getLength();
    }

    /**
     * {@inheritDoc}
     */
    public int getLength() {
        return getNodes().size();
    }

    /**
     * {@inheritDoc}
     */
    public Node item(final int index) {
        return (DomNode) transformer_.transform(getNodes().get(index));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public E get(final int index) {
        return (E) getNodes().get(index);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "XPathDomNodeList[" + xpath_ + "]";
    }

    /**
     * DOM change listener which clears the node cache when necessary.
     */
    private class DomHtmlAttributeChangeListenerImpl implements DomChangeListener, HtmlAttributeChangeListener {

        private static final long serialVersionUID = 3586393389945802774L;

        /**
         * {@inheritDoc}
         */
        public void nodeAdded(final DomChangeEvent event) {
            cachedElements_ = null;
        }

        /**
         * {@inheritDoc}
         */
        public void nodeDeleted(final DomChangeEvent event) {
            cachedElements_ = null;
        }

        /**
         * {@inheritDoc}
         */
        public void attributeAdded(final HtmlAttributeChangeEvent event) {
            cachedElements_ = null;
        }

        /**
         * {@inheritDoc}
         */
        public void attributeRemoved(final HtmlAttributeChangeEvent event) {
            cachedElements_ = null;
        }

        /**
         * {@inheritDoc}
         */
        public void attributeReplaced(final HtmlAttributeChangeEvent event) {
            cachedElements_ = null;
        }
    }

}
