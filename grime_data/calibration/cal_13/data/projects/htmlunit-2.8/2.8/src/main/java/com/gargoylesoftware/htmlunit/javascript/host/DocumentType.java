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
package com.gargoylesoftware.htmlunit.javascript.host;

import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.html.DomDocumentType;

/**
 * A JavaScript object for a DocumentType.
 *
 * @version $Revision: 5864 $
 * @author Ahmed Ashour
 * @see <a href="http://msdn.microsoft.com/en-us/library/ms762752.aspx">MSDN documentation</a>
 * @see <a href="http://www.xulplanet.com/references/objref/DocumentType.html">XUL Planet</a>
 */
public class DocumentType extends Node {

    private static final long serialVersionUID = -927596204137079990L;

    /**
     * Returns the name.
     * @return the name
     */
    public String jsxGet_name() {
        final String name = ((DomDocumentType) getDomNodeOrDie()).getName();
        if ("html".equals(name) && "FF3".equals(getBrowserVersion().getNickname())) {
            return "HTML";
        }
        return name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_nodeName() {
        return jsxGet_name();
    }

    /**
     * Returns the publicId.
     * @return the publicId
     */
    public String jsxGet_publicId() {
        return ((DomDocumentType) getDomNodeOrDie()).getPublicId();
    }

    /**
     * Returns the systemId.
     * @return the systemId
     */
    public String jsxGet_systemId() {
        return ((DomDocumentType) getDomNodeOrDie()).getSystemId();
    }

    /**
     * Returns the internal subset.
     * @return the internal subset
     */
    public String jsxGet_internalSubset() {
        return ((DomDocumentType) getDomNodeOrDie()).getInternalSubset();
    }

    /**
     * Returns entities.
     * @return entities
     */
    public String jsxGet_entities() {
        if (getBrowserVersion().hasFeature(BrowserVersionFeatures.GENERATED_33)) {
            return "";
        }
        return null;
    }

    /**
     * Returns notations.
     * @return notations
     */
    public String jsxGet_notations() {
        if (getBrowserVersion().hasFeature(BrowserVersionFeatures.GENERATED_34)) {
            return "";
        }
        return null;
    }
}
