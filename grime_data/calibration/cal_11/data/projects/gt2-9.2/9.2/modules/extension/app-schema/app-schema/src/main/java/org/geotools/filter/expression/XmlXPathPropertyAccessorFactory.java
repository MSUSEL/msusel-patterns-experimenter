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

package org.geotools.filter.expression;

import java.util.List;

import org.geotools.data.complex.xml.XmlXpathFilterData;
import org.geotools.factory.Hints;
import org.geotools.feature.IllegalAttributeException;
import org.geotools.util.XmlXpathUtilites;
import org.opengis.feature.Feature;
import org.xml.sax.helpers.NamespaceSupport;

/**
 * PropertyAccessorFactory used to create property accessors which can handle xpath expressions
 * against instances of {@link Feature}.
 * 
 * @author Russell Petty (GeoScience Victoria)
 * @author Rini Angreani (CSIRO Earth Science and Resource Engineering) 
 *
 * @source $URL$
 */
public class XmlXPathPropertyAccessorFactory implements PropertyAccessorFactory {
    /**
     * Namespace support hint
     */
    public static Hints.Key NAMESPACE_SUPPORT = new Hints.Key(NamespaceSupport.class);

    public PropertyAccessor createPropertyAccessor(Class type, String xpath, Class target,
            Hints hints) {
        if (XmlXpathFilterData.class.isAssignableFrom(type)) {
            return new XmlXPathPropertyAcessor();
        }

        return null;
    }

    static class XmlXPathPropertyAcessor implements PropertyAccessor {
        public boolean canHandle(Object object, String xpath, Class target) {
            // TODO: some better check for a valid xpath expression
            return (xpath != null) && !"".equals(xpath.trim());
        }

        public Object get(Object object, String xpath, Class target) {

            XmlXpathFilterData xmlResponse = (XmlXpathFilterData) object;
            String indexXpath = createIndexedXpath(xmlResponse, xpath);

            List<String> ls = XmlXpathUtilites.getXPathValues(xmlResponse.getNamespaces(),
                    indexXpath, xmlResponse.getDoc());
            if (ls != null && !ls.isEmpty()) {
                return ls.get(0);
            }
            return null;
        }

        public void set(Object object, String xpath, Object value, Class target)
                throws IllegalAttributeException {
            throw new UnsupportedOperationException("Do not support updating.");
            // context(object).setValue(xpath, value);
        }

        private String createIndexedXpath(XmlXpathFilterData xmlResponse, String xpathString) {

            String itemXpath = xmlResponse.getItemXpath();
            // if xpathString is from mapping file, as a function expression or inputattribute
            // it wouldn't be indexed
            // however the itemXpath would be indexed as it comes from the node
            // so need to remove the indexes when doing a search
            String unindexedXpath = XmlXpathUtilites.removeIndexes(itemXpath);
            int position = xpathString.indexOf(unindexedXpath);
            if (position != 0) {
                throw new RuntimeException("xpath passed in does not begin with itemXpath"
                        + "/n xpathString =" + xpathString + "/n itemXpath =" + itemXpath);
            }

            StringBuffer sb = new StringBuffer(itemXpath);
            int count = xmlResponse.getCount();
            if (count > -1) {
                    sb.append("[" + xmlResponse.getCount() + "]");
            }
            sb.append(xpathString.substring(unindexedXpath.length()));
            return sb.toString();
        }
    }
}
