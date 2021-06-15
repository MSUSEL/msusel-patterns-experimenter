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

import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.DomText;

/**
 * The JavaScript object that represents an "HTMLScriptElement".
 *
 * @version $Revision: 5301 $
 * @author Daniel Gredler
 * @author Marc Guillemot
 * @author Ahmed Ashour
 */
public class HTMLScriptElement extends HTMLElement {

    private static final long serialVersionUID = -4626517931702326308L;

    /**
     * Creates an instance.
     */
    public HTMLScriptElement() {
        // Empty.
    }

    /**
     * Returns the <tt>src</tt> attribute.
     * @return the <tt>src</tt> attribute
     */
    public String jsxGet_src() {
        return getDomNodeOrDie().getAttribute("src");
    }

    /**
     * Sets the <tt>src</tt> attribute.
     * @param src the <tt>src</tt> attribute
     */
    public void jsxSet_src(final String src) {
        getDomNodeOrDie().setAttribute("src", src);
    }

    /**
     * Returns the <tt>text</tt> attribute.
     * @return the <tt>text</tt> attribute
     */
    @Override
    public String jsxGet_text() {
        final DomNode firstChild = getDomNodeOrDie().getFirstChild();
        if (firstChild != null) {
            return firstChild.getNodeValue();
        }
        return "";
    }

    /**
     * Sets the <tt>text</tt> attribute.
     * @param text the <tt>text</tt> attribute
     */
    public void jsxSet_text(final String text) {
        final DomNode htmlElement = getDomNodeOrDie();
        DomNode firstChild = htmlElement.getFirstChild();
        if (firstChild == null) {
            firstChild = new DomText(htmlElement.getPage(), text);
            htmlElement.appendChild(firstChild);
        }
        else {
            firstChild.setNodeValue(text);
        }
    }

    /**
     * Returns the <tt>type</tt> attribute.
     * @return the <tt>type</tt> attribute
     */
    public String jsxGet_type() {
        return getDomNodeOrDie().getAttribute("type");
    }

    /**
     * Sets the <tt>type</tt> attribute.
     * @param type the <tt>type</tt> attribute
     */
    public void jsxSet_type(final String type) {
        getDomNodeOrDie().setAttribute("type", type);
    }

    /**
     * Returns the event handler that fires on every state change.
     * @return the event handler that fires on every state change
     */
    public Object jsxGet_onreadystatechange() {
        return getEventHandlerProp("onreadystatechange");
    }

    /**
     * Sets the event handler that fires on every state change.
     * @param handler the event handler that fires on every state change
     */
    public void jsxSet_onreadystatechange(final Object handler) {
        setEventHandlerProp("onreadystatechange", handler);
    }

    /**
     * Returns the event handler that fires on load.
     * @return the event handler that fires on load
     */
    public Object jsxGet_onload() {
        return getEventHandlerProp("onload");
    }

    /**
     * Sets the event handler that fires on load.
     * @param handler the event handler that fires on load
     */
    public void jsxSet_onload(final Object handler) {
        setEventHandlerProp("onload", handler);
    }

    /**
     * Returns the ready state of the script. This is an IE-only property.
     * @return the ready state of the script
     * @see DomNode#READY_STATE_UNINITIALIZED
     * @see DomNode#READY_STATE_LOADING
     * @see DomNode#READY_STATE_LOADED
     * @see DomNode#READY_STATE_INTERACTIVE
     * @see DomNode#READY_STATE_COMPLETE
     */
    public String jsxGet_readyState() {
        final DomNode node = getDomNodeOrDie();
        return node.getReadyState();
    }

}
