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
 *    (C) 2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverage.io.range;

import java.util.Set;

import org.opengis.feature.type.Name;
import org.opengis.util.InternationalString;

/**
 * Defines the fields (categories, measures, or values) in the range records 
 * available for each location in the coverage domain. Each such field may be 
 * a scalar (numeric or text) value, such as population density, or a vector 
 * (compound or tensor) of many similar values, such as incomes by race, or 
 * radiances by wavelength. 
 * 
 * @author Simone Giannecchini, GeoSolutions
 * 
 *
 *
 *
 * @source $URL$
 */
public interface RangeType {
	/**
	 * Retrieves this {@link RangeType} {@link org.opengis.feature.type.Name}.
	 * 
	 * @return this {@link RangeType} {@link org.opengis.feature.type.Name}.
	 */
	public Name getName();

	/**
	 * Retrieves this {@link RangeType} description as {@link InternationalString}.
	 * 
	 * @return this {@link RangeType} description as {@link InternationalString}.
	 */
	public InternationalString getDescription();

	/**
	 * Get the {@link FieldType} {@link org.opengis.feature.type.Name}s.
	 * 
	 * @return List of {@link FieldType} names
	 */
	public Set<Name> getFieldTypeNames();

	/**
	 * Get the Number of FieldTypes in the range
	 * 
	 * @return number of measure types
	 */
	public int getNumFieldTypes();

	/**
	 * Get all the measure types of this Coverage type
	 * 
	 * @return Set of FieldType instances
	 */
	public Set<FieldType> getFieldTypes();

	/**
	 * Get the FieldType by name
	 * 
	 * @param name
	 *            name of the FieldType in the form "nameSpace:localPart".
	 *            In case of no nameSpace, the simple "localPart" section
	 *            need to be provided
	 * @return FieldType instance or null if not found
	 */
	public FieldType getFieldType(String name);
}
