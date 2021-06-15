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
 *    (C) 2012, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.referencing.factory;

import org.geotools.factory.Hints;
import org.geotools.metadata.iso.citation.Citations;
import org.opengis.metadata.citation.Citation;
import org.opengis.referencing.AuthorityFactory;
import org.opengis.referencing.NoSuchAuthorityCodeException;

/**
 * {@link AuthorityFactory} for OGC HTTP URI resources. For example, the WGS84 coordinate reference system can be expressed as
 * {@code "http://www.opengis.net/def/crs/EPSG/0/4326"}.
 * 
 * <p>
 * 
 * Use {@link org.opengis.referencing.ReferencingFactoryFinder} instead of instantiating this class directly.
 * 
 * @author Ben Caradoc-Davies (CSIRO Earth Science and Resource Engineering)
 * 
 * @source $URL$
 */
public class HTTP_URI_AuthorityFactory extends Abstract_URI_AuthorityFactory {

    /**
     * The name used in {@link Hints#FORCE_AXIS_ORDER_HONORING} for this factory.
     */
    public static final String HINTS_AUTHORITY = "http-uri";

    /**
     * Constructor.
     * 
     * @see Abstract_URI_AuthorityFactory#Abstract_URI_AuthorityFactory(String)
     */
    public HTTP_URI_AuthorityFactory() {
        super(HINTS_AUTHORITY);
    }

    /**
     * Constructor.
     * 
     * @see Abstract_URI_AuthorityFactory#Abstract_URI_AuthorityFactory(Hints, String)
     */
    public HTTP_URI_AuthorityFactory(Hints userHints) {
        super(userHints, HINTS_AUTHORITY);
    }

    /**
     * Constructor.
     * 
     * @see Abstract_URI_AuthorityFactory#Abstract_URI_AuthorityFactory(AllAuthoritiesFactory)
     */
    public HTTP_URI_AuthorityFactory(AllAuthoritiesFactory factory) {
        super(factory);
    }

    /**
     * @see org.geotools.referencing.factory.AuthorityFactoryAdapter#getAuthority()
     */
    @Override
    public Citation getAuthority() {
        return Citations.HTTP_URI_OGC;
    }

    /**
     * @see org.geotools.referencing.factory.Abstract_URI_AuthorityFactory#buildParser(java.lang.String)
     */
    @Override
    protected URI_Parser buildParser(String code) throws NoSuchAuthorityCodeException {
        return HTTP_URI_Parser.buildParser(code);
    }

}
