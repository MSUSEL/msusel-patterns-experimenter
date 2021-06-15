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
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLElement;

/**
 * A JavaScript object for a BoxObject.
 *
 * @version $Revision: 5301 $
 * @author <a href="mailto:sam@redspr.com">Sam Hough</a>
 */
public class BoxObject extends SimpleScriptable {

    private static final long serialVersionUID = -6650009325965623469L;

    /** The element to which this box object corresponds. */
    private final HTMLElement element_;

    /**
     * Creates a new instance. JavaScript objects must have a default constructor.
     */
    public BoxObject() {
        this(null);
    }

    /**
     * Creates a new instance.
     * @param element the element to which this box object corresponds
     */
    public BoxObject(final HTMLElement element) {
        element_ = element;
    }

    /**
     * Returns the element to which this box object corresponds.
     * @return the element to which this box object corresponds
     */
    public HTMLElement jsxGet_element() {
        return element_;
    }

    /**
     * Returns this box object's element's first child.
     * @return this box object's element's first child
     */
    public Object jsxGet_firstChild() {
        return element_.jsxGet_firstChild();
    }

    /**
     * Returns this box object's element's last child.
     * @return this box object's element's last child
     */
    public Object jsxGet_lastChild() {
        return element_.jsxGet_lastChild();
    }

    /**
     * Returns this box object's element's next sibling.
     * @return this box object's element's next sibling
     */
    public Object jsxGet_nextSibling() {
        return element_.jsxGet_nextSibling();
    }

    /**
     * Returns this box object's element's previous sibling.
     * @return this box object's element's previous sibling
     */
    public Object jsxGet_previousSibling() {
        return element_.jsxGet_previousSibling();
    }

    /**
     * Returns the X position of this box object's element.
     * @return the X position of this box object's element
     */
    public int jsxGet_x() {
        return element_.getPosX();
    }

    /**
     * Returns the Y position of this box object's element.
     * @return the Y position of this box object's element
     */
    public int jsxGet_y() {
        return element_.getPosY();
    }

    /**
     * Returns the <tt>screenX</tt> property. Testing in FF2 suggests that this value is always
     * the same as the value returned by the <tt>x</tt> property.
     *
     * @return the <tt>screenX</tt> property
     */
    public int jsxGet_screenX() {
        return jsxGet_x();
    }

    /**
     * Returns the <tt>screenY</tt> property. Testing in FF2 suggests that this value is always
     * equal to the value returned by the <tt>y</tt> property plus <tt>121</tt> (probably for the
     * title bar, menu bar and toolbar).
     *
     * @return the <tt>screenY</tt> property
     */
    public int jsxGet_screenY() {
        return jsxGet_y() + 121;
    }

    /**
     * Returns the width of this box object's element, including padding, excluding margin and border.
     * @return the width of this box object's element, including padding, excluding margin and border
     */
    public int jsxGet_width() {
        return element_.jsxGet_clientWidth();
    }

    /**
     * Returns the height of this box object's element, including padding, excluding margin and border.
     * @return the height of this box object's element, including padding, excluding margin and border
     */
    public int jsxGet_height() {
        return element_.jsxGet_clientHeight();
    }

}
