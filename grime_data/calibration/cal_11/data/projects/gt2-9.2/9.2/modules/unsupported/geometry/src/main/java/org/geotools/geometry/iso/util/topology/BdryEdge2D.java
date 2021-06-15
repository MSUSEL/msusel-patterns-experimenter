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
package org.geotools.geometry.iso.util.topology;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import org.geotools.geometry.iso.util.algorithm2D.AlgoLine2D;


/**
 * @author roehrig
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 *
 *
 *
 *
 * @source $URL$
 */
public class BdryEdge2D extends BRepEdge2D {

	protected BRepNode2D p1;
	protected BRepNode2D p2;
	
	public BdryEdge2D(BRepNode2D p1, BRepNode2D p2, BRepFace2D surfaceRight, BRepFace2D surfaceLeft) {
		super(surfaceRight, surfaceLeft);
		this.p1 = p1;
		this.p2 = p2;
	}
	public BRepNode2D getNodeBeg() {
		return p1;
	}
	public BRepNode2D getNodeEnd() {
		return p2;
	}
	public void setSimplex(BRepFace2D simplex){
		if (this.surfaceRight == null) this.surfaceRight = simplex;
		else this.surfaceLeft = simplex;
	}
	/**
	 * @return Returns the point0.
	 */
	public  boolean hasPoint(BRepNode2D p) {
		return p1==p || p2==p;
	}
	/**
	 * @return Returns the surfaceLeft.
	 */
	public  boolean hasSimplex(BRepFace2D s) {
		return surfaceRight==s || surfaceLeft==s;
	}

	/**
	 * @param f
	 * @return
	 */
	public BRepFace2D getNeighborSimplex(BRepFace2D f) {
		return (surfaceRight==f) ? surfaceLeft : surfaceRight;
	}

	/* (non-Javadoc)
	 * @see java.awt.geom.Line2D#getX1()
	 */
	public double getX1() {
		return this.p1.getX();
	}
	/* (non-Javadoc)
	 * @see java.awt.geom.Line2D#getY1()
	 */
	public double getY1() {
		return this.p1.getY();
	}
	/* (non-Javadoc)
	 * @see java.awt.geom.Line2D#getP1()
	 */
	public Point2D getP1() {
		return this.p1;
	}
	/* (non-Javadoc)
	 * @see java.awt.geom.Line2D#getX2()
	 */
	public double getX2() {
		return this.p2.getX();
	}
	/* (non-Javadoc)
	 * @see java.awt.geom.Line2D#getY2()
	 */
	public double getY2() {
		return this.p2.getY();
	}
	/* (non-Javadoc)
	 * @see java.awt.geom.Line2D#getP2()
	 */
	public Point2D getP2() {
		return this.p2;
	}
	/* (non-Javadoc)
	 * @see java.awt.geom.Line2D#setLine(double, double, double, double)
	 */
	public void setLine(double x1, double y1, double x2, double y2) {
		this.p1.setLocation(x1,y1);
		this.p2.setLocation(x2,y2);
	}
	/* (non-Javadoc)
	 * @see java.awt.Shape#getBounds2D()
	 */
	public Rectangle2D getBounds2D() {
		double x1 = getX1();
		double y1 = getY1();
		double x2 = getX2();
		double y2 = getY2();
		if (x2<x1) {
			double tmp = x2;
			x2 = x1;
			x1 = tmp;
		}
		if (y2<y1) {
			double tmp = y2;
			y2 = y1;
			y1 = tmp;
		}
		return new Rectangle2D.Double(x1,y1,x2-x1,y2-y1);
	}
	
	/**
	 * @param maxSpacing
	 */
	public ArrayList split(double maxSpacing) {
		BdryEdge2D line = this;
		int n = (int) Math.ceil(AlgoLine2D.length(line) / maxSpacing);
		double x1 = line.getX1();
		double y1 = line.getY1();
		double x2 = line.getX2();
		double y2 = line.getY2();
		double deltaX = (x2-x1) / n;
		double deltaY = (y2-y1) / n;
		BRepNode2D node1 = line.getNodeBeg();
		BRepNode2D node2;
		BdryEdge2D newEdge;

		ArrayList result = new ArrayList();
		result.add(this);
		for (int j = 1; j < n; ++j) {
			node2 = new BRepNode2D(x1 + deltaX * j, y1 + deltaY * j);
			newEdge = new BdryEdge2D(node1,node2,this.surfaceRight,this.surfaceLeft);
			node2.insertEdge(this);
			node2.insertEdge(newEdge);
			result.add(newEdge);
			node1 = node2;
		}
		/** add the last node*/
		newEdge = new BdryEdge2D(node1,line.getNodeEnd(),this.surfaceRight,this.surfaceLeft);
		result.add(newEdge);
		return result;
	}
	
	public void merge(BdryEdge2D other) {
		BRepNode2D n, n0, n1;
		if (this.p1==other.p1) {
			n = this.p1;
			n0 = this.p2;
			n1 = other.p2;
		} else if (this.p1==other.p2 ) {
			n = this.p1;
			n0 = this.p2;
			n1 = other.p1;
		} else if (this.p2==other.p1) {
			n = this.p2;
			n0 = this.p1;
			n1 = other.p2;
		} else if (this.p2==other.p2 ) {
			n = this.p2;
			n0 = this.p1;
			n1 = other.p1;
		}
		else return;
		
		if (n.edges.size()!=2) return;
		
		
	}
}
