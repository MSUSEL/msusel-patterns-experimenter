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

import org.apache.commons.lang.StringUtils;

/**
 * A node which supports all of the <tt>onXXX</tt> event handlers and other event-related functions.
 *
 * Basically contains any event-related features that both elements and the document support.
 *
 * @version $Revision: 5301 $
 * @author Daniel Gredler
 * @author Ahmed Ashour
 */
public class EventNode extends Node {

    /** Serial version UID. */
    private static final long serialVersionUID = 4894810197917509182L;

    /**
     * Sets the <tt>onclick</tt> event handler for this element.
     * @param handler the <tt>onclick</tt> event handler for this element
     */
    public void jsxSet_onclick(final Object handler) {
        setEventHandlerProp("onclick", handler);
    }

    /**
     * Returns the <tt>onclick</tt> event handler for this element.
     * @return the <tt>onclick</tt> event handler for this element
     */
    public Object jsxGet_onclick() {
        return getEventHandlerProp("onclick");
    }

    /**
     * Sets the <tt>ondblclick</tt> event handler for this element.
     * @param handler the <tt>ondblclick</tt> event handler for this element
     */
    public void jsxSet_ondblclick(final Object handler) {
        setEventHandlerProp("ondblclick", handler);
    }

    /**
     * Returns the <tt>ondblclick</tt> event handler for this element.
     * @return the <tt>ondblclick</tt> event handler for this element
     */
    public Object jsxGet_ondblclick() {
        return getEventHandlerProp("ondblclick");
    }

    /**
     * Sets the <tt>onblur</tt> event handler for this element.
     * @param handler the <tt>onblur</tt> event handler for this element
     */
    public void jsxSet_onblur(final Object handler) {
        setEventHandlerProp("onblur", handler);
    }

    /**
     * Returns the <tt>onblur</tt> event handler for this element.
     * @return the <tt>onblur</tt> event handler for this element
     */
    public Object jsxGet_onblur() {
        return getEventHandlerProp("onblur");
    }

    /**
     * Sets the <tt>onfocus</tt> event handler for this element.
     * @param handler the <tt>onfocus</tt> event handler for this element
     */
    public void jsxSet_onfocus(final Object handler) {
        setEventHandlerProp("onfocus", handler);
    }

    /**
     * Returns the <tt>onfocus</tt> event handler for this element.
     * @return the <tt>onfocus</tt> event handler for this element
     */
    public Object jsxGet_onfocus() {
        return getEventHandlerProp("onfocus");
    }

    /**
     * Sets the <tt>onfocusin</tt> event handler for this element.
     * @param handler the <tt>onfocusin</tt> event handler for this element
     */
    public void jsxSet_onfocusin(final Object handler) {
        setEventHandlerProp("onfocusin", handler);
    }

    /**
     * Returns the <tt>onfocusin</tt> event handler for this element.
     * @return the <tt>onfocusin</tt> event handler for this element
     */
    public Object jsxGet_onfocusin() {
        return getEventHandlerProp("onfocusin");
    }

    /**
     * Sets the <tt>onfocusout</tt> event handler for this element.
     * @param handler the <tt>onfocusout</tt> event handler for this element
     */
    public void jsxSet_onfocusout(final Object handler) {
        setEventHandlerProp("onfocusout", handler);
    }

    /**
     * Returns the <tt>onfocusout</tt> event handler for this element.
     * @return the <tt>onfocusout</tt> event handler for this element
     */
    public Object jsxGet_onfocusout() {
        return getEventHandlerProp("onfocusout");
    }

    /**
     * Sets the <tt>onkeydown</tt> event handler for this element.
     * @param handler the <tt>onkeydown</tt> event handler for this element
     */
    public void jsxSet_onkeydown(final Object handler) {
        setEventHandlerProp("onkeydown", handler);
    }

    /**
     * Returns the <tt>onkeydown</tt> event handler for this element.
     * @return the <tt>onkeydown</tt> event handler for this element
     */
    public Object jsxGet_onkeydown() {
        return getEventHandlerProp("onkeydown");
    }

    /**
     * Sets the <tt>onkeypress</tt> event handler for this element.
     * @param handler the <tt>onkeypress</tt> event handler for this element
     */
    public void jsxSet_onkeypress(final Object handler) {
        setEventHandlerProp("onkeypress", handler);
    }

    /**
     * Returns the <tt>onkeypress</tt> event handler for this element.
     * @return the <tt>onkeypress</tt> event handler for this element
     */
    public Object jsxGet_onkeypress() {
        return getEventHandlerProp("onkeypress");
    }

    /**
     * Sets the <tt>onkeyup</tt> event handler for this element.
     * @param handler the <tt>onkeyup</tt> event handler for this element
     */
    public void jsxSet_onkeyup(final Object handler) {
        setEventHandlerProp("onkeyup", handler);
    }

    /**
     * Returns the <tt>onkeyup</tt> event handler for this element.
     * @return the <tt>onkeyup</tt> event handler for this element
     */
    public Object jsxGet_onkeyup() {
        return getEventHandlerProp("onkeyup");
    }

    /**
     * Sets the <tt>onmousedown</tt> event handler for this element.
     * @param handler the <tt>onmousedown</tt> event handler for this element
     */
    public void jsxSet_onmousedown(final Object handler) {
        setEventHandlerProp("onmousedown", handler);
    }

    /**
     * Returns the <tt>onmousedown</tt> event handler for this element.
     * @return the <tt>onmousedown</tt> event handler for this element
     */
    public Object jsxGet_onmousedown() {
        return getEventHandlerProp("onmousedown");
    }

