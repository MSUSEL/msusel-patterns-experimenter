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
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.feature.type;

import java.util.List;

import org.opengis.feature.type.AttributeType;
import org.opengis.feature.type.GeometryType;
import org.opengis.feature.type.Name;
import org.opengis.filter.Filter;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.util.InternationalString;

/**
 * AttributeType for hold geometry implementations, maintains CRS information.
 *
 *
 *
 * @source $URL$
 */
public class GeometryTypeImpl extends AttributeTypeImpl implements GeometryType {

	protected CoordinateReferenceSystem CRS;

	public GeometryTypeImpl(
		Name name, Class binding, CoordinateReferenceSystem crs, 
		boolean identified, boolean isAbstract, List<Filter> restrictions, 
		AttributeType superType, InternationalString description
	) {
		super(name, binding, identified, isAbstract, restrictions, superType, description);
		CRS = crs;
	}

	public CoordinateReferenceSystem getCoordinateReferenceSystem() {
		return CRS;
	}

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof GeometryType)) {
            return false;
        }
        if (!super.equals(other)) {
            return false;
        }
        GeometryType o = (GeometryType) other;
        if (CRS == null) {
            return o.getCoordinateReferenceSystem() == null;
        }
        if (o.getCoordinateReferenceSystem() == null) {
            return false;
        }
        return org.geotools.referencing.CRS.equalsIgnoreMetadata(CRS,
                o.getCoordinateReferenceSystem());
    }

}
