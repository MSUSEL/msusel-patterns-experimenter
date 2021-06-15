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
package org.geotools.filter;

/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2009, Open Source Geospatial Foundation (OSGeo)
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

import org.geotools.data.complex.XmlMappingFeatureIterator;
import org.geotools.data.complex.xml.XmlXpathFilterData;
import org.geotools.filter.FunctionExpressionImpl;
import org.geotools.filter.capability.FunctionNameImpl;
import org.geotools.util.XmlXpathUtilites;
import org.jdom.Document;
import org.opengis.filter.capability.FunctionName;
import org.xml.sax.helpers.NamespaceSupport;

/**
 * This is used for app-schema with web-service backend, where a function could have an xpath to the
 * web service as a parameter. E.g. strConcat('gsml.', gss:geologicEvent/@id). The expression would
 * throw a CQLParser exception, as it doesn't allow for special characters. This function is to wrap
 * the xpath expression, evaluate against the document and return the value. E.g. strConcat('gsml.',
 * asXpath('gss:geologicEvent/@id'))
 * <p>
 * This function expects:
 * <ol>
 * <li>Expression: an xpath to underlying web service
 * </ol>
 * 
 * @author Rini Angreani (CSIRO Earth Science and Resource Engineering)
 * 
 *
 * @source $URL$
 *         http://svn.osgeo.org/geotools/trunk/modules/unsupported/app-schema/webservice/src/main
 *         /java/org/geotools/filter/AsXpathFunctionExpression.java $
 */
public class AsXpathFunctionExpression extends FunctionExpressionImpl {
    /**
     * Make the instance of FunctionName available in a consistent spot.
     */
    public static final FunctionName NAME = new FunctionNameImpl("asXpath", "XPATH");
    
    public AsXpathFunctionExpression() {
        super(NAME.getName());
        functionName = NAME;
    }

    public Object evaluate(Object object) {
        if (object == null || !(object instanceof XmlXpathFilterData)) {
            return null;
        }
        XmlXpathFilterData data = (XmlXpathFilterData) object;
        Document doc = data.getDoc();
        String xpath = data.getItemXpath();
        NamespaceSupport ns = data.getNamespaces();

        // append the parameter from AsXpath() to the prefix
        xpath += XmlMappingFeatureIterator.XPATH_SEPARATOR
                + (params.get(0) == null ? "" : params.get(0).toString());
        // then evaluate xpath from the xmlResponse
        return XmlXpathUtilites.getSingleXPathValue(ns, xpath, doc);
    }

    @Override
    public int getArgCount() {
        // TODO Auto-generated method stub
        return 1;
    }
}
