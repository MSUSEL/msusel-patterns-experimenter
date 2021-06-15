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
package org.geotools.data.wps;

import java.net.URL;
import java.util.List;

import net.opengis.wps10.DataType;
import net.opengis.wps10.OutputDefinitionType;
import net.opengis.wps10.ResponseDocumentType;
import net.opengis.wps10.ResponseFormType;

import org.geotools.data.ows.Specification;
import org.geotools.data.wps.request.DescribeProcessRequest;
import org.geotools.data.wps.request.ExecuteProcessRequest;


/**
 *
 *
 * @source $URL$
 */
public abstract class WPSSpecification extends Specification
{

    /**
     * Creates a DescribeProcess request which can be used to retrieve
     * information about a specific process on the WPS Server.
     *
     * @param onlineResource the location where the request can be made
     * @return a DescribeProcessRequest to be configured and then passed to the WPS Server
     * @throws UnsupportedOperationException if the version of the specification doesn't support this request
     */
    public abstract DescribeProcessRequest createDescribeProcessRequest(URL onlineResource)
        throws UnsupportedOperationException;

    /**
     * Creates a Execute request which can be used to execute
     * a specific process on the WPS Server.
     *
     * @param onlineResource the location where the request can be made
     * @return an ExecuteProcessRequest to be configured and then passed to the WPS Server
     * @throws UnsupportedOperationException if the version of the specification doesn't support this request
     */
    public abstract ExecuteProcessRequest createExecuteProcessRequest(URL onlineResource)
        throws UnsupportedOperationException;

    public abstract DataType createLiteralInputValue(String literalValue);

    public abstract DataType createBoundingBoxInputValue(String crs, int dimensions, List<Double> lowerCorner,
        List<Double> upperCorner);

    public abstract ResponseFormType createResponseForm(ResponseDocumentType responseDoc,
        OutputDefinitionType rawOutput);

    public abstract ResponseDocumentType createResponseDocumentType(boolean lineage, boolean status,
        boolean storeExecuteResponse, String outputType);

    public abstract OutputDefinitionType createOutputDefinitionType(String identifier);

}
