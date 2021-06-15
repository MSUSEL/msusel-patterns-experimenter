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

/**
 * The JavaScript object "HTMLBaseElement".
 *
 * @version $Revision: 5301 $
 * @author Ahmed Ashour
 */
public class HTMLBaseElement extends HTMLElement {

    private static final long serialVersionUID = 1869359649341296910L;

    /**
     * Creates an instance.
     */
    public HTMLBaseElement() {
        // Empty.
    }

    /**
     * Returns the value of the "href" property.
     * @return the value of the "href" property
     */
    public String jsxGet_href() {
        return getDomNodeOrDie().getAttribute("href");
    }

    /**
     * Sets the value of the "href" property.
     * @param href the value of the "href" property
     */
    public void jsxSet_href(final String href) {
        getDomNodeOrDie().setAttribute("href", href);
    }

    /**
     * Returns the value of the "target" property.
     * @return the value of the "target" property
     */
    public String jsxGet_target() {
        return getDomNodeOrDie().getAttribute("target");
    }

    /**
     * Sets the value of the "target" property.
     * @param target the value of the "target" property
     */
    public void jsxSet_target(final String target) {
        getDomNodeOrDie().setAttribute("target", target);
    }

}
