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
package com.gargoylesoftware.htmlunit.javascript.host.xml;

import org.apache.commons.lang.StringEscapeUtils;

import com.gargoylesoftware.htmlunit.javascript.host.Attr;

/**
 * A JavaScript object for an Attribute of XMLElement.
 *
 * @see <a href="http://www.w3.org/TR/2000/REC-DOM-Level-2-Core-20001113/core.html#ID-63764602">W3C DOM Level 2</a>
 * @see <a href="http://msdn.microsoft.com/en-us/library/ms762738.aspx">MSDN documentation</a>
 * @version $Revision: 5301 $
 * @author Sudhan Moghe
 */
public class XMLAttr extends Attr {

    private static final long serialVersionUID = -9062357417620766444L;

    /**
     * Creates an instance. JavaScript objects must have a default constructor.
     */
    public XMLAttr() { }

    /**
     * Returns the text of this attribute.
     * @return the value of this attribute
     */
    public String jsxGet_text() {
        return jsxGet_value();
    }

    /**
     * Sets the text of this attribute.
     * @param value the new value of this attribute
     */
    public void jsxSet_text(final String value) {
        jsxSet_value(value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String jsxGet_xml() {
        final StringBuilder sb = new StringBuilder();
        sb.append(jsxGet_name());
        sb.append('=');
        sb.append('"');
        sb.append(StringEscapeUtils.escapeXml(jsxGet_value()));
        sb.append('"');
        return sb.toString();
    }
}
