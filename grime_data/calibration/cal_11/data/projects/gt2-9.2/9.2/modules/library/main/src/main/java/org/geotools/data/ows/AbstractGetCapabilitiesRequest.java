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
package org.geotools.data.ows;

import java.net.URL;

/**
 * Each Open Web Service typically defines an operation that describes what 
 * operations it supports and what data it holds. The document describing this
 * information is usually called the Capabilities document, and is usually
 * accessed using the GetCapabilities operation.
 * 
 * This class provides a basic building block for clients to implement their
 * GetCapabilitiesRequest. It automatically sets the REQUEST parameter to
 * "GetCapabilities".
 *
 * @author rgould
 *
 *
 * @source $URL$
 */
public abstract class AbstractGetCapabilitiesRequest extends AbstractRequest implements GetCapabilitiesRequest{
    /** Represents the SERVICE parameter */
    public static final String SERVICE = "SERVICE"; //$NON-NLS-1$

    public AbstractGetCapabilitiesRequest(URL serverURL) {
        super(serverURL, null);
    }

    /**
     * Sets the REQUEST parameter
     * <p>
     * Subclass can override if needed.
     * </p>
     */
    protected void initRequest() {
        setProperty(REQUEST, "GetCapabilities"); //$NON-NLS-1$
    }
}
