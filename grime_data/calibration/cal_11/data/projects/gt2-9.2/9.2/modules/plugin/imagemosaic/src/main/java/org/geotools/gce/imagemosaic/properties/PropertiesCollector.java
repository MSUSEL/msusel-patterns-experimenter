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
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gce.imagemosaic.properties;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.imageio.ImageReader;

import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.opengis.feature.simple.SimpleFeature;


/**
 * 
 *
 * @source $URL$
 */
public abstract class PropertiesCollector  {
	
	private List<String> propertyNames;
	private PropertiesCollectorSPI spi;
	private List<String> matches= new ArrayList<String>();
	
	
	public PropertiesCollector(
			final PropertiesCollectorSPI spi,
			final List<String> propertyNames) {
		this.spi = spi;
		this.propertyNames=new ArrayList<String>(propertyNames);
	}
	

	public PropertiesCollectorSPI getSpi() {
		return spi;
	}

	public PropertiesCollector collect(final File  file){
		return this;
	}
	
	public PropertiesCollector collect(final ImageReader  imageReader){
		return this;
	}
	
	public PropertiesCollector collect(final AbstractGridCoverage2DReader  abstractGridCoverageReader){
		return this;
	}		
	
	abstract public void setProperties(final SimpleFeature feature);
	
	public void reset(){
		matches= new ArrayList<String>();
	}

	public List<String> getPropertyNames() {
		return Collections.unmodifiableList(propertyNames);
	}
	
	protected void addMatch(String match){
		matches.add(match);
	}

	protected List<String> getMatches() {
		return Collections.unmodifiableList(matches);
	}
	
}
