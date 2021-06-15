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
package org.geotools.swing.tool;

import java.awt.Rectangle;
import java.lang.ref.WeakReference;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.geometry.DirectPosition2D;
import org.geotools.map.GridReaderLayer;
import org.geotools.map.Layer;
import org.geotools.parameter.Parameter;
import org.geotools.resources.geometry.XRectangle2D;
import org.opengis.coverage.PointOutsideCoverageException;
import org.opengis.coverage.grid.GridEnvelope;
import org.opengis.geometry.DirectPosition;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.referencing.datum.PixelInCell;
import org.opengis.referencing.operation.MathTransform;


/**
 * Helper class used by {@linkplain InfoTool} to query values in a
 * {@linkplain org.geotools.map.GridReaderLayer}.
 *
 * @author Michael Bedward
 * @since 8.0
 *
 * @source $URL$
 * @version $URL$
 */
public class GridReaderLayerHelper extends InfoToolHelper {

    private static final int CACHED_RASTER_WIDTH = 20;
    private WeakReference<AbstractGridCoverage2DReader> sourceRef;
    private GridCoverage2D cachedCoverage;

    public GridReaderLayerHelper() {
    }

    @Override
    public boolean isSupportedLayer(Layer layer) {
        return (layer instanceof GridReaderLayer);
    }

    @Override
    public void setLayer(Layer layer) {
        if (!(layer instanceof GridReaderLayer)) {
            throw new IllegalArgumentException("layer must be an instance of GridReaderLayer");
        }

        super.setLayer(layer);
        sourceRef = new WeakReference<AbstractGridCoverage2DReader>(
                ((GridReaderLayer) layer).getReader());
    }
    
    @Override
    public boolean isValid() {
        return super.isValid() && sourceRef != null && sourceRef.get() != null;
    }

    @Override
    public InfoToolResult getInfo(DirectPosition2D pos) throws Exception {
        InfoToolResult result = new InfoToolResult();

        if (isValid()) {
            DirectPosition trPos =
                    InfoToolHelperUtils.getTransformed(pos, getContentToLayerTransform());

            if (cachedCoverage == null || !cachedCoverage.getEnvelope2D().contains(trPos)) {
                if (!renewCachedCoverage(trPos)) {
                    return result;
                }
            }

            try {
                Object objArray = cachedCoverage.evaluate(trPos);
                Number[] bandValues = InfoToolHelperUtils.asNumberArray(objArray);

                if (bandValues != null) {
                    result.newFeature("Raw values");
                    for (int i = 0; i < bandValues.length; i++) {
                        result.setFeatureValue("Band " + i, bandValues[i]);
                    }
                }

            } catch (PointOutsideCoverageException e) {
                // The empty result will be returned
            }
        }

        return result;
    }

    private boolean renewCachedCoverage(DirectPosition centrePos) {
        final Rectangle queryRect = createQueryGridEnvelope(centrePos);
        if (queryRect.isEmpty()) {
            return false;
        }

        final AbstractGridCoverage2DReader reader = sourceRef.get();
        GeneralParameterValue parameter = new Parameter(
                AbstractGridFormat.READ_GRIDGEOMETRY2D,
                new GridGeometry2D(new GridEnvelope2D(queryRect),
                reader.getOriginalGridToWorld(PixelInCell.CELL_CENTER),
                reader.getCrs()));
        
        try {
            cachedCoverage = (GridCoverage2D) reader.read(new GeneralParameterValue[]{parameter});
            return cachedCoverage != null;
            
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private Rectangle createQueryGridEnvelope(DirectPosition pos) {
        final AbstractGridCoverage2DReader reader = sourceRef.get();
        try {
            MathTransform worldToGridTransform =
                    reader.getOriginalGridToWorld(PixelInCell.CELL_CORNER).inverse();

            DirectPosition midPos = worldToGridTransform.transform(pos, null);
            int x = (int) midPos.getOrdinate(0);
            int y = (int) midPos.getOrdinate(1);
            int halfWidth = CACHED_RASTER_WIDTH / 2;

            final Rectangle queryRect = new Rectangle(
                    x - halfWidth, y - halfWidth,
                    CACHED_RASTER_WIDTH, CACHED_RASTER_WIDTH);

            GridEnvelope gridEnv = reader.getOriginalGridRange();
                Rectangle rect = new Rectangle(
                        gridEnv.getLow(0), gridEnv.getLow(1),
                        gridEnv.getSpan(0), gridEnv.getSpan(1));

            XRectangle2D.intersect(queryRect, rect, queryRect);
            return queryRect;

        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

}
