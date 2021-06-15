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
package org.geotools.renderer3d.provider.height;

import org.geotools.renderer3d.utils.BoundingRectangle;

import java.nio.FloatBuffer;

/**
 * A listener that is called when a HeightProvider has the height data ready for a requestd area.
 *
 * @author Hans H�ggstr�m
 */
public interface HeightListener
{
    /**
     * @param heightProvider the provider that created the data.
     * @param area           the area that the height data is for
     * @param heightData     the height data for each gridpoint in the requested area, with the number of grid nodes specified
     *                       in the request call.  The data is stored in y major order (row by row).
     *                       The buffer should be reset so that it is ready to read from index 0.
     *                       <p/>
     *                       TODO: Check what unit is the height expressed in, and relative to what base?
     *                       Sealevel presumably, with same units as the area is defined in.
     *                       <p/>
     *                       When this listener call returns, the HeightProvider is free to re-use the heightData buffer,
     *                       as the height values will be copied into internal data structures by the listener.
     */
    void onHeightDataReady( HeightProvider heightProvider, BoundingRectangle area, FloatBuffer heightData );

}
