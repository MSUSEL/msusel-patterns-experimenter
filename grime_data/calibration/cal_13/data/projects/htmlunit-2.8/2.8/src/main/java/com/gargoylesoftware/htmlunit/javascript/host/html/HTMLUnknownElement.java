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
package com.gargoylesoftware.htmlunit.javascript.host.html;

import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.xml.XmlPage;

/**
 * The JavaScript object "HTMLUnknownElement".
 *
 * @version $Revision: 5864 $
 * @author Ahmed Ashour
 */
public class HTMLUnknownElement extends HTMLElement {

    private static final long serialVersionUID = 2898724046094626048L;

    /**
     * Creates an instance.
     */
    public HTMLUnknownElement() {
        // Empty.
    }

    /**
     * Gets the JavaScript property "nodeName" for the current node.
     * @return the node name
     */
    @Override
    public String jsxGet_nodeName() {
        final Page page = getDomNodeOrDie().getPage();
        if (page instanceof XmlPage || (getBrowserVersion().hasFeature(BrowserVersionFeatures.GENERATED_112)
            && ((HtmlPage) page).getNamespaces().containsKey(getDomNodeOrDie().getPrefix()))) {
            return getDomNodeOrDie().getLocalName();
        }
        return super.jsxGet_nodeName();
    }
}
