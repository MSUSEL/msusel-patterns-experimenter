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
package org.geotools.data.wps.request;

import java.net.URL;
import java.util.Properties;


/**
 * Describes an abstract DescribeProcess request. Provides everything except
 * the versioning info, which subclasses must implement.
 * 
 * @author gdavis
 *
 *
 *
 *
 * @source $URL$
 */
public abstract class AbstractDescribeProcessRequest extends AbstractWPSRequest implements DescribeProcessRequest {

    /**
     * Constructs a basic DescribeProcessRequest, without versioning info.
     * 
     * @param onlineResource the location of the request
     * @param properties a set of properties to use. Can be null.
     */
    public AbstractDescribeProcessRequest( URL onlineResource, Properties properties ) {
        super(onlineResource, properties);
    }
    
    protected void initRequest() {
        setProperty(REQUEST, "DescribeProcess");
	}

	/**
     * @see org.geotools.data.wps.request.DescribeProcessRequest#setIdentifier(java.lang.String)
     */
    public void setIdentifier( String identifiers ) {
        setProperty(IDENTIFIER, identifiers);
    }

    protected abstract void initVersion();
}
