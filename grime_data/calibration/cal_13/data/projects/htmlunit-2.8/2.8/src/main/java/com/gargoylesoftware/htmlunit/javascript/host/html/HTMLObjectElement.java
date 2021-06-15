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

import net.sourceforge.htmlunit.corejs.javascript.Context;
import net.sourceforge.htmlunit.corejs.javascript.Scriptable;

import com.gargoylesoftware.htmlunit.BrowserVersionFeatures;
import com.gargoylesoftware.htmlunit.javascript.SimpleScriptable;
import com.gargoylesoftware.htmlunit.javascript.host.ActiveXObjectImpl;
import com.gargoylesoftware.htmlunit.javascript.host.FormChild;

/**
 * The JavaScript object "HTMLObjectElement".
 *
 * @version $Revision: 5864 $
 * @author Ahmed Ashour
 */
public class HTMLObjectElement extends FormChild {

    private static final long serialVersionUID = -916091257587937486L;

    private SimpleScriptable wrappedActiveX_;

    /**
     * Creates an instance.
     */
    public HTMLObjectElement() {
        // Empty.
    }

    /**
     * Returns the value of the "alt" property.
     * @return the value of the "alt" property
     */
    public String jsxGet_alt() {
        String alt = getDomNodeOrDie().getAttribute("alt");
        if (alt == NOT_FOUND) {
            alt = "";
        }
        return alt;
    }

    /**
     * Returns the value of the "alt" property.
     * @param alt the value
     */
    public void jsxSet_alt(final String alt) {
        getDomNodeOrDie().setAttribute("alt", alt);
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
     * Gets the "classid" attribute.
     * @return the "classid" attribute
     */
    public String jsxGet_classid() {
        String classid = getDomNodeOrDie().getAttribute("classid");
        if (classid == NOT_FOUND) {
            classid = "";
        }
        return classid;
    }

    /**
     * Sets the "classid" attribute.
     * @param classid the "classid" attribute
     */
    public void jsxSet_classid(final String classid) {
        getDomNodeOrDie().setAttribute("classid", classid);
        if (classid.indexOf(':') != -1 && getBrowserVersion().hasFeature(BrowserVersionFeatures.GENERATED_86)
                && getWindow().getWebWindow().getWebClient().isActiveXNative()
                && System.getProperty("os.name").contains("Windows")) {
            try {
                wrappedActiveX_ = new ActiveXObjectImpl(classid);
                wrappedActiveX_.setParentScope(getParentScope());
            }
            catch (final Exception e) {
                Context.throwAsScriptRuntimeEx(e);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object get(final String name, final Scriptable start) {
        if (wrappedActiveX_ != null) {
            return wrappedActiveX_.get(name, start);
        }
        return super.get(name, start);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void put(final String name, final Scriptable start, final Object value) {
        if (wrappedActiveX_ != null) {
            wrappedActiveX_.put(name, start, value);
        }
        else {
            super.put(name, start, value);
        }
    }
}
