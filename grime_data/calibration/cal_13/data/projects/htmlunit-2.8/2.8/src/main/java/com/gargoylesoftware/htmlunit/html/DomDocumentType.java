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

import org.w3c.dom.DocumentType;
import org.w3c.dom.NamedNodeMap;

import com.gargoylesoftware.htmlunit.SgmlPage;

/**
 * A DOM object for DocumentType.
 *
 * @version $Revision: 5301 $
 * @author Ahmed Ashour
 */
public class DomDocumentType extends DomNode implements DocumentType {

    private static final long serialVersionUID = -5089258626822609432L;

    private final String name_;
    private final String publicId_;
    private final String systemId_;

    /**
     * Creates a new instance.
     * @param page the page which contains this node
     * @param name the name
     * @param publicId the public ID
     * @param systemId the system ID
     */
    public DomDocumentType(final SgmlPage page, final String name, final String publicId, final String systemId) {
        super(page);
        name_ = name;
        publicId_ = publicId;
        systemId_ = systemId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getNodeName() {
        return name_;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public short getNodeType() {
        return DOCUMENT_TYPE_NODE;
    }

    /**
     * {@inheritDoc}
     */
    public NamedNodeMap getEntities() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public String getInternalSubset() {
        return "";
    }

    /**
     * {@inheritDoc}
     */
    public String getName() {
        return name_;
    }

    /**
     * {@inheritDoc}
     */
    public NamedNodeMap getNotations() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public String getPublicId() {
        return publicId_;
    }

    /**
     * {@inheritDoc}
     */
    public String getSystemId() {
        return systemId_;
    }
}
