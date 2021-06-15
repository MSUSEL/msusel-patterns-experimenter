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

import java.util.EventObject;

/**
 * This is the event class for notifications about changes to the attributes of the
 * HtmlElement.
 *
 * @version $Revision: 5726 $
 * @author Ahmed Ashour
 * @see HtmlAttributeChangeListener
 */
public class HtmlAttributeChangeEvent extends EventObject {

    private static final long serialVersionUID = -7432080435641028075L;

    private final String name_;
    private final String value_;

    /**
     * Constructs a new AttributeEvent from the given element, for the given attribute name and attribute value.
     *
     * @param element the element that is sending the event
     * @param name the name of the attribute that changed on the element
     * @param value the value of the attribute that has been added, removed, or replaced
     */
    public HtmlAttributeChangeEvent(final HtmlElement element, final String name, final String value) {
        super(element);
        name_ = name;
        value_ = value;
    }

    /**
     * Returns the HtmlElement that changed.
     * @return the HtmlElement that sent the event
     */
    public HtmlElement getHtmlElement() {
        return (HtmlElement) getSource();
    }

    /**
     * Returns the name of the attribute that changed on the element.
     * @return the name of the attribute that changed on the element
     */
    public String getName() {
        return name_;
    }

    /**
     * Returns the value of the attribute that has been added, removed, or replaced.
     * If the attribute was added, this is the value of the attribute.
     * If the attribute was removed, this is the value of the removed attribute.
     * If the attribute was replaced, this is the old value of the attribute.
     *
     * @return the value of the attribute that has been added, removed, or replaced
     */
    public String getValue() {
        return value_;
    }
}
