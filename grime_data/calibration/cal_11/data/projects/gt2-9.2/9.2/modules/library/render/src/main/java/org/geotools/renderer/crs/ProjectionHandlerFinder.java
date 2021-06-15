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
package org.geotools.renderer.crs;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.util.logging.Logging;

/**
 * Looks up the {@link ProjectionHandler} for the specified rendering area.
 * WARNING: this API is not finalized and is meant to be used by StreamingRenderer only 
 * 
 * @author Andrea Aime - OpenGeo
 *
 *
 * @source $URL$
 */
public class ProjectionHandlerFinder {

    static List<ProjectionHandlerFactory> factories = new ArrayList<ProjectionHandlerFactory>();
    
    static final Logger LOGGER = Logging.getLogger(ProjectionHandlerFinder.class);
    
    public static final String WRAP_LIMIT_KEY = "org.geotools.render.wrapLimit";
    
    static int WRAP_LIMIT;

    static {
        factories.add(new GeographicHandlerFactory());
        factories.add(new MercatorHandlerFactory());
        factories.add(new TransverseMercatorHandlerFactory());
        factories.add(new PolarStereographicHandlerFactory());
        
        String wrapLimit = System.getProperty(WRAP_LIMIT_KEY);
        int limit = 10;
        try {
            if(wrapLimit != null) {
                limit = Integer.valueOf(wrapLimit);
            }
        } catch(NumberFormatException e) {
            LOGGER.log(Level.SEVERE, WRAP_LIMIT_KEY + " has invalid value, it should be an integer number but it was: " + wrapLimit);
        }
        WRAP_LIMIT = limit;
    }
    
    /**
     * Programmatically sets the number of wraps per direction the wrapping projection handlers
     * will apply
     * 
     * @param wrapLimit
     */
    public void setWrapLimit(int wrapLimit) {
        ProjectionHandlerFinder.WRAP_LIMIT = wrapLimit;
    }

    /**
     * Returns a projection handler for the specified rendering area, or null if not found
     * @param renderingArea The area to be painted (mind, the CRS must be property set for projection handling to work)
     * @param wrap Enable continuous map wrapping if it's possible for the current projection
     */
    public static ProjectionHandler getHandler(ReferencedEnvelope renderingArea, boolean wrap) {
        if (renderingArea.getCoordinateReferenceSystem() == null)
            return null;

        for (ProjectionHandlerFactory factory : factories) {
            ProjectionHandler handler = factory.getHandler(renderingArea, wrap, WRAP_LIMIT);
            if (handler != null)
                return handler;
        }

        return null;
    }

}
