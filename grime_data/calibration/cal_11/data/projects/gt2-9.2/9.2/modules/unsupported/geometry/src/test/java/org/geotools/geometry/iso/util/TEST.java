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
 */
package org.geotools.geometry.iso.util;


import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import org.geotools.geometry.iso.util.algorithm2D.AlgoPoint2D;
import org.geotools.geometry.iso.util.algorithm2D.AlgoRectangle2D;

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
public class TEST {

	public static void main(String[] args) {
		Double dd = new Double(1.0);
		double aa = dd.doubleValue();
		int ii = 1;
		Integer iii = new Integer(ii);
		
		Rectangle2D r= new Rectangle2D.Double(10.0,80.0,10.0,10.0);
		Point2D pa = new Point2D.Double(0.0, 100.0);
		Point2D pb = new Point2D.Double(23.423188405797102, 78.11304347826088);
		Point2D pab[] = AlgoRectangle2D.intersectionRectangleLine(r,new Line2D.Double(pa,pb));
		
		Rectangle2D r0= new Rectangle2D.Double(0.0,0.0,100.0,100.0);
		Line2D l0 = new Line2D.Double(-10.0,50.0,110.0,50.0);
		Point2D p0[] = AlgoRectangle2D.intersectionRectangleLine(r0,l0);
		Line2D l1 = new Line2D.Double(-10.0,0.0,110.0,0.0);
		Point2D p1[] = AlgoRectangle2D.intersectionRectangleLine(r0,l1);
		Line2D l2 = new Line2D.Double(-10.0,10.0,10.0,-10.0);
		Point2D p2[] = AlgoRectangle2D.intersectionRectangleLine(r0,l2);
		int i=0;
		
		pa.setLocation(1.0,0.0);
		pb.setLocation(1.0,1.0);
		double a0 = AlgoPoint2D.getAngle2D(pa,pb);

		pa.setLocation(1.0,0.0);
		pb.setLocation(1.0,-1.0);
		a0 = AlgoPoint2D.getAngle2D(pa,pb);

		pa.setLocation(1.0,0.0);
		pb.setLocation(-1.0,1.0);
		a0 = AlgoPoint2D.getAngle2D(pa,pb);

		pa.setLocation(1.0,0.0);
		pb.setLocation(-1.0,-1.0);
		a0 = AlgoPoint2D.getAngle2D(pa,pb);

		pa.setLocation(1.0,0.0);
		pb.setLocation(-1.0,0.0);
		a0 = AlgoPoint2D.getAngle2D(pa,pb);
		
		pa.setLocation(-1.0,1.0);
		pb.setLocation(-1.0,-1.0);
		a0 = AlgoPoint2D.getAngle2D(pa,pb);
		
		
	}
}
