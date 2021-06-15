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
package com.itextpdf.text.xml.simpleparser;

import java.util.HashMap;

/**
 * The handler for the events fired by <CODE>SimpleXMLParser</CODE>.
 * @author Paulo Soares
 */
public interface SimpleXMLDocHandler {
    /**
     * Called when a start tag is found.
     * @param tag the tag name
     * @param h the tag's attributes
     */
    public void startElement(String tag, HashMap<String, String> h);
    /**
     * Called when an end tag is found.
     * @param tag the tag name
     */
    public void endElement(String tag);
    /**
     * Called when the document starts to be parsed.
     */
    public void startDocument();
    /**
     * Called after the document is parsed.
     */
    public void endDocument();
    /**
     * Called when a text element is found.
     * @param str the text element, probably a fragment.
     */
    public void text(String str);
}