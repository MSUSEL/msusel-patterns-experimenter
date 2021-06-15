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
package org.geotools.renderer.shape;

import java.io.IOException;

import junit.framework.TestCase;

import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.map.DefaultMapContext;
import org.geotools.map.MapContext;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.styling.SLDParser;
import org.geotools.styling.Style;
import org.geotools.styling.StyleFactory;
import org.geotools.styling.StyleFactoryFinder;
import org.geotools.test.TestData;

import com.vividsolutions.jts.geom.Envelope;


/**
 * Tests the LiteRenderer labelling algorithms
 *
 * @author jeichar
 *
 * @since 0.9.0
 *
 *
 *
 * @source $URL$
 */
public class LabelingTest extends TestCase {
    private static final int CENTERX = 160;
    private static final int CENTERY = 40;
    private static final boolean INTERACTIVE = false;
    private long timout = 1000;

    static Style loadStyle(String sldFilename) throws IOException {
        StyleFactory factory = StyleFactoryFinder.createStyleFactory();

        java.net.URL surl = TestData.getResource(LabelingTest.class, sldFilename);
        SLDParser stylereader = new SLDParser(factory, surl);

        Style style = stylereader.readXML()[0];

        return style;
    }

    public void testLineLabeling() throws Exception {
        ShapefileDataStore ds = (ShapefileDataStore) TestUtilites.getDataStore(
                "theme1.shp");
        Style style = loadStyle("LineStyle.sld");
        assertNotNull(style);

        MapContext map = new DefaultMapContext(DefaultGeographicCRS.WGS84);
        map.addLayer(ds.getFeatureSource(), style);

        ShapefileRenderer renderer = new ShapefileRenderer(map);
        Envelope env = map.getLayerBounds();
        int boundary = 10;
        TestUtilites.INTERACTIVE = INTERACTIVE;
        env = new Envelope(env.getMinX() - boundary, env.getMaxX() + boundary,
                env.getMinY() - boundary, env.getMaxY() + boundary);
        
        TestUtilites.showRender("testLineLabeling", renderer, timout, env );
    }

    public void testPolyLabeling() throws Exception {
        ShapefileDataStore ds = (ShapefileDataStore) TestUtilites.getDataStore(
                "smallMultiPoly.shp");

        Style style = loadStyle("PolyStyle.sld");
        assertNotNull(style);

        MapContext map = new DefaultMapContext(DefaultGeographicCRS.WGS84);
        map.addLayer(ds.getFeatureSource(), style);

        ShapefileRenderer renderer = new ShapefileRenderer(map);
        Envelope env = map.getLayerBounds();
        int boundary = 1;
        env = new Envelope(env.getMinX() - boundary, env.getMaxX() + boundary,
                env.getMinY() - boundary, env.getMaxY() + boundary);
        TestUtilites.INTERACTIVE = INTERACTIVE;
        TestUtilites.showRender("testPolyLabeling", renderer, timout, env);
    }

    public void testPolyLabelingZoomedOut() throws Exception {
        ShapefileDataStore ds = (ShapefileDataStore) TestUtilites.getDataStore(
                "smallMultiPoly.shp");
        SimpleFeatureSource source = ds.getFeatureSource(ds.getTypeNames()[0]);

        Style style = loadStyle("PolyStyle.sld");
        assertNotNull(style);

        MapContext map = new DefaultMapContext(DefaultGeographicCRS.WGS84);
        map.addLayer(ds.getFeatureSource(), style);

        ShapefileRenderer renderer = new ShapefileRenderer(map);
        Envelope env = map.getLayerBounds();
        int boundary = 30;
        env = new Envelope(env.getMinX() - boundary, env.getMaxX() + boundary,
                env.getMinY() - boundary, env.getMaxY() + boundary);
        TestUtilites.INTERACTIVE = INTERACTIVE;
        TestUtilites.showRender("testPolyLabeling", renderer, timout, env);
    }
}
