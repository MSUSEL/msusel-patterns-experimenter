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
package org.geotools.xml.schema.impl;

import java.net.URI;

import org.geotools.xml.schema.All;
import org.geotools.xml.schema.Element;


/**
 *
 *
 *
 * @source $URL$
 */
public class AllGT implements All {

    private Element[] elements;
    private String id;
    private int max;
    private int min;

    public AllGT( Element[] elements ) {
        this.elements = elements;
        max = min = 1;
    }

    public AllGT( String id, Element[] elements, int min, int max ) {
        this.id = id;
        this.elements = elements;
        this.min = min;
        this.max = max;
    }
    /* (non-Javadoc)
     * @see org.geotools.xml.schema.All#getElements()
     */
    public Element[] getElements() {
        return elements;
    }

    /* (non-Javadoc)
     * @see org.geotools.xml.schema.All#getId()
     */
    public String getId() {
        return id;
    }

    /* (non-Javadoc)
     * @see org.geotools.xml.schema.ElementGrouping#getMaxOccurs()
     */
    public int getMaxOccurs() {
        return max;
    }

    /* (non-Javadoc)
     * @see org.geotools.xml.schema.ElementGrouping#getMinOccurs()
     */
    public int getMinOccurs() {
        return min;
    }

    /* (non-Javadoc)
     * @see org.geotools.xml.schema.ElementGrouping#getGrouping()
     */
    public int getGrouping() {
        return ALL;
    }

    /* (non-Javadoc)
     * @see org.geotools.xml.schema.ElementGrouping#findChildElement(java.lang.String)
     */
    public Element findChildElement( String name ) {
        if (elements != null) {
            for (int i = 0; i < elements.length; i++) {
                Element e = elements[i].findChildElement(name);

                if (e != null) {
                    return e;
                }
            }
        }

        return null;
    }

	public Element findChildElement(String localName, URI namespaceURI) {
		if (elements != null) {
            for (int i = 0; i < elements.length; i++) {
                Element e = elements[i].findChildElement(localName, namespaceURI);

                if (e != null) {
                    return e;
                }
            }
        }

        return null;
	}

}
