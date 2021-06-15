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

package org.geotools.util;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.jxpath.JXPathContext;
import org.jdom.Document;
import org.xml.sax.helpers.NamespaceSupport;

/**
 * Ulities class for xpath handling on a jdom document object
 * 
 * @author Russell Petty (GeoScience Victoria)
 * @version $Id$
 *
 *
 *
 * @source $URL$
 */
public class XmlXpathUtilites {

    /**
     * @param ns namespaces
     * @param xpathString xpath to search on
     * @param doc xml to search
     * @return a list of values matching the xpath in the xml supplied
     */
    public static List<String> getXPathValues(NamespaceSupport ns, String xpathString, Document doc) {
        JXPathContext context = initialiseContext(ns, doc);        
        return getXPathValues(xpathString, context);
    }

    /**
     * @param ns namespaces
     * @param xpathString xpath to search on
     * @param doc xml to search
     * @return count of the values matching the xpath passed in
     */
    public static int countXPathNodes(NamespaceSupport ns, String xpathString, Document doc) {
        int count = 0;
        List<String> ls = getXPathValues(ns, xpathString, doc);
        if (ls != null) {
            count = ls.size();
        }
        return count;
    }
    
    /**
     * 
* @param ns namespaces
     * @param xpathString xpath to search on
     * @param doc xml to search
     * @return the (single) value matching the xpath in the xml supplied
     */
    public static String getSingleXPathValue(NamespaceSupport ns, String xpathString, Document doc) {
        String id = null;
        JXPathContext context = initialiseContext(ns, doc); 
        try {
            Object ob = context.getValue(xpathString); 
            id = (String) ob;
        } catch (RuntimeException e) {
            throw new RuntimeException("Error reading xpath " + xpathString, e);
        }
        return id;
    }
    
    private static JXPathContext initialiseContext(NamespaceSupport ns, Document doc) {
        JXPathContext context = JXPathContext.newContext(doc);
        addNamespaces(ns, context);
        context.setLenient(true);
        return context;
    }

    private static void addNamespaces(NamespaceSupport ns, JXPathContext context) {
        Enumeration<String> prefixes = ns.getPrefixes();
        while (prefixes.hasMoreElements()) {
            String prefix = prefixes.nextElement();
            String uri = ns.getURI(prefix);
            context.registerNamespace(prefix, uri);
        }
    }
    
    /**
     * Remove indexes from an xpath string.
     * @param xpath xpath string
     * @return unindexed xpath string
     */
    public static String removeIndexes(String xpath) {
        final String[] partialSteps = xpath.split("[/]");
        if (partialSteps.length == 0) {
            return xpath;
        }

        int startIndex = 0;
        StringBuffer buf = new StringBuffer();

        for (int i = startIndex; i < partialSteps.length; i++) {
            String step = partialSteps[i];
            int start = step.indexOf('[');

            if (start > -1) {
                int end = step.indexOf(']');
                Scanner scanner = new Scanner(step.substring(start + 1, end));
                if (scanner.hasNextInt()) {
                    // remove index and the brackets
                    step = step.substring(0, start);
                }
            }
            buf.append(step);
            if (i < partialSteps.length - 1) {
                buf.append("/");
            }
        }
        return buf.toString();
    }
        
    private static List<String> getXPathValues(String xpathString, JXPathContext context) {

        List values = null;
        try {
            values = context.selectNodes(xpathString);    
        } catch (RuntimeException e) {
            throw new RuntimeException("Error reading xpath " + xpathString, e);
        }
        
        List<String> ls = null;
        if(values == null) {
            ls = new ArrayList<String>();
        } else {    
            ls = new ArrayList<String>(values.size());
            for (int i = 0; i < values.size(); i++) {
                Object value = values.get(i);
                String unwrappedValue = "";
                if (value instanceof org.jdom.Attribute) {
                    unwrappedValue = ((org.jdom.Attribute) value).getValue();
                } else if (value instanceof org.jdom.Element) {
                    unwrappedValue = ((org.jdom.Element) value).getValue();
                }
                ls.add(unwrappedValue);
            }    
        }

        return ls;
    }
}
