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

import net.sourceforge.htmlunit.corejs.javascript.Function;

import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlForm;

/**
 * Base class for all JavaScript object corresponding to form fields.
 *
 * @version $Revision: 5301 $
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author <a href="mailto:cse@dynabean.de">Christian Sell</a>
 * @author Marc Guillemot
 * @author Chris Erskine
 * @author Ahmed Ashour
 */
public class FormField extends FormChild {

    private static final long serialVersionUID = 3712016051364495710L;

    /**
     * Sets the associated DOM node and sets the enclosing form as parent scope of the current element.
     * @see com.gargoylesoftware.htmlunit.javascript.SimpleScriptable#setDomNode(DomNode)
     * @param domNode the DOM node
     */
    @Override
    public void setDomNode(final DomNode domNode) {
        super.setDomNode(domNode);

        final HtmlForm form = ((HtmlElement) domNode).getEnclosingForm();
        if (form != null) {
            setParentScope(getScriptableFor(form));
        }
    }

    /**
     * Returns the value of the JavaScript attribute "value".
     *
     * @return the value of this attribute
     */
    public String jsxGet_value() {
        return getDomNodeOrDie().getAttribute("value");
    }

    /**
     * Sets the value of the JavaScript attribute "value".
     *
     * @param newValue  the new value
     */
    public void jsxSet_value(final String newValue) {
        getDomNodeOrDie().setAttribute("value", newValue);
    }

    /**
     * Returns the value of the JavaScript attribute "name".
     *
     * @return the value of this attribute
     */
    public String jsxGet_name() {
        return getDomNodeOrDie().getAttribute("name");
    }

    /**
     * Sets the value of the JavaScript attribute "name".
     *
     * @param newName  the new name
     */
    public void jsxSet_name(final String newName) {
        getDomNodeOrDie().setAttribute("name", newName);
    }

    /**
     * Returns the value of the JavaScript attribute "type".
     *
     * @return the value of this attribute
     */
    public String jsxGet_type() {
        return getDomNodeOrDie().getAttribute("type");
    }

    /**
     * Sets the <tt>onchange</tt> event handler for this element.
     * @param onchange the <tt>onchange</tt> event handler for this element
     */
    public void jsxSet_onchange(final Object onchange) {
        setEventHandlerProp("onchange", onchange);
    }

    /**
     * Returns the <tt>onchange</tt> event handler for this element.
     * @return the <tt>onchange</tt> event handler for this element
     */
    public Function jsxGet_onchange() {
        return getEventHandler("onchange");
    }
}
