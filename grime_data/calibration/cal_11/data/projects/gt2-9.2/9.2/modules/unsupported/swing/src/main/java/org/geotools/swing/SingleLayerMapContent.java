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
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.swing;

import org.geotools.map.Layer;
import org.geotools.map.MapContent;

/**
 * Used by {@linkplain DefaultRenderingExecutor} to hold a single {@code Layer}
 * that is being passed to a renderer. Calling the {@code dispose} method of 
 * this class does not dispose of the layer unlike {@linkplain MapContent#dispose()}.
 * It does not permit subsequent changes to its layer list.
 * 
 * @author Michael Bedward
 * @since 8.0
 *
 * @source $URL$
 * @version $Id$
 */
public class SingleLayerMapContent extends MapContent {

    /**
     * Creates a new instance to hold the given layer.
     * 
     * @param layer the layer
     * @throws IllegalArgumentException if {@code layer} is {@code null}
     */
    public SingleLayerMapContent(Layer layer) {
        if (layer == null) {
            throw new IllegalArgumentException("layer must not be null");
        }
        super.addLayer(layer);
    }

    /**
     * Throws an {@code UnsupportedOperationException} if called.
     * 
     * @return 
     */
    @Override
    public boolean addLayer(Layer layer) {
        throw new UnsupportedOperationException("Should not be called");
    }

    /**
     * Throws an {@code UnsupportedOperationException} if called.
     * 
     * @return 
     */
    @Override
    public void moveLayer(int sourcePosition, int destPosition) {
        throw new UnsupportedOperationException("Should not be called");
    }

    /**
     * Throws an {@code UnsupportedOperationException} if called.
     * 
     * @return 
     */
    @Override
    public boolean removeLayer(Layer layer) {
        throw new UnsupportedOperationException("Should not be called");
    }
    
    /**
     * Does nothing.
     */
    @Override
    public void dispose() {
        // does nothing
    }

    /**
     * Does nothing.
     */
    @Override
    protected void finalize() throws Throwable {
        // does nothing
    }
    
}
