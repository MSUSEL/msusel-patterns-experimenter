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
 * [ 1719398 ] First shot at LWPOLYLINE
 * Peter Hopfgartner - hopfgartner
 *  
 */
package org.geotools.data.dxf.entities;

import java.awt.geom.Point2D;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 *
 * @source $URL$
 */
public class DXFLwVertex extends DXFPoint {

    private static final Log log = LogFactory.getLog(DXFLwVertex.class);

    /*
     * The bulge is the tangent of 1/4 of the included angle for the arc 
     * between the selected vertex and the next vertex in the polyline's 
     * vertex list. A negative bulge value indicates that the arc goes 
     * clockwise from the selected vertex to the next vertex. A bulge of 
     * 0 indicates a straight segment, and a bulge of 1 is a semicircle.
     * 
     * http://www.afralisp.net/lisp/Bulges1.htm
     */
    double _bulge;

    public DXFLwVertex(Point2D.Double p, double bulge) {
        super(p);
        this._bulge = bulge;
        setName("DXFLwVertex");
    }

    public DXFLwVertex(double x, double y, double bulge) {
        super(new Point2D.Double(x, y));
        this._bulge = bulge;
        setName("DXFLwVertex");
    }

    public DXFLwVertex(DXFLwVertex orig, boolean bis) {
        super(orig._point.x, orig._point.y, orig.getColor(), orig.getRefLayer(), 0, 1);
        _bulge = orig._bulge;
        setName("DXFLwVertex");
    }

    public double getBulge() {
        return _bulge;
    }
    
    public String toString(double b, double x, double y) {
        StringBuffer s = new StringBuffer();
        s.append("DXFLwVertex [");
        s.append("bulge: ");
        s.append(b + ", ");
        s.append("x: ");
        s.append(x + ", ");
        s.append("y: ");
        s.append(y);
         s.append("]");
        return s.toString();
    }
    
    /*
     * Calculates the <see cref="Bulge"/> from the arc segment's 
     * center and end points.
     * <param name="center">The arc center.</param>
     * <param name="arcStart">The arc start point.</param>
     * <param name="arcEnd">The arc end point.</param>
     */
    public static double GetBulgeFromEndPoints(
            Point2D center,
            Point2D arcStart,
            Point2D arcEnd) {
        // Take v1 as the x-axis.
        Point2D v1 = new Point2D.Double(arcStart.getX() - center.getX(),arcStart.getY() - center.getY());
        Point2D yaxis = new Point2D.Double(-v1.getY(), v1.getX());

        Point2D v2 = new Point2D.Double(arcEnd.getX() - center.getX(),arcEnd.getY() - center.getY());
        // Project on v1.
        double x = v1.getX()*v2.getX()+v1.getY()*v2.getY();
        double y = yaxis.getX()*v2.getX()+yaxis.getY()*v2.getY();
 
        double angle = Math.atan2(y, x);

        return Math.tan(angle / 4d);
    }

    public DXFEntity clone(){
        return new DXFLwVertex(this, true);
    }
}
