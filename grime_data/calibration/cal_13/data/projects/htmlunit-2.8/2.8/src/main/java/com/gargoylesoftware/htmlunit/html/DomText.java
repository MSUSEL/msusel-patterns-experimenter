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

import java.io.PrintWriter;

import org.w3c.dom.DOMException;
import org.w3c.dom.Text;

import com.gargoylesoftware.htmlunit.SgmlPage;
import com.gargoylesoftware.htmlunit.util.StringUtils;

/**
 * Representation of a text node in the HTML DOM.
 *
 * @version $Revision: 5689 $
 * @author David K. Taylor
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author Rodney Gitzel
 * @author Ahmed Ashour
 * @author Sudhan Moghe
 * @author Philip Graf
 */
public class DomText extends DomCharacterData implements Text {

    private static final long serialVersionUID = 6589779086230288951L;

    /** The symbolic node name. */
    public static final String NODE_NAME = "#text";

    /**
     * Creates an instance of DomText.
     *
     * @param page the Page that contains this element
     * @param data the string data held by this node
     */
    public DomText(final SgmlPage page, final String data) {
        super(page, data);
    }

    /**
     * {@inheritDoc}
     */
    public DomText splitText(final int offset) {
        if (offset < 0 || offset > getLength()) {
            throw new IllegalArgumentException("offset: " + offset + " data.length: " + getLength());
        }

        // split text into two separate nodes
        final DomText newText = createSplitTextNode(offset);
        setData(getData().substring(0, offset));

        // insert new text node
        if (getParentNode() != null) {
            getParentNode().insertBefore(newText, getNextSibling());
        }
        return newText;
    }

    /**
     * Creates a new text node split from another text node. This method allows
     * the derived type of the new text node to match the original node type.
     *
     * @param offset the character position at which to split the DomText node
     * @return the newly created Text node
     */
    protected DomText createSplitTextNode(final int offset) {
        return new DomText(getPage(), getData().substring(offset));
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    public boolean isElementContentWhitespace() {
        throw new UnsupportedOperationException("DomText.isElementContentWhitespace is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    public String getWholeText() {
        throw new UnsupportedOperationException("DomText.getWholeText is not yet implemented.");
    }

    /**
     * {@inheritDoc}
     * Not yet implemented.
     */
    public Text replaceWholeText(final String content) throws DOMException {
        throw new UnsupportedOperationException("DomText.replaceWholeText is not yet implemented.");
    }

    /**
     * @return the node type constant, in this case {@link org.w3c.dom.Node#TEXT_NODE}
     */
    @Override
    public short getNodeType() {
        return org.w3c.dom.Node.TEXT_NODE;
    }

    /**
     * @return the node name, in this case {@link #NODE_NAME}
     */
    @Override
    public String getNodeName() {
        return NODE_NAME;
    }

    /**
     * Recursively writes the XML data for the node tree starting at <code>node</code>.
     *
     * @param indent white space to indent child nodes
     * @param printWriter writer where child nodes are written
     */
    @Override
    protected void printXml(final String indent, final PrintWriter printWriter) {
        if (getData().trim().length() != 0) {
            printWriter.print(indent);
            String data = getData();
            if (!(getParentNode() instanceof HtmlStyle) || !data.startsWith("<!--") || !data.endsWith("-->")) {
                data = StringUtils.escapeXmlChars(data);
            }
            printWriter.println(data);
        }
        printChildrenAsXml(indent, printWriter);
    }

    /**
     * Gives a simple representation to facilitate debugging.
     * @return a simple representation
     */
    @Override
    public String toString() {
        return asText();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isTrimmedText() {
        return false;
    }

}
