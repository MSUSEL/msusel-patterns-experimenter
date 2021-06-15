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
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.geometry.jts.spatialschema;

import java.util.List;

import org.geotools.geometry.jts.spatialschema.geometry.DirectPositionImpl;
import org.geotools.geometry.jts.spatialschema.geometry.geometry.PointArrayImpl;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.geometry.PositionFactory;
import org.opengis.geometry.Precision;
import org.opengis.geometry.coordinate.PointArray;
import org.opengis.geometry.coordinate.Position;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * 
 *
 * @source $URL$
 */
public class PositionFactoryImpl implements PositionFactory {
	private CoordinateReferenceSystem crs;
	
	/**
	 * No argument constructor for the plugin system.
	 */
	public PositionFactoryImpl(){
	    this( DefaultGeographicCRS.WGS84);
	}
	public PositionFactoryImpl( CoordinateReferenceSystem crs ){
		this.crs = crs;
	}
	public DirectPosition createDirectPosition(double[] ordiantes)
			throws MismatchedDimensionException {
		return new DirectPositionImpl( crs, ordiantes );
	}

	public Position createPosition(Position position) {
		return new DirectPositionImpl( position.getDirectPosition() );
	}

	public List createPositionList() {
		return new PointArrayImpl( crs );
	}

	public List createPositionList(double[] coordinates, int start, int end) {
		PointArray array = new PointArrayImpl( crs );
		int N = crs.getCoordinateSystem().getDimension();
		for( int i=start; i < end ; i += N ){
			double[] ords = new double[N];
			System.arraycopy( coordinates, i, ords, 0, N );
			array.add( createDirectPosition( ords ));			
		}
		return array;
	}

	public List createPositionList(float[] coordinates, int start, int end) {
		PointArray array = new PointArrayImpl( crs );
		int N = crs.getCoordinateSystem().getDimension();
		for( int i=start; i < end ; i += N ){
			double[] ords = new double[N];
			System.arraycopy( coordinates, i, ords, 0, N );
			array.add( createDirectPosition( ords ));			
		}
		return array;
	}

	public CoordinateReferenceSystem getCoordinateReferenceSystem() {
		return crs;
	}

	public Precision getPrecision() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public PointArray createPointArray(float[] array, int start, int end) {
		PointArray pointArray = (PointArray) createPointArray();
		int D = crs.getCoordinateSystem().getDimension();
		if (D == 2) {
			for (int i = start; i < end; i += D) {
				double[] ordinates = new double[] { array[i], array[i + 1] };
				pointArray.add(new DirectPositionImpl(crs, ordinates));
			}
		} else if (D == 3) {
			for (int i = start; i < end; i += D) {
				double[] ordinates = new double[] { array[i], array[i + 1],
						array[i + 2] };
				pointArray.add(new DirectPositionImpl(crs, ordinates));
			}
		} else {
			for (int i = start; i < end; i += D) {
				double[] ordinates = new double[D];
				for (int o = 0; i < D; i++) {
					ordinates[o] = array[i + o];
				}
				pointArray.add(new DirectPositionImpl(crs, ordinates));
			}
		}
		return pointArray;
	}
	
	public PointArray createPointArray(final double[] array,
			final int start, final int end) {
		PointArray pointArray = (PointArray) createPointArray();
		int n = crs.getCoordinateSystem().getDimension();
		double[] ordinates = new double[n]; 
		for (int i=start; i<array.length && i <= end; i += n) {
		    for ( int j = i; j < i + n; j++ ) {
		        ordinates[j-i] = array[j]; 
		    }
		    
			pointArray.add(createDirectPosition(ordinates));
		}
		return pointArray;
	}
	public PointArray createPointArray() {
		return new PointArrayImpl(crs);
	}

}
