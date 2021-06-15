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
package org.geotools.geometry.jts.spatialschema.geometry;

import org.geotools.geometry.jts.GeometryUtils;

import javax.measure.unit.NonSI;

import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.Envelope;

/**
 * A minimum bounding box or rectangle. Regardless of dimension, an {@code Envelope} can
 * be represented without ambiguity as two direct positions (coordinate points). To encode an
 * {@code Envelope}, it is sufficient to encode these two points. This is consistent with
 * all of the data types in this specification, their state is represented by their publicly
 * accessible attributes.
 *
 * @UML datatype GM_Envelope
 * @author ISO/DIS 19107
 * @author <A HREF="http://www.opengis.org">OpenGIS&reg; consortium</A>
 *
 *
 *
 *
 * @source $URL$
 * @version 2.0
 */
public class EnvelopeImpl implements Envelope {

    //*************************************************************************
    //  Fields
    //*************************************************************************

    /**
     * DirectPosition that has the minimum values for each coordinate dimension
     * (e.g. min x and min y).
     */
    private DirectPosition lowerCorner;

    /**
     * DirectPosition that has the maximum values for each coordinate dimension
     * (e.g. max x and max y).
     */
    private DirectPosition upperCorner;

    //*************************************************************************
    //  Constructors
    //*************************************************************************

    /**
     * Creates a new {@code EnvelopeImpl}.
     * @param lowerCorner
     * @param upperCorner
     */
    public EnvelopeImpl(
            final DirectPosition lowerCorner, 
            final DirectPosition upperCorner) {
        this.lowerCorner = new DirectPositionImpl(lowerCorner);
        this.upperCorner = new DirectPositionImpl(upperCorner);
    }

    //*************************************************************************
    //  implement the Envelope interface
    //*************************************************************************

    /**
     * @inheritDoc
     * @see org.opengis.geometry.coordinate.Envelope#getDimension()
     */
    public final int getDimension() {
        return upperCorner.getDimension();
    }

    /**
     * @inheritDoc
     * @see org.opengis.geometry.coordinate.Envelope#getMinimum(int)
     */
    public final double getMinimum(int dimension) {
        return lowerCorner.getOrdinate(dimension);
    }

    /**
     * @inheritDoc
     * @see org.opengis.geometry.coordinate.Envelope#getMaximum(int)
     */
    public final double getMaximum(int dimension) {
        return upperCorner.getOrdinate(dimension);
    }

    /**
     * @inheritDoc
     * @see org.opengis.geometry.coordinate.Envelope#getCenter(int)
     */
    @Deprecated
    public final double getCenter(int dimension) {
        return 0.5 * (upperCorner.getOrdinate(dimension) + lowerCorner.getOrdinate(dimension));
    }

    /**
     * @inheritDoc
     * @see org.opengis.geometry.coordinate.Envelope#getMedian(int)
     */
    public final double getMedian(int dimension) {
        return 0.5 * (upperCorner.getOrdinate(dimension) + lowerCorner.getOrdinate(dimension));
    }

    /**
     * @inheritDoc
     * @see org.opengis.geometry.coordinate.Envelope#getLength(int)
     */
    @Deprecated
    public final double getLength(int dimension) {
        return upperCorner.getOrdinate(dimension) - lowerCorner.getOrdinate(dimension);
    }

    /**
     * @inheritDoc
     * @see org.opengis.geometry.coordinate.Envelope#getSpan(int)
     */
    public final double getSpan(int dimension) {
        return upperCorner.getOrdinate(dimension) - lowerCorner.getOrdinate(dimension);
    }

    /**
     * @inheritDoc
     * @see org.opengis.geometry.coordinate.Envelope#getUpperCorner()
     */
    public final DirectPosition getUpperCorner() {
        return new DirectPositionImpl(upperCorner);
    }

    /**
     * @inheritDoc
     * @see org.opengis.geometry.coordinate.Envelope#getLowerCorner()
     */
    public final DirectPosition getLowerCorner() {
        return new DirectPositionImpl(lowerCorner);
    }
    
    /**
     * @inheritDoc
     * @see java.lang.Object#toString()
     */
    public String toString() {
        final double[] bbox = GeometryUtils.getBBox(this, NonSI.DEGREE_ANGLE);
        
        final StringBuffer returnable = new StringBuffer("Envelope[").append(bbox[0]);
        for (int i = 1; i < bbox.length; i++) {
            returnable.append(",").append(bbox[i]);
        }
        return returnable.append("]").toString();
    }
    
    
    /**
     * @inheritDoc
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(final Object obj) {
        final Envelope that = (Envelope) obj;
        return GeometryUtils.equals(this, that);
    }

    public CoordinateReferenceSystem getCoordinateReferenceSystem() {
        return getUpperCorner().getCoordinateReferenceSystem();
    }
}
