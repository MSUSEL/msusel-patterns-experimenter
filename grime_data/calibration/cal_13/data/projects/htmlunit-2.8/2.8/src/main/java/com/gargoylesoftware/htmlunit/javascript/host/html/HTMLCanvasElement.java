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

import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.javascript.host.canvas.CanvasRenderingContext2D;

/**
 * A JavaScript object for {@link com.gargoylesoftware.htmlunit.html.HtmlCanvas}.
 *
 * @version $Revision: 5301 $
 * @author Ahmed Ashour
 */
public class HTMLCanvasElement extends HTMLElement {

    private static final long serialVersionUID = 2198667710163712419L;

    /**
     * Creates an instance.
     */
    public HTMLCanvasElement() {
    }

    /**
     * Returns the "width" property.
     * @return the "width" property
     */
    public String jsxGet_width() {
        String width = getDomNodeOrDie().getAttribute("width");
        if (width == DomElement.ATTRIBUTE_NOT_DEFINED) {
            width = "300";
        }
        return width;
    }

    /**
     * Sets the "width" property.
     * @param width the "width" property
     */
    public void jsxSet_width(final String width) {
        getDomNodeOrDie().setAttribute("width", width);
    }

    /**
     * Returns the "height" property.
     * @return the "height" property
     */
    public String jsxGet_height() {
        String height = getDomNodeOrDie().getAttribute("height");
        if (height == DomElement.ATTRIBUTE_NOT_DEFINED) {
            height = "150";
        }
        return height;
    }

    /**
     * Sets the "height" property.
     * @param height the "height" property
     */
    public void jsxSet_height(final String height) {
        getDomNodeOrDie().setAttribute("height", height);
    }

    /**
     * Gets the context.
     * @param contextId the context id
     * @return Returns an object that exposes an API for drawing on the canvas,
     * or null if the given context ID is not supported
     */
    public Object jsxFunction_getContext(final String contextId) {
        if (contextId.equals("2d")) {
            final CanvasRenderingContext2D context = new CanvasRenderingContext2D();
            context.setParentScope(getParentScope());
            context.setPrototype(getPrototype(context.getClass()));
            return context;
        }
        return null;
    }
}
