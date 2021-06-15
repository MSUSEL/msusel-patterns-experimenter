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
/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
 *    
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.xml.handlers.xsi;

import org.geotools.xml.XSIElementHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;


/**
 * FacetHandler purpose.
 * 
 * <p>
 * Abstract class representing common Facet abilites + attributes.
 * </p>
 *
 * @author dzwiers, Refractions Research, Inc. http://www.refractions.net
 * @author $Author:$ (last modification)
 *
 *
 * @source $URL$
 * @version $Id$
 */
public abstract class FacetHandler extends XSIElementHandler {
    //    /** ENUMERATION  */
    //    public static final int ENUMERATION = 1;
    //
    //    /** FRACTIONDIGITS  */
    //    public static final int FRACTIONDIGITS = 2;
    //
    //    /** LENGTH  */
    //    public static final int LENGTH = 4;
    //
    //    /** MAXEXCLUSIVE  */
    //    public static final int MAXEXCLUSIVE = 8;
    //
    //    /** MAXINCLUSIVE  */
    //    public static final int MAXINCLUSIVE = 16;
    //
    //    /** MAXLENGTH  */
    //    public static final int MAXLENGTH = 32;
    //
    //    /** MINEXCLUSIVE  */
    //    public static final int MINEXCLUSIVE = 64;
    //
    //    /** MININCLUSIVE  */
    //    public static final int MININCLUSIVE = 128;
    //
    //    /** MINLENGTH  */
    //    public static final int MINLENGTH = 264;
    //
    //    /** PATTERN  */
    //    public static final int PATTERN = 512;
    //
    //    /** TOTALDIGITS  */
    //    public static final int TOTALDIGITS = 1024;
    private String value;

    /**
     * @see org.geotools.xml.XSIElementHandler#endElement(java.lang.String,
     *      java.lang.String)
     */
    public void endElement(String namespaceURI, String localName){
        // do nothing
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return getHandlerType() * ((value == null) ? 1 : value.hashCode());
    }

    /**
     * <p>
     * Return the int mask for the facet type.
     * </p>
     *
     */
    public abstract int getType();

    /**
     * @see org.geotools.xml.XSIElementHandler#getHandlerType()
     */
    public int getHandlerType() {
        return FACET;
    }

    /**
     * @see org.geotools.xml.XSIElementHandler#getHandler(java.lang.String,
     *      java.lang.String)
     */
    public XSIElementHandler getHandler(String namespaceURI, String localName)
        throws SAXException {
    	if (localName.equalsIgnoreCase("annotation") || localName.equalsIgnoreCase("documentation")) {
    		return new IgnoreHandler();
    	}
        throw new SAXNotRecognizedException(
            "Facets are not allowed to have sub-elements");
    }

    /**
     * @see org.geotools.xml.XSIElementHandler#startElement(java.lang.String,
     *      java.lang.String, org.xml.sax.Attributes)
     */
    public void startElement(String namespaceURI, String localName,
        Attributes atts){
        value = atts.getValue("", "value");

        if (value == null) {
            value = atts.getValue(namespaceURI, "value");
        }
    }

    /**
     * <p>
     * Returns the Facet Value
     * </p>
     *
     */
    public String getValue() {
        return value;
    }
}
