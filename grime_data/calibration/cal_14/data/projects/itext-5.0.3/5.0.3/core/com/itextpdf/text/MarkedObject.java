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
package com.itextpdf.text;

import java.util.ArrayList;
import java.util.Properties;

/**
 * Wrapper that allows to add properties to 'basic building block' objects.
 * Before iText 1.5 every 'basic building block' implemented the MarkupAttributes interface.
 * By setting attributes, you could add markup to the corresponding XML and/or HTML tag.
 * This functionality was hardly used by anyone, so it was removed, and replaced by
 * the MarkedObject functionality.
 */

public class MarkedObject implements Element {

	/** The element that is wrapped in a MarkedObject. */
	protected Element element;

	/** Contains extra markupAttributes */
	protected Properties markupAttributes = new Properties();

	/**
	 * This constructor is for internal use only.
	 */
	protected MarkedObject() {
		element = null;
	}

	/**
	 * Creates a MarkedObject.
	 */
	public MarkedObject(Element element) {
		this.element = element;
	}

    /**
     * Gets all the chunks in this element.
     *
     * @return  an <CODE>ArrayList</CODE>
     */
	public ArrayList<Chunk> getChunks() {
		return element.getChunks();
	}

    /**
     * Processes the element by adding it (or the different parts) to an
     * <CODE>ElementListener</CODE>.
     *
     * @param       listener        an <CODE>ElementListener</CODE>
     * @return <CODE>true</CODE> if the element was processed successfully
     */
	public boolean process(ElementListener listener) {
        try {
            return listener.add(element);
        }
        catch(DocumentException de) {
            return false;
        }
	}

    /**
     * Gets the type of the text element.
     *
     * @return  a type
     */
	public int type() {
		return MARKED;
	}

	/**
	 * @see com.itextpdf.text.Element#isContent()
	 * @since	iText 2.0.8
	 */
	public boolean isContent() {
		return true;
	}

	/**
	 * @see com.itextpdf.text.Element#isNestable()
	 * @since	iText 2.0.8
	 */
	public boolean isNestable() {
		return true;
	}

	/**
	 * Getter for the markup attributes.
	 * @return the markupAttributes
	 */
	public Properties getMarkupAttributes() {
		return markupAttributes;
	}

	/**
	 * Adds one markup attribute.
	 */
	public void setMarkupAttribute(String key, String value) {
		markupAttributes.setProperty(key, value);
	}

}