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
package org.geotools.referencing.operation.builder;

import org.geotools.geometry.GeneralEnvelope;
import org.geotools.parameter.ParameterGroup;
import org.geotools.referencing.CRS;
import org.geotools.referencing.operation.matrix.GeneralMatrix;
import org.geotools.referencing.operation.transform.AffineTransform2D;
import org.geotools.referencing.operation.transform.ProjectiveTransform;
import org.geotools.referencing.operation.transform.WarpGridTransform2D;
import org.opengis.geometry.Envelope;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

/**
 * Grid parameters are handled here. 
 * @author jezekjan
 * 
 */

class GridParameters {

	final private double xStep;
	final private double yStep;
	final private double xStart;
	final private double yStart;
	final private int xNumber;
	final private int yNumber;	
	
	
	//final private AffineTransform2D trans;
	private float[] warpPositions;

	/**
	 * 
	 */

	private GridParameters(double xstep, double ystep, double xstart,
			double ystart, int xnumber, int ynumber) {
		super();
		xStep = xstep;
		yStep = ystep;
		xStart = xstart;
		yStart = ystart;
		xNumber = xnumber; 
		yNumber = ynumber; 			
		
		
		warpPositions = new float[2 * (xnumber + 1) * (ynumber + 1)];

	}

	private GridParameters(int xstep, int ystep, int xstart, int ystart,
			int xnumber, int ynumber) {
		
		xStep = xstep;
		yStep = ystep;
		xStart = xstart;
		yStart = ystart;
		xNumber = xnumber;
		yNumber = ynumber;		
		warpPositions = new float[2 * (xnumber + 1) * (ynumber + 1)];

	}

	
	public static GridParameters createGridParameters(Envelope env, double dx,
			double dy, AffineTransform2D trans, boolean isInteger)
			throws TransformException {
		
		if (isInteger) {
			if (trans==null) { trans = getIdntityTransform();
			}
			GeneralEnvelope transEnv = CRS.transform(trans, env);

			int iDx = Math.abs((new Double(Math.round(dx * trans.getScaleX())))
					.intValue());
			int iDy =  Math.abs((new Double(Math.round(dy * trans.getScaleY())))
					.intValue());
			int xNum = (new Double(Math.floor(transEnv.getSpan(0) / iDx)))
					.intValue();
			int yNum = (new Double(Math.floor(transEnv.getSpan(1) / iDy)))
					.intValue();
			int xMin = (new Double(Math.floor(transEnv.getMinimum(0)))
					.intValue());
			int yMin = (new Double(Math.floor(transEnv.getMinimum(1)))
					.intValue());

			return new GridParameters(iDx, iDy, xMin, yMin, xNum, yNum);
		} else {
			
			int xNum = (new Double(Math.floor(env.getSpan(0) / dx)))
					.intValue();
			int yNum = (new Double(Math.floor(env.getSpan(1) / dy)))
					.intValue();

			return new GridParameters(dx, dy, env.getMinimum(0), env
					.getMinimum(1), xNum, yNum);
		}
	}

	/*
	 * private static GridParameters createInstance(double xstep, double ystep,
	 * double xstart, double ystart, int xnumber, int ynumber){ return new
	 * GridParameters(xstep, ystep, xstart, ystart, xnumber, ynumber) ; }
	 */
	public double getXStep() {
		return xStep;
	}

	public double getYStep() {
		return yStep;
	}

	public double getXStart() {
		return xStart;
	}

	public double getYStart() {
		return yStart;
	}

	public int getXNumber() {
		return xNumber;
	}

	public int getYNumber() {
		return yNumber;
	}
	
	public void setWarpPositions(float[] positions) {
		this.warpPositions = positions;
	}
	public float[] getWarpPositions() {
		return warpPositions;
	}
	
		
	public ParameterValueGroup getWarpGridParameters() {
		ParameterValueGroup WarpGridParameters = new ParameterGroup(
				new WarpGridTransform2D.Provider().getParameters());
		/**
		 * TODO - throw exception when the values are not integers
		 */
		WarpGridParameters.parameter("xStart").setValue(new Double(this.xStart).intValue());
		WarpGridParameters.parameter("yStart").setValue(new Double(this.yStart).intValue());				
		WarpGridParameters.parameter("xStep").setValue(new Double(this.xStep).intValue());				
		WarpGridParameters.parameter("yStep").setValue(new Double(this.yStep).intValue());				
		WarpGridParameters.parameter("xNumCells").setValue(new Double(xNumber).intValue());	
		WarpGridParameters.parameter("yNumCells").setValue(new Double(yNumber).intValue());				

		WarpGridParameters.parameter("warpPositions").setValue(warpPositions);
				
		return WarpGridParameters;
	}

	private static AffineTransform2D getIdntityTransform() {
		GeneralMatrix M = new GeneralMatrix(3, 3);
		double[] m0 = { 1, 0, 0 };
		double[] m1 = { 0, 1, 0 };
		double[] m2 = { 0, 0, 1 };
		M.setRow(0, m0);
		M.setRow(1, m1);
		M.setRow(2, m2);
		return (AffineTransform2D)ProjectiveTransform.create(M);
	}
}
