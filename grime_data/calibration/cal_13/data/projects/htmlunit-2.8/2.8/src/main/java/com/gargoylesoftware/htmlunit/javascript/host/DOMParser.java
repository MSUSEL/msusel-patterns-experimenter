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

import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.host.xml.XMLDocument;

/**
 * A JavaScript object for DOMParser.
 *
 * @version $Revision: 5301 $
 * @author Ahmed Ashour
 *
 * @see <a href="http://www.xulplanet.com/references/objref/DOMParser.html">XUL Planet</a>
 */
public class DOMParser extends SimpleScriptable {

    private static final long serialVersionUID = 3143102490697686971L;

    /**
     * JavaScript constructor.
     */
    public void jsConstructor() {
        // Empty.
    }

    /**
     * The string passed in is parsed into a DOM document.
     * @param str the UTF16 string to be parsed
     * @param contentType the content type of the string -
     *        either <tt>text/xml</tt>, <tt>application/xml</tt>, or <tt>application/xhtml+xml</tt>. Must not be NULL.
     * @return the generated document
     */
    public XMLDocument jsxFunction_parseFromString(final String str, final String contentType) {
        final XMLDocument document = new XMLDocument();
        document.setParentScope(getParentScope());
        document.setPrototype(getPrototype(XMLDocument.class));
        document.jsxFunction_loadXML(str);
        return document;
    }
}
