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
import org.w3c.dom.ProcessingInstruction;

import com.gargoylesoftware.htmlunit.SgmlPage;

/**
 * Wrapper for the DOM node ProcessingInstruction.
 *
 * @version $Revision: 5301 $
 * @author Ahmed Ashour
 */
public class DomProcessingInstruction extends DomNode implements ProcessingInstruction {

    private static final long serialVersionUID = -4400111815012912413L;

    private final String target_;
    private String data_;

    /**
     * Creates a new instance.
     *
     * @param page the Page that contains this element
     * @param target the target
     * @param data the data
     */
    public DomProcessingInstruction(final SgmlPage page, final String target, final String data) {
        super(page);
        target_ = target;
        setData(data);
    }

    /**
     * {@inheritDoc}
     * @return the node type constant, in this case {@link org.w3c.dom.Node#PROCESSING_INSTRUCTION_NODE}
     */
    @Override
    public short getNodeType() {
        return org.w3c.dom.Node.PROCESSING_INSTRUCTION_NODE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getNodeName() {
        return target_;
    }

    /**
     * {@inheritDoc}
     */
    public String getTarget() {
        return getNodeName();
    }

    /**
     * {@inheritDoc}
     */
    public String getData() {
        return getNodeValue();
    }

    /**
     * {@inheritDoc}
     */
    public void setData(final String data) throws DOMException {
        setNodeValue(data);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setNodeValue(final String value) {
        data_ = value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getNodeValue() {
        return data_;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setTextContent(final String textContent) {
        setNodeValue(textContent);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void printXml(final String indent, final PrintWriter printWriter) {
        printWriter.print("<?");
        printWriter.print(getTarget());
        printWriter.print(" ");
        printWriter.print(getData());
        printWriter.print("?>");
    }

}
