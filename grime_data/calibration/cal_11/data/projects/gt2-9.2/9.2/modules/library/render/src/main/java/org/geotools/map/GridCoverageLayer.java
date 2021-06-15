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
 *    (C) 2003-2011, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.map;

import java.util.logging.Level;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.factory.FactoryRegistryException;
import org.geotools.feature.SchemaException;
import org.geotools.geometry.Envelope2D;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.resources.coverage.FeatureUtilities;
import org.geotools.styling.Style;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.TransformException;
/**
 * Layer used to draw a raster {@link GridCoverage}.
 * <p>
 * Direct access to the {@link GridCoverage} is available using {@link #getCoverage()}, the
 * outline of the raster is also available via {@link #toFeatureCollection()} for vector based
 * rendering systems.
 * @author Jody Garnett
 * @version 8.0
 * @since 2.7
 *
 * @source $URL$
 */
public class GridCoverageLayer extends RasterLayer {
    /**
     * Grid Coverage to be drawn.
     */
    protected GridCoverage2D coverage;

    /**
     * Create layer to draw the provided grid coverage.
     * 
     * @param coverage
     *            The new layer that has been added.
     * @param style
     * @throws SchemaException
     * @throws FactoryRegistryException
     * @throws TransformException
     */
    public GridCoverageLayer(GridCoverage2D coverage, Style style) {
        super(style);
        this.coverage = coverage;
    }
    /**
     * Create layer to draw the provided grid coverage.
     * @param coverage
     * @param style
     * @param title
     */
    public GridCoverageLayer(GridCoverage2D coverage, Style style, String title) {
        super(style,title);
        this.coverage = coverage;
    }

    @Override
    public void dispose() {
        preDispose();
        if( coverage != null ){
            try{
                coverage.dispose(true);
            }catch (Exception e) {
                // eat me
            }            
            coverage = null;
        }
        if( style != null ){
            style = null;
        }
        super.dispose();
    }

    /**
     * Access to the grid coverage being drawn.
     * @return grid coverage being drawn.
     */
    public GridCoverage2D getCoverage() {
        return coverage;
    }
    /**
     * Layer bounds generated from the grid coverage.
     * 
     * @return layer bounds generated from the grid coverage.
     */
    public ReferencedEnvelope getBounds() {
        if (coverage != null) {
            CoordinateReferenceSystem crs = coverage.getCoordinateReferenceSystem();
            Envelope2D bounds = coverage.getEnvelope2D();
            if (bounds != null) {
                return new ReferencedEnvelope(bounds);
            } else if (crs != null) {
                return new ReferencedEnvelope(crs);
            }
        }
        return null;
    }
    
    public SimpleFeatureCollection toFeatureCollection() {
        SimpleFeatureCollection collection;
        try {
            collection = FeatureUtilities.wrapGridCoverage(coverage);
            return collection;
        } catch (Exception e) {
            LOGGER.log(Level.FINER, "Coverage could not be converted to FeatureCollection", e);
            return null;
        }
    }
}
