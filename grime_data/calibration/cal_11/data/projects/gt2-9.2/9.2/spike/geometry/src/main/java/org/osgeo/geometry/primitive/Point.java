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
 *    OSGeom -- Geometry Collab
 *
 *    (C) 2009, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2001-2009 Department of Geography, University of Bonn
 *    (C) 2001-2009 lat/lon GmbH
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
package org.osgeo.geometry.primitive;

/**
 * 0-dimensional primitive.
 * 
 * @author <a href="mailto:schneider@lat-lon.de">Markus Schneider</a>
 * @author <a href="mailto:jive@users.sourceforge.net">Jody Garnett</a>
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @author last edited by: $Author$
 * 
 * @version. $Revision$, $Date$
 */
public interface Point extends GeometricPrimitive {

    /**
     * Must always return {@link GeometricPrimitive.PrimitiveType#Point}.
     * 
     * @return {@link GeometricPrimitive.PrimitiveType#Point}
     */
    public PrimitiveType getPrimitiveType();

    /**
     * Returns the value of the ordinate 0.
     * 
     * @return value of the ordinate 0
     */
    public double get0();

    /**
     * Returns the value of the ordinate 1.
     * 
     * @return value of the ordinate 1, or <code>Double.NAN</code> if the point only has one dimension
     */
    public double get1();

    /**
     * Returns the value of the ordinate 2.
     * 
     * @return value of the ordinate 2, or <code>Double.NAN</code> if the point only has one or two dimensions
     */
    public double get2();

    /**
     * Returns the value of the specified ordinate.
     * 
     * @param dimension
     *            ordinate to be returned (first dimension=0)
     * @return ordinate value of the passed dimension, or <code>Double.NAN</code> if <code>dimension</code> is greater
     *         than the number of actual dimensions
     */
    public double get( int dimension ); 

    /**
     * Returns all ordinates.
     * 
     * @return all ordinates, the length of the array is equal to the number of dimensions
     */
    public double[] getAsArray(); // This is copy of the internal ordinates presented as an array; does not offer direct access
    
    /**
     * All ordinates for the point.
     * @param ordinates All ordinates for the point
     */
    public void setAsArray( double ordinates[] ); // Bulk set of ordintes; some implementations may be able to use array copy for speed
}
