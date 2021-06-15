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
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.host.xml.XMLDocument;
import com.gargoylesoftware.htmlunit.xml.XmlPage;

/**
 * A JavaScript object for DOMImplementation.
 *
 * @version $Revision: 5864 $
 * @author Ahmed Ashour
 *
 * @see <a href="http://www.w3.org/TR/2000/WD-DOM-Level-1-20000929/level-one-core.html#ID-102161490">
 * W3C Dom Level 1</a>
 */
public class DOMImplementation extends SimpleScriptable {

    private static final long serialVersionUID = -6824157544527299635L;

    /**
     * Test if the DOM implementation implements a specific feature.
     * @param feature the name of the feature to test (case-insensitive)
     * @param version the version number of the feature to test
     * @return true if the feature is implemented in the specified version, false otherwise
     */
    public boolean jsxFunction_hasFeature(final String feature, final String version) {
        if (getBrowserVersion().hasFeature(BrowserVersionFeatures.GENERATED_35)) {
            if ("HTML".equals(feature) && "1.0".equals(version)) {
                return true;
            }
        }
        else {
            if ("HTML".equals(feature) && ("1.0".equals(version) || "2.0".equals(version))) {
                return true;
            }
            else if ("XML".equals(feature) && ("1.0".equals(version) || "2.0".equals(version))) {
                return true;
            }
            else if ("CSS2".equals(feature) && "2.0".equals(version)) {
                return true;
            }
            else if ("XPath".equals(feature) && "3.0".equals(version)) {
                return true;
            }
            //TODO: other features.
        }
        return false;
    }

    /**
     * Creates an {@link XMLDocument}.
     *
     * @param namespaceURI the URI that identifies an XML namespace
     * @param qualifiedName the qualified name of the document to instantiate
     * @param doctype the document types of the document
     * @return the newly created {@link XMLDocument}
     */
    //TODO: change doctype type to "DocType"
    public XMLDocument jsxFunction_createDocument(final String namespaceURI, final String qualifiedName,
            final Object doctype) {
        final XMLDocument document = new XMLDocument(getWindow().getWebWindow());
        document.setParentScope(getParentScope());
        document.setPrototype(getPrototype(document.getClass()));
        if (qualifiedName != null && qualifiedName.length() != 0) {
            final XmlPage page = document.getDomNodeOrDie();
            page.appendChild(page.createXmlElementNS(namespaceURI, qualifiedName));
        }
        return document;
    }
}
