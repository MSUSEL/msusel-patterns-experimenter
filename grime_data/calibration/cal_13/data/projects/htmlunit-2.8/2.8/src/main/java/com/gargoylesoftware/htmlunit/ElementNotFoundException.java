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
package com.gargoylesoftware.htmlunit;

/**
 * An exception that is thrown when a specified XML element cannot be found in the DOM model.
 *
 * @version $Revision: 5301 $
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 */
public class ElementNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 22133747152805455L;

    private final String elementName_;
    private final String attributeName_;
    private final String attributeValue_;

    /**
     * Creates an instance from the variables that were used to search for the XML element.
     *
     * @param elementName the name of the element
     * @param attributeName the name of the attribute
     * @param attributeValue the value of the attribute
     */
    public ElementNotFoundException(
            final String elementName, final String attributeName, final String attributeValue) {
        super("elementName=[" + elementName
                 + "] attributeName=[" + attributeName
                 + "] attributeValue=[" + attributeValue + "]");

        elementName_ = elementName;
        attributeName_ = attributeName;
        attributeValue_ = attributeValue;
    }

    /**
     * Returns the name of the element.
     *
     * @return the name of the element
     */
    public String getElementName() {
        return elementName_;
    }

    /**
     * Returns the name of the attribute.
     *
     * @return the name of the attribute
     */
    public String getAttributeName() {
        return attributeName_;
    }

    /**
     * Returns the value of the attribute.
     *
     * @return the value of the attribute
     */
    public String getAttributeValue() {
        return attributeValue_;
    }
}