    /**
     * Sets the <tt>onmousemove</tt> event handler for this element.
     * @param handler the <tt>onmousemove</tt> event handler for this element
     */
    public void jsxSet_onmousemove(final Object handler) {
        setEventHandlerProp("onmousemove", handler);
    }

    /**
     * Returns the <tt>onmousemove</tt> event handler for this element.
     * @return the <tt>onmousemove</tt> event handler for this element
     */
    public Object jsxGet_onmousemove() {
        return getEventHandlerProp("onmousemove");
    }

    /**
     * Sets the <tt>onmouseout</tt> event handler for this element.
     * @param handler the <tt>onmouseout</tt> event handler for this element
     */
    public void jsxSet_onmouseout(final Object handler) {
        setEventHandlerProp("onmouseout", handler);
    }

    /**
     * Returns the <tt>onmouseout</tt> event handler for this element.
     * @return the <tt>onmouseout</tt> event handler for this element
     */
    public Object jsxGet_onmouseout() {
        return getEventHandlerProp("onmouseout");
    }

    /**
     * Sets the <tt>onmouseover</tt> event handler for this element.
     * @param handler the <tt>onmouseover</tt> event handler for this element
     */
    public void jsxSet_onmouseover(final Object handler) {
        setEventHandlerProp("onmouseover", handler);
    }

    /**
     * Returns the <tt>onmouseover</tt> event handler for this element.
     * @return the <tt>onmouseover</tt> event handler for this element
     */
    public Object jsxGet_onmouseover() {
        return getEventHandlerProp("onmouseover");
    }

    /**
     * Sets the <tt>onmouseup</tt> event handler for this element.
     * @param handler the <tt>onmouseup</tt> event handler for this element
     */
    public void jsxSet_onmouseup(final Object handler) {
        setEventHandlerProp("onmouseup", handler);
    }

    /**
     * Returns the <tt>onmouseup</tt> event handler for this element.
     * @return the <tt>onmouseup</tt> event handler for this element
     */
    public Object jsxGet_onmouseup() {
        return getEventHandlerProp("onmouseup");
    }

    /**
     * Sets the <tt>oncontextmenu</tt> event handler for this element.
     * @param handler the <tt>oncontextmenu</tt> event handler for this element
     */
    public void jsxSet_oncontextmenu(final Object handler) {
        setEventHandlerProp("oncontextmenu", handler);
    }

    /**
     * Returns the <tt>oncontextmenu</tt> event handler for this element.
     * @return the <tt>oncontextmenu</tt> event handler for this element
     */
    public Object jsxGet_oncontextmenu() {
        return getEventHandlerProp("oncontextmenu");
    }

    /**
     * Sets the <tt>onresize</tt> event handler for this element.
     * @param handler the <tt>onresize</tt> event handler for this element
     */
    public void jsxSet_onresize(final Object handler) {
        setEventHandlerProp("onresize", handler);
    }

    /**
     * Returns the <tt>onresize</tt> event handler for this element.
     * @return the <tt>onresize</tt> event handler for this element
     */
    public Object jsxGet_onresize() {
        return getEventHandlerProp("onresize");
    }

    /**
     * Sets the <tt>onpropertychange</tt> event handler for this element.
     * @param handler the <tt>onpropertychange</tt> event handler for this element
     */
    public void jsxSet_onpropertychange(final Object handler) {
        setEventHandlerProp("onpropertychange", handler);
    }

    /**
     * Returns the <tt>onpropertychange</tt> event handler for this element.
     * @return the <tt>onpropertychange</tt> event handler for this element
     */
    public Object jsxGet_onpropertychange() {
        return getEventHandlerProp("onpropertychange");
    }

    /**
     * Sets the <tt>onerror</tt> event handler for this element.
     * @param handler the <tt>onerror</tt> event handler for this element
     */
    public void jsxSet_onerror(final Object handler) {
        setEventHandlerProp("onerror", handler);
    }

    /**
     * Returns the <tt>onerror</tt> event handler for this element.
     * @return the <tt>onerror</tt> event handler for this element
     */
    public Object jsxGet_onerror() {
        return getEventHandlerProp("onerror");
    }

    /**
     * Fires a specified event on this element (IE only). See the
     * <a href="http://msdn.microsoft.com/en-us/library/ms536423.aspx">MSDN documentation</a>
     * for more information.
     * @param type specifies the name of the event to fire.
     * @param event specifies the event object from which to obtain event object properties.
     * @return <tt>true</tt> if the event fired successfully, <tt>false</tt> if it was canceled
     */
    public boolean jsxFunction_fireEvent(final String type, Event event) {
        if (event == null) {
            event = new MouseEvent();
        }

        // Create the event, whose class will depend on the type specified.
        final String cleanedType = StringUtils.removeStart(type.toLowerCase(), "on");
        if (MouseEvent.isMouseEvent(cleanedType)) {
            event.setPrototype(getPrototype(MouseEvent.class));
        }
        else {
            event.setPrototype(getPrototype(Event.class));
        }
        event.setParentScope(getWindow());

        // These four properties have predefined values, independent of the template.
        event.jsxSet_cancelBubble(false);
        event.jsxSet_returnValue(Boolean.TRUE);
        event.jsxSet_srcElement(this);
        event.setEventType(cleanedType);

        fireEvent(event);
        return ((Boolean) event.jsxGet_returnValue()).booleanValue();
    }

}
