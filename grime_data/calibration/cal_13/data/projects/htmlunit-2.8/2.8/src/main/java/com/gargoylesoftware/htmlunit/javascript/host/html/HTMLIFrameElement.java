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
import com.gargoylesoftware.htmlunit.html.BaseFrame;
import com.gargoylesoftware.htmlunit.javascript.host.Window;

/**
 * A JavaScript object for {@link com.gargoylesoftware.htmlunit.html.HtmlInlineFrame}.
 *
 * @version $Revision: 5864 $
 * @author Marc Guillemot
 * @author Chris Erskine
 * @author Ahmed Ashour
 */
public class HTMLIFrameElement extends HTMLElement {

    private static final long serialVersionUID = -7005081332114203694L;

    /**
     * Creates an instance. A default constructor is required for all JavaScript objects.
     */
    public HTMLIFrameElement() { }

    /**
     * Returns the value of URL loaded in the frame.
     * @return the value of this attribute
     */
    public String jsxGet_src() {
        return getFrame().getSrcAttribute();
    }

    /**
     * Returns the document the frame contains, if any.
     * @return <code>null</code> if no document is contained
     * @see <a href="http://www.mozilla.org/docs/dom/domref/dom_frame_ref4.html">Gecko DOM Reference</a>
     */
    public HTMLDocumentProxy jsxGet_contentDocument() {
        return ((Window) getFrame().getEnclosedWindow().getScriptObject()).jsxGet_document();
    }

    /**
     * Returns the window the frame contains, if any.
     * @return the window
     * @see <a href="http://www.mozilla.org/docs/dom/domref/dom_frame_ref5.html">
     * Gecko DOM Reference</a>
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms533692.aspx">MSDN documentation</a>
     */
    public Window jsxGet_contentWindow() {
        return (Window) getFrame().getEnclosedWindow().getScriptObject();
    }

    /**
     * Sets the value of the source of the contained frame.
     * @param src the new value
     */
    public void jsxSet_src(final String src) {
        getFrame().setSrcAttribute(src);
    }

    /**
     * Returns the value of the name attribute.
     * @return the value of this attribute
     */
    public String jsxGet_name() {
        return getFrame().getNameAttribute();
    }

    /**
     * Sets the value of the name attribute.
     * @param name the new value
     */
    public void jsxSet_name(final String name) {
        getFrame().setNameAttribute(name);
    }

    private BaseFrame getFrame() {
        return (BaseFrame) getDomNodeOrDie();
    }

    /**
     * Sets the <tt>onload</tt> event handler for this element.
     * @param eventHandler the <tt>onload</tt> event handler for this element
     */
    public void jsxSet_onload(final Object eventHandler) {
        setEventHandlerProp("onload", eventHandler);
    }

    /**
     * Returns the <tt>onload</tt> event handler for this element.
     * @return the <tt>onload</tt> event handler for this element
     */
    public Object jsxGet_onload() {
        return getEventHandlerProp("onload");
    }

    /**
     * Gets the "border" attribute.
     * @return the "border" attribute
     */
    public String jsxGet_border() {
        String border = getDomNodeOrDie().getAttribute("border");
        if (border == NOT_FOUND) {
            border = "";
        }
        return border;
    }

    /**
     * Sets the "border" attribute.
     * @param border the "border" attribute
     */
    public void jsxSet_border(final String border) {
        getDomNodeOrDie().setAttribute("border", border);
    }

    /**
     * Returns the value of the "align" property.
     * @return the value of the "align" property
     */
    public String jsxGet_align() {
        return getAlign(true);
    }

    /**
     * Sets the value of the "align" property.
     * @param align the value of the "align" property
     */
    public void jsxSet_align(final String align) {
        setAlign(align, false);
    }

    /**
     * Returns the value of the "width" property.
     * @return the value of the "width" property
     */
    public String jsxGet_width() {
        final boolean ie = getBrowserVersion().hasFeature(BrowserVersionFeatures.GENERATED_82);
        final Boolean returnNegativeValues = ie ? true : null;
        return getWidthOrHeight("width", returnNegativeValues);
    }

    /**
     * Sets the value of the "width" property.
     * @param width the value of the "width" property
     */
    public void jsxSet_width(final String width) {
        setWidthOrHeight("width", width, true);
    }

    /**
     * Returns the value of the "width" property.
     * @return the value of the "width" property
     */
    public String jsxGet_height() {
        final boolean ie = getBrowserVersion().hasFeature(BrowserVersionFeatures.GENERATED_83);
        final Boolean returnNegativeValues = ie ? true : null;
        return getWidthOrHeight("height", returnNegativeValues);
    }

    /**
     * Sets the value of the "width" property.
     * @param width the value of the "width" property
     */
    public void jsxSet_height(final String width) {
        setWidthOrHeight("height", width, true);
    }

}
