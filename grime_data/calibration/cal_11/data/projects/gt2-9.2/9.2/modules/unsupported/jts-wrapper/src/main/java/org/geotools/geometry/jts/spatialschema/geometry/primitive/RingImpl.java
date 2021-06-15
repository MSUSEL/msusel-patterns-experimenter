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
package org.geotools.geometry.jts.spatialschema.geometry.primitive;

import org.opengis.referencing.crs.CoordinateReferenceSystem;
//import org.opengis.geometry.Geometry;
import org.opengis.geometry.complex.CompositeCurve;
//import org.opengis.geometry.coordinate.LineString;
import org.opengis.geometry.primitive.OrientableCurve;
import org.opengis.geometry.primitive.Ring;
import org.opengis.geometry.primitive.Shell;
import org.opengis.geometry.primitive.SurfaceBoundary;

import org.geotools.geometry.jts.spatialschema.geometry.complex.CompositeCurveImpl;
import org.geotools.geometry.jts.JTSUtils;
import com.vividsolutions.jts.geom.LineString;
//import com.vividsolutions.jts.geom.Geometry;

//import java.util.Iterator;
//import java.util.List;


/**
 * Represent a single connected component of a {@linkplain SurfaceBoundary surface boundary}.
 * It consists of a number of references to {@linkplain OrientableCurve orientable curves}
 * connected in a cycle (an object whose boundary is empty). A {@code Ring} is structurally
 * similar to a {@linkplain CompositeCurve composite curve} in that the end point of each
 * {@linkplain OrientableCurve orientable curve} in the sequence is the start point of the next
 * {@linkplain OrientableCurve orientable curve} in the sequence. Since the sequence is circular,
 * there is no exception to this rule. Each ring, like all boundaries is a cycle and each ring is
 * simple.
 * <br><br>
 * Even though each {@code Ring} is simple, the boundary need not be simple. The easiest
 * case of this is where one of the interior rings of a surface is tangent to its exterior ring.
 * Implementations may enforce stronger restrictions on the interaction of boundary elements.
 * <p>
 * This implementation does not automatically close itself.  The isValid method
 * returns false if the curve is either not closed or crosses itself.
 *
 * @UML type GM_Ring
 * @author ISO/DIS 19107
 * @author <A HREF="http://www.opengis.org">OpenGIS&reg; consortium</A>
 *
 *
 *
 *
 * @source $URL$
 * @version 2.0
 *
 * @see SurfaceBoundary
 * @see Shell
 */
public class RingImpl extends CompositeCurveImpl implements Ring {
    /**
     * Constructs a new Ring instance with no CRS and no parent curve.
     */
    public RingImpl() {
        this(null, null);
    }

    /**
     * Constructs a new Ring instance with the given CRS and no parent curve.
     */
    public RingImpl(CoordinateReferenceSystem crs) {
        this(null, crs);
    }

    /**
     * Constructs a new Ring instance with the given curve as its parent and
     * with the CRS retrieved from parentCurve.
     */
    public RingImpl(CompositeCurve parentCurve) {
        this(parentCurve, parentCurve.getCoordinateReferenceSystem());
    }

    public RingImpl(CompositeCurve parentCurve, CoordinateReferenceSystem crs) {
        super(parentCurve, crs);
    }
    
    /**
     * This implementation returns true if the curve
     * does not cross itself, false otherwise.
     * It does not test for closure or disconnects.
     * To check for disconnects, call the superclass method.
     * @return True if this object's coordinates are a valid Ring.
     */
    public boolean isValid() {
    	// Verify that the line doesn't cross itself
        com.vividsolutions.jts.geom.Coordinate[] coords =
        	computeJTSPeer().getCoordinates();
        int count = coords.length;
//System.err.println("RingImpl.isValid -- coord count " + count);
        if (count > 2) {
        	if (!coords[0].equals(coords)) {
//System.err.println("  Adding closure coord");
        		// Close the set of coordinates for the validation test
        		// if it isn't already closed
        		com.vividsolutions.jts.geom.Coordinate[] tmp =
        			new com.vividsolutions.jts.geom.Coordinate[count + 1];
        		System.arraycopy(coords, 0, tmp, 0, count);
        		tmp[count] = coords[0];
        		coords = tmp;
        	}
        	LineString jtsLineString = JTSUtils.GEOMETRY_FACTORY.createLineString(coords);
        	/*
System.err.println("  Created JTS LineString, point count " + jtsLineString.getNumPoints()
	+ ", length " + jtsLineString.getLength() + "\n  simple? " + jtsLineString.isSimple()
	+ "; valid? " + jtsLineString.isValid() + "; closed? " + jtsLineString.isClosed());
	*/        	
        	return jtsLineString.isRing();
        }
        return false;
    }
}
