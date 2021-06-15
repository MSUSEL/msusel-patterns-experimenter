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
 *    (C) 2003-2008, Open Source Geospatial Foundation (OSGeo)
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
 *
 *    Refractions Research Inc. Can be found on the web at:
 *    http://www.refractions.net/
 *
 *    Created on Oct 29, 2003
 */
package org.geotools.data.oracle.sdo;

import com.vividsolutions.jts.geom.CoordinateSequenceFactory;

/**
 * Extends CoordianteSequenceFactory with meta data information.
 * <p>
 * This allows us to determine the dimensions of a Geometry.
 * </p>
 * @author jgarnett
 *
 *
 * @source $URL$
 */
public interface CoordinateAccessFactory extends CoordinateSequenceFactory {
    /**
     * Create method that allows additional content.
     * <p>
     * Example: (x,y,z,t) getDimension()==2, getNumAttributes()==2
     * </p> 
     * <pre><code>
     * <b>xyz</b>:[ [ x1, x2,...,xN], [ y1, y2,...,yN] ]
     * <b>attributes</b>:[ [ z1, z2,...,zN], [ t1, t2,..., tN] ]
     * </code></pre>
     * @param xyz        an array of doubles in column major order where xyz.length == getDimension()
     * @param attributes an array of Objects which can be null. Column major measure arrays where attributes.length == getNumAttributes()
     */    
    public CoordinateAccess create(double[] xyz[], Object[] attributes );

    /** Number of spatial ordinates() */
    public int getDimension();
    
    /** Number of non spatial ordinates() */
    public int getNumAttributes();
}
