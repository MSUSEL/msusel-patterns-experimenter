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
package org.geotools.coverage.io.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.geotools.coverage.io.CoverageStore;
import org.opengis.coverage.Coverage;

/**
 * @author   Simone Giannecchini, GeoSolutions
 * @todo revisit and improve when feedback starts to flow in
 * @todo inherit {@link CoverageReadRequest} and add a method to obtain the capabilities 
 * for a {@link CoverageStore}
 *
 *
 *
 * @source $URL$
 */
public class CoverageUpdateRequest extends CoverageRequest  {

	/**
	 * @uml.property  name="data"
	 */
	private Collection<? extends Coverage> data;
	
	/**
	 * @uml.property  name="metadata"
	 */
	private Map<String, String> metadata;
	
	

	/**
	 * @see org.geotools.coverage.io.CoverageUpdateRequest#getMetadataNames()
	 */
	public java.lang.String[] getMetadataNames() throws java.io.IOException {
		return null;
	}

	/**
	 * @see org.geotools.coverage.io.CoverageUpdateRequest#getMetadataValue(java.lang.String)
	 */
	public java.lang.String getMetadataValue(java.lang.String arg0)
			throws java.io.IOException {
		return arg0;
	}

	/**
	 * @see org.geotools.coverage.io.CoverageUpdateRequest#setMetadata(java.util.Map)
	 */
	public void setMetadata(Map<String, String> metadata)
			throws java.io.IOException {
		this.metadata=new HashMap<String, String>(metadata);
	}

	/**
	 * @see org.geotools.coverage.io.CoverageUpdateRequest#getMetadata()
	 */
	public Map<String, String> getMetadata() throws java.io.IOException {
		return new HashMap<String, String>(this.metadata);
	}
	
	/**
	 * @see org.geotools.coverage.io.CoverageUpdateRequest#setData(java.util.Collection)
	 */
	public void setData(Collection<? extends Coverage> data) {
		this.data = data;
	}

	/**
	 * @see org.geotools.coverage.io.CoverageUpdateRequest#getData()
	 */
	public Collection<? extends Coverage> getData() {
		return data;
	}

}
