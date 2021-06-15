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

//J2SE dependencies
import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.Arrays;

//openGIS dependencies
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.coordinate.Position;
import org.opengis.geometry.primitive.Point;
import org.opengis.util.Cloneable;

/**
 * Holds the coordinates for a position within some coordinate reference system. Since
 * {@code DirectPosition}s, as data types, will often be included in larger objects
 * (such as {@linkplain org.opengis.geometry.coordinate. geometries}) that have
 * references to {@link CoordinateReferenceSystem}, the {@link #getCoordinateReferenceSystem}
 * method may returns {@code null} if this particular {@code DirectPosition} is
 * included in a larger object with such a reference to a {@linkplain CoordinateReferenceSystem
 * coordinate reference system}. In this case, the cordinate reference system is implicitly
 * assumed to take on the value of the containing object's {@link CoordinateReferenceSystem}.
 * 
 * @UML datatype DirectPosition
 * @author ISO/DIS 19107
 * @author <A HREF="http://www.opengis.org">OpenGIS&reg; consortium</A>
 *
 *
 *
 *
 * @source $URL$
 * @version $Revision: 1.9 $, $Date: 2005/11/02 05:39:33 $
 *
 * @revisit Version number: I suggest to use <strong>specification</strong> version number
 *          (here 2.0).
 */
public class DirectPositionImpl implements Cloneable, DirectPosition, Position, Serializable {
    
    //*************************************************************************
    //  Fields
    //*************************************************************************
    
    private static int hashCode(double[] array) {
		final int PRIME = 31;
		if (array == null)
			return 0;
		int result = 1;
		for (int index = 0; index < array.length; index++) {
			long temp = Double.doubleToLongBits(array[index]);
			result = PRIME * result + (int) (temp ^ (temp >>> 32));
		}
		return result;
	}

	/**
     * Comment for {@code ordinates}.
     */
    public final double[] ordinates;
    
    /**
     * Comment for {@code crs}.
     */
    private CoordinateReferenceSystem crs;
    
    //*************************************************************************
    //  Constructors
    //*************************************************************************
    
    /**
     * Construct a position with the specified number of dimensions.
     *
     * @param  numDim Number of dimensions.
     * @throws NegativeArraySizeException if {@code numDim} is negative.
     */
    public DirectPositionImpl(final int numDim) throws NegativeArraySizeException {
        ordinates = new double[numDim];
    }
    
    /**
     * Construct a position with the specified ordinates.
     * The {@code ordinates} array will be copied.
     */
    public DirectPositionImpl(final double[] ordinates) {
        this.ordinates = (double[]) ordinates.clone();
    }
    
    /**
     * Construct a 2D position from the specified ordinates.
     */
    public DirectPositionImpl(final double x, final double y) {
        ordinates = new double[] {x,y};
    }
    
    /**
     * Construct a 3D position from the specified ordinates.
     */
    public DirectPositionImpl(final double x, final double y, final double z) {
        ordinates = new double[] {x,y,z};
    }
    
    /**
     * Construct a position from the specified {@link Point2D}.
     */
    public DirectPositionImpl(final Point2D point) {
        this(point.getX(), point.getY());
    }
    
    /**
     * Construct a position initialized to the same values than the specified point.
     */
    public DirectPositionImpl(final DirectPositionImpl point) {
        ordinates = (double[]) point.ordinates.clone();
        crs = point.crs;
    }
    
    /**
     * Construct a position initialized to the same values than the specified point.
     */
    public DirectPositionImpl(final DirectPosition point) {
        ordinates = (double[]) point.getCoordinate().clone();
        crs = point.getCoordinateReferenceSystem();
    }

    /**
     * Creates a new {@code DirectPositionImpl}.
     * @param crs
     */
    public DirectPositionImpl(final CoordinateReferenceSystem crs) {
        setCRS(crs);
        this.ordinates = new double[crs.getCoordinateSystem().getDimension()];
    }
    
    /**
     * Creates a new {@code DirectPositionImpl}.
     * @param crs
     * @param ordinates
     */
    public DirectPositionImpl(final CoordinateReferenceSystem crs, final double[] ordinates) {
        setCRS(crs);
        this.ordinates = new double[crs.getCoordinateSystem().getDimension()];
        for (int i = 0; i < crs.getCoordinateSystem().getDimension(); i++) {
            this.ordinates[i] = ordinates[i];
        }
    }
    
    public String toString() {
        StringBuffer buff = new StringBuffer("DirectPositionImpl(");
        for (int i = 0; i < ordinates.length; i++) {
            buff.append(ordinates[i]);
            if (i < ordinates.length-1) {
                buff.append(",");
            }
        }
        buff.append(")");
        return buff.toString();
    }

    //*************************************************************************
    //  implement the DirectPosition interface
    //*************************************************************************
    
    /**
     * DOCUMENT ME.
     * @param crs
     */
    private void setCRS(final CoordinateReferenceSystem crs) {
        if (crs == null) {
            throw new IllegalArgumentException("DirectPosition cannot have a null CRS");
        }
        this.crs = crs;
        //ordinates = new double[crs.getCoordinateSystem().getDimension()];        
    }
    
    /**
     * @inheritDoc
     * @see org.opengis.geometry.coordinate.DirectPosition#getDimension()
     */
    public int getDimension() {
        return ordinates.length;
    }

    /**
     * @inheritDoc
     * @see org.opengis.geometry.coordinate.DirectPosition#getCoordinates()
     */
    public double[] getCoordinate() {
        return ordinates;
    }
    @Deprecated
    public double[] getCoordinates() {
        return getCoordinate();
    }

    /**
     * @inheritDoc
     * @see org.opengis.geometry.coordinate.DirectPosition#getOrdinate(int)
     */
    public double getOrdinate(final int dimension) throws IndexOutOfBoundsException {
        return ordinates[dimension];
    }

    /**
     * @inheritDoc
     * @see org.opengis.geometry.coordinate.DirectPosition#setOrdinate(int, double)
     */
    public void setOrdinate(final int dimension, final double value) throws IndexOutOfBoundsException {
        ordinates[dimension] = value;
    }

    /**
     * @inheritDoc
     * @see org.opengis.geometry.coordinate.DirectPosition#getCoordinateReferenceSystem()
     */
    public CoordinateReferenceSystem getCoordinateReferenceSystem() {
        return crs;
    }

    /**
     * @inheritDoc
     * @see java.lang.Object#clone()
     */
    public DirectPositionImpl clone() {
        /*DirectPositionImpl result = (DirectPositionImpl) super.clone();
        result.ordinates = (double []) ordinates.clone();
        return result;*/
        return new DirectPositionImpl(this);
    }   
    
    /**
     * @inheritDoc
     * @see org.opengis.geometry.coordinate.Position#getDirectPosition()
     */
    public DirectPosition getDirectPosition() {
        return this;
    }
    
    /**
     * @inheritDoc
     * @see org.opengis.geometry.coordinate.Position#getPosition()
     */
    @Deprecated
    public DirectPosition getPosition() {
        return this;
    }

	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((crs == null) ? 0 : crs.hashCode());
		result = PRIME * result + DirectPositionImpl.hashCode(ordinates);
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final DirectPositionImpl other = (DirectPositionImpl) obj;
		if (crs == null) {
			if (other.crs != null)
				return false;
		} else if (!crs.equals(other.crs))
			return false;
		if (!Arrays.equals(ordinates, other.ordinates))
			return false;
		return true;
	}
    
}
