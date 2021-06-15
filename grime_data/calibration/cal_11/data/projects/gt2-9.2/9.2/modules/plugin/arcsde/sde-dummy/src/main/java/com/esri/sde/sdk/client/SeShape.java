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
package com.esri.sde.sdk.client;

/**
 * 
 *
 * @source $URL$
 */
public class SeShape {
	
	public static int TYPE_NIL = -1;
	public static int TYPE_POINT = 0;
	public static int TYPE_MULTI_POINT = 1;
	public static int TYPE_LINE = 2;
	public static int TYPE_SIMPLE_LINE = 3;
	public static int TYPE_MULTI_LINE = 4;
	public static int TYPE_MULTI_SIMPLE_LINE = 5;
	public static int TYPE_POLYGON = 6;
	public static int TYPE_MULTI_POLYGON = 7;
	
	public SeShape(){}
	public SeShape(SeCoordinateReference s) throws SeException {}
	
	public boolean isNil() { return false; }
	public double[][][] getAllCoords() { return null; }
	public void generatePoint(int i, SDEPoint[] p) throws SeException {}
	public void generateLine(int i, int j, int[] k, SDEPoint[] p) throws SeException {}
	public void generatePolygon(int i, int j, int[] k, SDEPoint[] p) throws SeException {}
	public void generateRectangle(SeExtent x) throws SeException {}
	public int getType() { return 0; }
	public SeObjectId getFeatureId() throws SeException { return null; }
	public int getNumOfPoints(){return 0;}
	public void generateSimpleLine(int numPts, int numParts, int[] partOffsets, SDEPoint[] ptArray){}

}
