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

import org.opengis.geometry.primitive.Bearing;


/**
 * Represents direction in the coordinate reference system. In a 2D coordinate reference
 * system, this can be accomplished using a "angle measured from true north" or a 2D vector
 * point in that direction. In a 3D coordinate reference system, two angles or any 3D vector
 * is possible. If both a set of angles and a vector are given, then they shall be consistent
 * with one another.
 *
 * @UML datatype Bearing
 * @author ISO/DIS 19107
 * @author <A HREF="http://www.opengis.org">OpenGIS&reg; consortium</A>
 *
 *
 *
 *
 * @source $URL$
 * @version 2.0
 *
 * @revisit Should we move this interface elsewhere (e.g. in some kind of units package)?
 */
public class BearingImpl implements Bearing {
    
    //*************************************************************************
    //  fields
    //*************************************************************************
    
    private double[] angles;
    
    private double[] direction;
    
    //*************************************************************************
    //  Constructor
    //*************************************************************************
    
    public BearingImpl(double[] angles, double[] direction) {
        this.angles = angles;
        this.direction = direction;
    }
    
    //*************************************************************************
    //
    //*************************************************************************
    
    /**
     * Returns the azimuth and (optionnaly) the altitude.
     * In this variant of bearing usually used for 2D coordinate systems, the first angle (azimuth)
     * is measured from the first coordinate axis (usually north) in a counterclockwise fashion
     * parallel to the reference surface tangent plane. If two angles are given, the second angle
     * (altitude) usually represents the angle above (for positive angles) or below (for negative
     * angles) a local plane parallel to the tangent plane of the reference surface.
     *
     * @return An array of length 0, 1 or 2 containing the azimuth and altitude angles.
     * @UML operation angle
     *
     * @revisit Should we split this method in {@code getAzimuth()} and
     *          {@code getAltitude()} methods instead? Should we provides
     *          a {@code getDimension()} method too?
     */
    public double[] getAngles() {
        return angles;
    }

    /**
     * Returns the direction as a vector.
     * In this variant of bearing usually used for 3D coordinate systems, the direction is
     * express as an arbitrary vector, in the coordinate system.
     *
     * @return The direction.
     * @UML operation direction
     */
    public double[] getDirection() {
        return direction;
    }
}
