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
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.renderer;

import java.awt.Graphics;

import org.geotools.feature.FeatureCollection;
import org.geotools.styling.Style;
import org.opengis.feature.Feature;
import org.opengis.feature.type.FeatureType;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;

/**
 * Base interface for renderer. This is very much work in progress.
 * <strong>Note: this interface will changes in future versions.</strong>
 *
 *
 *
 * @source $URL$
 * @version $Id$
 * @author James Macgill
 */
public interface Renderer {
    
    /**
     * Renders the provided features using the specified style.
     * The features should fill the viewport but may well extend beyond it.
     * Features should be cropped (if appropriate) to the specified viewport.
     *
     * @param fc The feature collection to render
     * @param viewport The visible extent to be rendered
     * @param style The style definition to apply to each feature
     */
    void render(FeatureCollection<? extends FeatureType, ? extends Feature> fc, Envelope viewport, Style style);

    /**
     * Getter for property interactive.
     * @return Value of property interactive.
     */
    boolean isInteractive();
    
    /**
     * Setter for property interactive.
     * @param interactive New value of property interactive.
     */
    void setInteractive(boolean interactive);
    
    /** sets the output graphics for the renderer and the size of the graphic.
     */
    void setOutput(Graphics g, java.awt.Rectangle r);
    
    public Coordinate pixelToWorld(int x, int y, Envelope map);
}
