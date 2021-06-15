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

import java.net.MalformedURLException;

import net.sourceforge.htmlunit.corejs.javascript.Context;

import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.host.css.ComputedCSSStyleDeclaration;

/**
 * The JavaScript object "HTMLBodyElement".
 *
 * @version $Revision: 5864 $
 * @author Ahmed Ashour
 * @author Marc Guillemot
 * @author Daniel Gredler
 */
public class HTMLBodyElement extends HTMLElement {

    private static final long serialVersionUID = -915040139319661419L;

    /**
     * Creates a new instance.
     */
    public HTMLBodyElement() {
        // Empty.
    }

    /**
     * Creates the event handler from the attribute value. This has to be done no matter which browser
     * is simulated to handle ill-formed HTML code with many body (possibly generated) elements.
     * @param attributeName the attribute name
     * @param value the value
     */
    public void createEventHandlerFromAttribute(final String attributeName, final String value) {
        // when many body tags are found while parsing, attributes of
        // different tags are added and should create an event handler when needed
        if (attributeName.toLowerCase().startsWith("on")) {
            createEventHandler(attributeName, value);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDefaults(final ComputedCSSStyleDeclaration style) {
        if (getBrowserVersion().hasFeature(BrowserVersionFeatures.GENERATED_41)) {
            style.setDefaultLocalStyleAttribute("margin", "15px 10px");
            style.setDefaultLocalStyleAttribute("padding", "0px");
        }
        else {
            style.setDefaultLocalStyleAttribute("margin-left", "8px");
            style.setDefaultLocalStyleAttribute("margin-right", "8px");
            style.setDefaultLocalStyleAttribute("margin-top", "8px");
            style.setDefaultLocalStyleAttribute("margin-bottom", "8px");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HTMLElement jsxGet_offsetParent() {
        return null;
    }

    /**
     * Returns the value of the <tt>aLink</tt> attribute.
     * @return the value of the <tt>aLink</tt> attribute
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms533070.aspx">MSDN Documentation</a>
     */
    public String jsxGet_aLink() {
        String aLink = getDomNodeOrDie().getAttribute("aLink");
        if (aLink == DomElement.ATTRIBUTE_NOT_DEFINED
                && getBrowserVersion().hasFeature(BrowserVersionFeatures.HTML_BODY_COLOR)) {
            aLink = "#ee0000";
        }
        return aLink;
    }

    /**
     * Sets the value of the <tt>aLink</tt> attribute.
     * @param aLink the value of the <tt>aLink</tt> attribute
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms533070.aspx">MSDN Documentation</a>
     */
    public void jsxSet_aLink(final String aLink) {
        setColorAttribute("aLink", aLink);
    }

    /**
     * Returns the value of the <tt>background</tt> attribute.
     * @return the value of the <tt>background</tt> attribute
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms533498.aspx">MSDN Documentation</a>
     */
    public String jsxGet_background() {
        final HtmlElement node = getDomNodeOrDie();
        String background = node.getAttribute("background");
        if (background != DomElement.ATTRIBUTE_NOT_DEFINED
                && getBrowserVersion().hasFeature(BrowserVersionFeatures.GENERATED_158)) {
            try {
                final HtmlPage page = (HtmlPage) node.getPage();
                background = page.getFullyQualifiedUrl(background).toExternalForm();
            }
            catch (final MalformedURLException e) {
                Context.throwAsScriptRuntimeEx(e);
            }
        }
        return background;
    }

    /**
     * Sets the value of the <tt>background</tt> attribute.
     * @param background the value of the <tt>background</tt> attribute
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms533498.aspx">MSDN Documentation</a>
     */
    public void jsxSet_background(final String background) {
        getDomNodeOrDie().setAttribute("background", background);
    }

    /**
     * Returns the value of the <tt>bgColor</tt> attribute.
     * @return the value of the <tt>bgColor</tt> attribute
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms533505.aspx">MSDN Documentation</a>
     */
    public String jsxGet_bgColor() {
        String bgColor = getDomNodeOrDie().getAttribute("bgColor");
        if (bgColor == DomElement.ATTRIBUTE_NOT_DEFINED
                && getBrowserVersion().hasFeature(BrowserVersionFeatures.HTML_BODY_COLOR)) {
            bgColor = "#ffffff";
        }
        return bgColor;
    }

    /**
     * Sets the value of the <tt>bgColor</tt> attribute.
     * @param bgColor the value of the <tt>bgColor</tt> attribute
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms533505.aspx">MSDN Documentation</a>
     */
    public void jsxSet_bgColor(final String bgColor) {
        setColorAttribute("bgColor", bgColor);
    }

    /**
     * Returns the value of the <tt>link</tt> attribute.
     * @return the value of the <tt>link</tt> attribute
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms534119.aspx">MSDN Documentation</a>
     */
    public String jsxGet_link() {
        String link = getDomNodeOrDie().getAttribute("link");
        if (link == DomElement.ATTRIBUTE_NOT_DEFINED
                && getBrowserVersion().hasFeature(BrowserVersionFeatures.HTML_BODY_COLOR)) {
            link = "#0000ee";
        }
        return link;
    }

    /**
     * Sets the value of the <tt>link</tt> attribute.
     * @param link the value of the <tt>link</tt> attribute
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms534119.aspx">MSDN Documentation</a>
     */
    public void jsxSet_link(final String link) {
        setColorAttribute("link", link);
    }

    /**
     * Returns the value of the <tt>text</tt> attribute.
     * @return the value of the <tt>text</tt> attribute
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms534677.aspx">MSDN Documentation</a>
     */
    @Override
    public String jsxGet_text() {
        String text = getDomNodeOrDie().getAttribute("text");
        if (text == DomElement.ATTRIBUTE_NOT_DEFINED
                && getBrowserVersion().hasFeature(BrowserVersionFeatures.HTML_BODY_COLOR)) {
            text = "#000000";
        }
        return text;
    }

    /**
     * Sets the value of the <tt>text</tt> attribute.
     * @param text the value of the <tt>text</tt> attribute
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms534677.aspx">MSDN Documentation</a>
     */
    public void jsxSet_text(final String text) {
        setColorAttribute("text", text);
    }

    /**
     * Returns the value of the <tt>vLink</tt> attribute.
     * @return the value of the <tt>vLink</tt> attribute
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms534677.aspx">MSDN Documentation</a>
     */
    public String jsxGet_vLink() {
        String vLink = getDomNodeOrDie().getAttribute("vLink");
        if (vLink == DomElement.ATTRIBUTE_NOT_DEFINED
                && getBrowserVersion().hasFeature(BrowserVersionFeatures.HTML_BODY_COLOR)) {
            vLink = "#551a8b";
        }
        return vLink;
    }

    /**
     * Sets the value of the <tt>vLink</tt> attribute.
     * @param vLink the value of the <tt>vLink</tt> attribute
     * @see <a href="http://msdn.microsoft.com/en-us/library/ms534677.aspx">MSDN Documentation</a>
     */
    public void jsxSet_vLink(final String vLink) {
        setColorAttribute("vLink", vLink);
    }

}
