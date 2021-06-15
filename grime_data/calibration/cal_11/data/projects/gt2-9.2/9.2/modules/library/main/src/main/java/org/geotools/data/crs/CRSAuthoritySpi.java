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
package org.geotools.data.crs;

import java.io.IOException;
import java.util.Set;

import org.geotools.factory.Factory;
import org.opengis.referencing.crs.CRSAuthorityFactory;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * This is a marker used to allow CRSService to dynamically locate
 * implementations of CoordinateSystemAuthorityFactory.
 * <p>
 * When the time comes CRSService can switch over to
 * org.geotools.referencing.Factory - that time is not now.
 * </p>
 * @author Jody Garnett
 *
 * @deprecated Now replaced by {@link CRSAuthorityFactory}.
 *
 *
 * @source $URL$
 */
public interface CRSAuthoritySpi extends Factory {
    /**
     * Provides the complete set of codes managed by this CRSAuthority.
     * <p>
     * Note these codes are will include any authority information such
     * as EPSG or AUTO.
     * </p>
     * 
     * @return Set of codes used to identify CRS provided by this Factory.
     */
    public Set getCodes();
    
    /**
     * Provide access to the low-level decoding system provided by this Factory.
     * <p>
     * This method is provided to allow access to parser (WKT/XML/?) used by
     * this Authority in a seemless manner.
     * </p>
     * <p>
     * What purpose this servers is unclear to me since the EPSG code will not be
     * known for the resulting CoordinateReferenceSystem.
     * </p>
     * 
     * @param encoding
     * @return CoordinateReferenceSystem for the encoding or null if encoding was unsuitable
     * @throws IOException If the encoding should of been parsable but contained an error
     *
     * @deprecated Replaced by {@link CRSAuthorityFactory#createCoordinateReferenceSystem}.
     */
    public CoordinateReferenceSystem decode( String encoding ) throws IOException;
    
}
