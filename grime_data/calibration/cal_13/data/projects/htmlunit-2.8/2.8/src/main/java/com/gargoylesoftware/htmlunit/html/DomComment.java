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

import org.w3c.dom.Comment;

import com.gargoylesoftware.htmlunit.SgmlPage;

/**
 * Wrapper for the DOM node Comment.
 *
 * @version $Revision: 5301 $
 * @author Karel Kolman
 * @author Ahmed Ashour
 */
public class DomComment extends DomCharacterData implements Comment {

    private static final long serialVersionUID = -7728247579175928593L;

    /** The symbolic node name. */
    public static final String NODE_NAME = "#comment";

    /**
     * Creates an instance of DomComment.
     *
     * @param page the Page that contains this element
     * @param data the string data held by this node
     */
    public DomComment(final SgmlPage page, final String data) {
        super(page, data);
    }

    /**
     * {@inheritDoc}
     * @return the node type constant, in this case {@link org.w3c.dom.Node#COMMENT_NODE}
     */
    @Override
    public short getNodeType() {
        return org.w3c.dom.Node.COMMENT_NODE;
    }

    /**
     * @return the node name, in this case {@link #NODE_NAME}
     */
    @Override
    public String getNodeName() {
        return NODE_NAME;
    }

    /**
     * Recursively write the XML data for the node tree starting at <code>node</code>.
     *
     * @param indent white space to indent child nodes
     * @param printWriter writer where child nodes are written
     */
    @Override
    protected void printXml(final String indent, final PrintWriter printWriter) {
        printWriter.print(indent);
        printWriter.print("<!--");
        printWriter.print(getData());
        printWriter.print("-->");
        printChildrenAsXml(indent, printWriter);
    }

    /**
     * Returns a simple string representation to facilitate debugging.
     * @return a simple string representation
     */
    @Override
    public String toString() {
        return asXml();
    }
}
