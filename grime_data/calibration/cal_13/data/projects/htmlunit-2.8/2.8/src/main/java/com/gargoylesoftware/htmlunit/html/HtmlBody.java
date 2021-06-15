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
package com.gargoylesoftware.htmlunit.html;

import java.util.Map;

import com.gargoylesoftware.htmlunit.SgmlPage;

/**
 * Wrapper for the HTML element "body".
 *
 * @version $Revision: 5301 $
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author David K. Taylor
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author Ahmed Ashour
 */
public class HtmlBody extends HtmlElement {

    /** Serial version UID. */
    private static final long serialVersionUID = -4133102076637734903L;

    /** The HTML tag represented by this element. */
    public static final String TAG_NAME = "body";

    /** Whether or not this body is temporary (created because the <tt>body</tt> tag has not yet been parsed). */
    private final boolean temporary_;

    /**
     * Creates a new instance.
     *
     * @param namespaceURI the URI that identifies an XML namespace
     * @param qualifiedName the qualified name of the element type to instantiate
     * @param page the page that contains this element
     * @param attributes the initial attributes
     * @param temporary whether or not this body is temporary (created because the <tt>body</tt>
     *        tag does not exist or has not yet been parsed)
     */
    public HtmlBody(final String namespaceURI, final String qualifiedName, final SgmlPage page,
            final Map<String, DomAttr> attributes, final boolean temporary) {

        super(namespaceURI, qualifiedName, page, attributes);

        temporary_ = temporary;

        // Force script object creation now to forward onXXX handlers to window.
        if (getOwnerDocument() instanceof HtmlPage) {
            getScriptObject();
        }
    }

    /**
     * Returns the value of the attribute "onload". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "onload" or an empty string if that attribute isn't defined
     */
    public final String getOnLoadAttribute() {
        return getAttribute("onload");
    }

    /**
     * Returns the value of the attribute "onunload". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "onunload" or an empty string if that attribute isn't defined
     */
    public final String getOnUnloadAttribute() {
        return getAttribute("onunload");
    }

    /**
     * Returns the value of the attribute "background". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "background" or an empty string if that attribute isn't defined
     */
    public final String getBackgroundAttribute() {
        return getAttribute("background");
    }

    /**
     * Returns the value of the attribute "bgcolor". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "bgcolor" or an empty string if that attribute isn't defined
     */
    public final String getBgcolorAttribute() {
        return getAttribute("bgcolor");
    }

    /**
     * Returns the value of the attribute "text". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "text" or an empty string if that attribute isn't defined
     */
    public final String getTextAttribute() {
        return getAttribute("text");
    }

    /**
     * Returns the value of the attribute "link". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "link" or an empty string if that attribute isn't defined
     */
    public final String getLinkAttribute() {
        return getAttribute("link");
    }

    /**
     * Returns the value of the attribute "vlink". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "vlink" or an empty string if that attribute isn't defined
     */
    public final String getVlinkAttribute() {
        return getAttribute("vlink");
    }

    /**
     * Returns the value of the attribute "alink". Refer to the
     * <a href='http://www.w3.org/TR/html401/'>HTML 4.01</a>
     * documentation for details on the use of this attribute.
     *
     * @return the value of the attribute "alink" or an empty string if that attribute isn't defined
     */
    public final String getAlinkAttribute() {
        return getAttribute("alink");
    }

    /**
     * Returns <tt>true</tt> if this body is temporary (created because the <tt>body</tt> tag
     * has not yet been parsed).
     *
     * @return <tt>true</tt> if this body is temporary (created because the <tt>body</tt> tag
     *         has not yet been parsed)
     */
    public final boolean isTemporary() {
        return temporary_;
    }

}
