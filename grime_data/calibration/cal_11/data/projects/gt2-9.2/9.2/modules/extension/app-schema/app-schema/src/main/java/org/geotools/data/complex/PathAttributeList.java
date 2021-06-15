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
 *    (C) 2009-2011, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.data.complex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.opengis.feature.Attribute;

/**
 * @author Russell Petty (GeoScience Victoria)
 * @version $Id$
 *
 *
 *
 * @source $URL$
 */
public class PathAttributeList {
   
private Map<String, List<Pair>> elements = new HashMap<String, List<Pair>>();

private Map<String, String> labelToXpath = new HashMap<String, String>();

    /**
     * Store information for labelled attributes.
     * 
     * @param key
     *            AttributeMapping label
     * @param xpath
     *            full input xpath from web service including itemXpath + instanceXpath
     * @param attribute
     *            Attribute instance that is created for the AttributeMapping
     */
    public void put(String key, String xpath, Attribute attribute) {
        List<Pair> ls = null;
        if (elements.containsKey(key)) {
            ls = elements.get(key);
        } else {
            ls = new ArrayList<Pair>();
            elements.put(key, ls);
            labelToXpath.put(key, xpath);
        }
        ls.add(new Pair(xpath, attribute));
    }
    
    /**
     * Get full input xpath based on the label.
     * 
     * @param label
     *            AttributeMapping label
     * @return full input xpath from web service including itemXpath + instanceXpath
     */
    public String getPath(String label) {
        return labelToXpath.get(label);
    }

    /**
     * Return list of matching source input xpath - Attribute pair based on the label.
     * 
     * @param key
     *            The attribute label
     * @return full input xpath - Attribute pair
     */
    public List<Pair> get(String key) {
        return elements.get(key);
    }

public class Pair {
    private String xpath;
    private Attribute attribute;

    public Pair(String xpath, Attribute attribute) {
        this.xpath = xpath;
        this.attribute = attribute;
    }

    public String getXpath() {
        return xpath;
    }

    public Attribute getAttribute() {
        return attribute;
    }
    
}
}
