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
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.referencing.operation.builder;

import java.util.List;

import org.geotools.geometry.DirectPosition2D;
import org.geotools.referencing.operation.builder.algorithm.TPSInterpolation;
import org.geotools.referencing.operation.transform.IdentityTransform;
import org.opengis.geometry.Envelope;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.NoSuchIdentifierException;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;


/**
 * Implementation of grid builder based on thin plate spline (TPS) algorithm
 *
 * @see <A HREF="http://elonen.iki.fi/code/tpsdemo/index.html">Pages about TPS</A>
 *
 * @author jezekjan
 *
 */
public class TPSGridBuilder extends WarpGridBuilder {	
   
    /**
     * Constructs TPSGridBuilder from set of parameters.
     * @param vectors known shift vectors
     * @param dx width of gird cell
     * @param dy height of grid cells
     * @param env Envelope of generated grid
     * @throws TransformException
     */
    public TPSGridBuilder(List<MappedPosition> vectors, double dx, double dy, Envelope env)
        throws TransformException, NoSuchIdentifierException {
        this(vectors, dx, dy, env, IdentityTransform.create(2));
    }

    /**
     * Constructs TPSGridBuilder from set of parameters. The Warp Grid values are
     * calculated in transformed coordinate system.
     * @param vectors known shift vectors
     * @param dx width of gird cell
     * @param dy height of grid cells
     * @param envelope Envelope of generated grid
     * @param realToGrid Transformation from real to grid coordinates (when working with images)
     * @throws TransformException
     */
    public TPSGridBuilder(List<MappedPosition> vectors, double dx, double dy, Envelope envelope,
        MathTransform realToGrid) throws TransformException, NoSuchIdentifierException {
        super(vectors, dx, dy, envelope, realToGrid);     
    }

    protected float[] computeWarpGrid(GridParameters gridParams)
        throws TransformException {
    	
    	TPSInterpolation dxInterpolation = new TPSInterpolation(buildPositionsMap(0));
    	TPSInterpolation dyInterpolation = new TPSInterpolation(buildPositionsMap(1));
    	
        return interpolateWarpGrid(gridParams, dxInterpolation, dyInterpolation);    	      
    }
}
