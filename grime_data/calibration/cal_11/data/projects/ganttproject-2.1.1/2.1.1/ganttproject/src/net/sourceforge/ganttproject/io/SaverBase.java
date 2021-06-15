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
package net.sourceforge.ganttproject.io;

import javax.xml.transform.sax.TransformerHandler;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

class SaverBase {
    protected void startElement(String name, TransformerHandler handler)
            throws SAXException {
        startElement(name, ourEmptyAttributes, handler);
    }

    protected void startElement(String name, AttributesImpl attrs,
            TransformerHandler handler) throws SAXException {
        handler.startElement("", name, name, attrs);
        attrs.clear();
    }

    protected void endElement(String name, TransformerHandler handler)
            throws SAXException {
        handler.endElement("", name, name);
    }

    protected void addAttribute(String name, String value, AttributesImpl attrs) {
    	if (value!=null) { 
    		attrs.addAttribute("", name, name, "CDATA", value);
    	}
    }

	protected void addAttribute(String name, int value, AttributesImpl attrs) {
		addAttribute(name, String.valueOf(value), attrs);
	}
    
    protected void emptyElement(String name, AttributesImpl attrs,
            TransformerHandler handler) throws SAXException {
        startElement(name, attrs, handler);
        endElement(name, handler);
        attrs.clear();
    }

    protected void cdataElement(String name, String cdata, AttributesImpl attrs, TransformerHandler handler) throws SAXException {
        startElement(name, handler);
        handler.startCDATA();
        handler.characters(cdata.toCharArray(), 0, cdata.length());
        handler.endCDATA();
        endElement(name, handler);
    }
    protected void emptyComment(TransformerHandler handler) throws SAXException {
        handler.comment(new char[] { ' ' }, 0, 1);

    }

    private static AttributesImpl ourEmptyAttributes = new AttributesImpl();
}
