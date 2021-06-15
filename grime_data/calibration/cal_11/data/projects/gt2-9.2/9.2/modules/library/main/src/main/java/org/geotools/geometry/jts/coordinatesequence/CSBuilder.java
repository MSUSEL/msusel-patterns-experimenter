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
 *
 *    Created on 31-dic-2004
 */
package org.geotools.geometry.jts.coordinatesequence;

import com.vividsolutions.jts.geom.CoordinateSequence;

/**
 * A Builder for JTS CoordinateSequences.
 * 
 * @author wolf
 *
 *
 *
 * @source $URL$
 */
public interface CSBuilder {
	/**
	 * Starts the building of a new coordinate sequence 
	 * @param size - the number of coordinates in the coordinate sequence
	 * @param dimensions - the dimension of the coordinates in the coordinate sequence,
	 * may be ignored if the coordinate sequence does not support variabile dimensions
	 */
	public void start(int size, int dimensions);
	
	/**
	 * Stops the coordinate sequence building and returns the result
	 */
	public CoordinateSequence end();

	
	/**
	 * Sets and ordinate in the specified coordinate
	 * @param value
	 * @param ordinateIndex
	 * @param coordinateIndex
	 */
	public void setOrdinate(double value, int ordinateIndex, int coordinateIndex);
	
	/**
	 * Utility method that allows to set an ordinate in an already built coordinate sequence
	 * Needed because the CoordinateSequence interface does not expose it
	 * @param sequence
	 * @param value
	 * @param ordinateIndex
	 * @param coordinateIndex
	 */
	public void setOrdinate(CoordinateSequence sequence, double value, int ordinateIndex, int coordinateIndex);
	
	/**
	 * Gets an ordinate in the specified coordinate
	 * 
	 * @param ordinateIndex
	 * @param coordinateIndex
	 */
	public double getOrdinate(int ordinateIndex, int coordinateIndex);
	
	/**
	 * Returns the size of the coordinate sequence we are building, -1 if there is none
	 */
	public int getSize();
	
	/**
	 * Returns the dimension of the coordinate sequence we are building, -1 if there is none
	 */
	public int getDimension();
}


