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
package org.osgeo.geometry.primitive.patches;

import java.util.List;

import org.osgeo.geometry.points.Points;

/**
 * A {@link GriddedSurfacePatch} is a (usually non-planar) parametric {@link SurfacePatch} derived from a rectangular
 * grid in the parameter space. The rows from this grid are control points for horizontal surface curves; the columns
 * are control points for vertical surface curves.
 *
 * @author <a href="mailto:schneider@lat-lon.de">Markus Schneider </a>
 * @author last edited by: $Author$
 *
 * @version $Revision$, $Date$
 */
public interface GriddedSurfacePatch extends SurfacePatch {

    /**
     * Discriminates the different types of gridded surface patches.
     */
    public enum GriddedSurfaceType {
        /** A presumably custom type of GriddedSurfaceType */
        GRIDDED_SURFACE_PATCH,

        /** A gridded surface given as a family of conic sections whose control points vary linearly. */
        CONE,
        /**
         * A gridded surface given as a family of circles whose positions vary along a set of parallel lines, keeping
         * the cross sectional horizontal curves of a constant shape.
         */
        CYLINDER,
        /**
         * A gridded surface given as a family of circles whose positions vary linearly along the axis of the sphere,
         * and whise radius varies in proportions to the cosine function of the central angle. The horizontal circles
         * resemble lines of constant latitude, and the vertical arcs resemble lines of constant longitude.
         */
        SPHERE
    }

    /**
     * Returns the type of gridded surface, the type determines the horizontal and vertical curve types used for
     * interpolation.
     *
     * @return the type of gridded surface
     */
    public GriddedSurfaceType getGriddedSurfaceType();

    /**
     * Returns the number of rows in the parameter grid.
     *
     * @return the number of rows
     */
    public int getNumRows();

    /**
     * Returns the number of columns in the parameter grid.
     *
     * @return the number of columns
     */
    public int getNumColumns();

    /**
     * Returns the specified row of the parameter grid.
     *
     * @param rownum
     *            row to be returned
     * @return the specified row
     */
    public Points getRow( int rownum );

    /**
     * Returns all rows of the parameter grid.
     *
     * @return all rows
     */
    public List<Points> getRows();
}
