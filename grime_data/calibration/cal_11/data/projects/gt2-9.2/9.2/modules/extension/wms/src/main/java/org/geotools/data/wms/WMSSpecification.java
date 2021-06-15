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
package org.geotools.data.wms;

import java.net.URL;

import org.geotools.data.ows.Specification;
import org.geotools.data.wms.request.DescribeLayerRequest;
import org.geotools.data.wms.request.GetFeatureInfoRequest;
import org.geotools.data.wms.request.GetLegendGraphicRequest;
import org.geotools.data.wms.request.GetMapRequest;
import org.geotools.data.wms.request.GetStylesRequest;
import org.geotools.data.wms.request.PutStylesRequest;

/**
 * 
 *
 * @source $URL$
 */
public abstract class WMSSpecification extends Specification {

    /**
     * Creates a GetMapRequest for this specification, populating it with valid
     * values.
     * 
     * @param onlineResource the URL for the GetMapRequest
     * @return a GetMapRequest that can be configured and used
     */
    public abstract GetMapRequest createGetMapRequest( URL onlineResource );

    /**
     * Creates a GetFeatureInfoRequest for this specification, populating it 
     * with valid values.
     * 
     * @param onlineResource the URL to be executed against
     * @param getMapRequest a previously configured GetMapRequest
     * @return a GetFeatureInfoRequest that can be configured and used
     */
    public abstract GetFeatureInfoRequest createGetFeatureInfoRequest( URL onlineResource, GetMapRequest getMapRequest);
    
    /**
     * Creates a DescribeLayer request which can be used to retrieve
     * information about specific layers on the Web Map Server.
     * 
     * @param onlineResource the location where the request can be made
     * @return a DescribeLayerRequest to be configured and then passed to the Web Map Server
     * @throws UnsupportedOperationException if the version of the specification doesn't support this request
     */
    public abstract DescribeLayerRequest createDescribeLayerRequest( URL onlineResource ) throws UnsupportedOperationException;

    /**
     * Creates a GetLegendGraphicRequest which can be used to retrieve legend
     * graphics from the WebMapServer
     * 
     * @param onlineResource the location where the request can be made
     * @return a GetLegendGraphicRequest to be configured and passed to the WMS
     * @throws UnsupportedOperationException if the version of the specification doesn't support this request
     */
    public abstract GetLegendGraphicRequest createGetLegendGraphicRequest(URL onlineResource) throws UnsupportedOperationException;
    
    
    /**
     * Creates a GetStylesRequest which can be used to retrieve styles from
     * the WMS.
     * 
     * @param onlineResource The location where the request can be made
     * @return a configurable request object to be passed to a WMS
     * @throws UnsupportedOperationException if the version of the specification doesn't support this request
     */
    public abstract GetStylesRequest createGetStylesRequest(URL onlineResource) throws UnsupportedOperationException;
    
    /**
     * Creates a PutStyles request which can be configured and the passed to 
     * the WMS to store styles for later use.
     * 
     * @param onlineResource the location where the request can be made
     * @return a configureable request object to be passed to the WMS
     * @throws UnsupportedOperationException if the version of the specification doesn't support this request
     */
    public abstract PutStylesRequest createPutStylesRequest(URL onlineResource) throws UnsupportedOperationException;
}
