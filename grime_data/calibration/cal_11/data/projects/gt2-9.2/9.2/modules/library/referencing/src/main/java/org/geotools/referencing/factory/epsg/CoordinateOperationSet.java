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
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.referencing.factory.epsg;

// J2SE dependencies
import java.util.HashMap;
import java.util.Map;

// OpenGIS dependencies
import org.opengis.referencing.AuthorityFactory;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.IdentifiedObject;
import org.opengis.referencing.crs.CRSAuthorityFactory;
import org.opengis.referencing.operation.CoordinateOperation; // For javadoc
import org.opengis.referencing.operation.CoordinateOperationAuthorityFactory;

// Geotools dependencies
import org.geotools.referencing.factory.IdentifiedObjectSet;


/**
 * A lazy set of {@link CoordinateOperation} objects to be returned by the
 * {@link DirectEpsgFactory#createFromCoordinateReferenceSystemCodes
 * createFromCoordinateReferenceSystemCodes} method. 
 *
 * @since 2.2
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
final class CoordinateOperationSet extends IdentifiedObjectSet {
    /**
     * For compatibility with previous versions.
     */
    private static final long serialVersionUID = -2421669857023064667L;
    
    /**
     * The codes of {@link ProjectedCRS} objects for the specified {@link Conversion} codes,
     * or {@code null} if none.
     */
    private Map/*<String,String>*/ projections;

    /**
     * Creates a new instance of this lazy set.
     */
    public CoordinateOperationSet(final AuthorityFactory factory) {
        super(factory);
    }

    /**
     * Add the specified authority code.
     *
     * @param code The code for the {@link CoordinateOperation} to add.
     * @param crs  The code for the CRS is create instead of the operation,
     *             or {@code null} if none.
     */
    public boolean addAuthorityCode(final String code, final String crs) {
        if (crs != null) {
            if (projections == null) {
                projections = new HashMap();
            }
            projections.put(code, crs);
        }
        return super.addAuthorityCode(code);
    }

    /**
     * Creates an object for the specified code.
     */
    protected IdentifiedObject createObject(final String code) throws FactoryException {
        if (projections != null) {
            final String crs = (String) projections.get(code);
            if (crs != null) {
                return ((CRSAuthorityFactory) factory).createProjectedCRS(crs).getConversionFromBase();
            }
        }
        return ((CoordinateOperationAuthorityFactory) factory).createCoordinateOperation(code);
    }
}
