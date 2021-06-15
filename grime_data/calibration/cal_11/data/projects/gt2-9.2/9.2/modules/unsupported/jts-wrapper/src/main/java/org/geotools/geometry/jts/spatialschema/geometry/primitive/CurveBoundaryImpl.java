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

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.geometry.complex.Complex;
import org.opengis.geometry.primitive.CurveBoundary;
import org.opengis.geometry.primitive.Point;

/**
 * This is Chris's implementation of a CurveBoundary.  I started it and
 * realized about halfway through that I won't necessarily need it.  So the
 * last few methods are still unimplemented (and just delegate to the
 * superclass, which currently does nothing).
 *
 *
 *
 *
 * @source $URL$
 */
public class CurveBoundaryImpl extends PrimitiveBoundaryImpl implements CurveBoundary {
    
    //*************************************************************************
    //  
    //*************************************************************************
    /**
     * Comment for {@code EMPTY_COMPLEX_ARRAY}.
     */
    private static final Complex [] EMPTY_COMPLEX_ARRAY = new Complex[0];

    //*************************************************************************
    //  
    //*************************************************************************
    
    private Point startPoint;
    
    private Point endPoint;
    
    private Set pointSet;

    //*************************************************************************
    //  
    //*************************************************************************
    
    public CurveBoundaryImpl(
            final CoordinateReferenceSystem crs, 
            final Point startPoint, 
            final Point endPoint) {
        
        super(crs);
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        HashSet tempSet = new HashSet();
        if (startPoint != null) {
            tempSet.add(startPoint);
        }
        if (endPoint != null) { 
            tempSet.add(endPoint);
        }
        this.pointSet = Collections.unmodifiableSet(tempSet);
    }

    //*************************************************************************
    //  
    //*************************************************************************

    /**
     * @inheritDoc
     * @see org.opengis.geometry.primitive.CurveBoundary#getStartPoint()
     */
    public Point getStartPoint() {
        return startPoint;
    }

    /**
     * @inheritDoc
     * @see org.opengis.geometry.primitive.CurveBoundary#getEndPoint()
     */
    public Point getEndPoint() {
        return endPoint;
    }
}
