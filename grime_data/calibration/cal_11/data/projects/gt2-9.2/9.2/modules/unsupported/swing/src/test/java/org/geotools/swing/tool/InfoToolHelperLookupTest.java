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
 *    (C) 2012, Open Source Geospatial Foundation (OSGeo)
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

import org.geotools.coverage.CoverageFactoryFinder;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.GridCoverageLayer;
import org.geotools.map.Layer;
import org.geotools.swing.testutils.TestDataUtils;

import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Tests for the info tool helper lookup.
 * 
 * @author Michael Bedward
 * @since 8.0
 * @source $URL: $
 * @version $Id: $
 */
public class InfoToolHelperLookupTest {

    @Test
    public void featureLayerHelper() throws Exception {
        Layer layer = TestDataUtils.getPolygonLayer();
        InfoToolHelper helper = InfoToolHelperLookup.getHelper(layer);
        assertNotNull(helper);
        assertEquals(FeatureLayerHelper.class, helper.getClass());
    }
    
    @Test
    public void gridCoverageLayerHelper() throws Exception {
        float[][] data = {
            {1, 2, 3, 4},
            {5, 6, 7, 8}
        };
        
        ReferencedEnvelope env = new ReferencedEnvelope(0, 4, 0, 2, null);
        GridCoverageFactory gcf = CoverageFactoryFinder.getGridCoverageFactory(null);
        GridCoverage2D cov = gcf.create("coverage", data, env);
        Layer layer = new GridCoverageLayer(cov, null);
        
        InfoToolHelper helper = InfoToolHelperLookup.getHelper(layer);
        assertNotNull(helper);
        assertEquals(GridCoverageLayerHelper.class, helper.getClass());
    }
}
