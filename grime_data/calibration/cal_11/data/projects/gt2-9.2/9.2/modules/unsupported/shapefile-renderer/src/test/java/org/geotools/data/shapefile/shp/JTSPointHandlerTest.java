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
package org.geotools.data.shapefile.shp;

import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.net.URL;

import junit.framework.TestCase;

import org.geotools.TestData;
import org.geotools.data.Query;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.data.shapefile.ShapefileRendererUtil;
import org.geotools.referencing.CRS;
import org.geotools.referencing.ReferencingFactoryFinder;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.referencing.operation.matrix.GeneralMatrix;
import org.geotools.renderer.lite.RendererUtilities;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;

/**
 * @TODO class description
 * 
 * @author jeichar
 * @since 2.1.x
 *
 *
 *
 * @source $URL$
 */
public class JTSPointHandlerTest extends TestCase {

    public void testRead() throws Exception {
        URL url = TestData.url("shapes/pointtest.shp");
        ShapefileDataStore ds = (ShapefileDataStore) new ShapefileDataStoreFactory()
                .createDataStore(url);

        Envelope env = ds.getFeatureSource().getBounds();
        CoordinateReferenceSystem crs = DefaultGeographicCRS.WGS84;
        MathTransform mt = CRS.findMathTransform(crs,
                DefaultGeographicCRS.WGS84);

        Rectangle rectangle = new Rectangle(300,0,300, 300);
        AffineTransform transform = RendererUtilities.worldToScreenTransform(
                env, rectangle);
        GeneralMatrix matrix = new GeneralMatrix(transform);
        MathTransform at = ReferencingFactoryFinder.getMathTransformFactory(null)
                .createAffineTransform(matrix);
        mt = ReferencingFactoryFinder.getMathTransformFactory(null)
                .createConcatenatedTransform(mt, at);

        ShapefileReader reader=new ShapefileReader(ShapefileRendererUtil.getShpFiles(ds), false, false);
        reader.setHandler(new org.geotools.renderer.shape.shapehandler.jts.PointHandler(reader.getHeader().getShapeType(),
                env, rectangle, mt, false));

        Object shape = reader.nextRecord().shape();
        assertNotNull(shape);
        assertTrue(shape instanceof Geometry);
        Coordinate[] coords = ((Geometry)shape).getCoordinates();
        for (int i = 0; i < coords.length; i++) {
            Coordinate coordinate = coords[i];
            assertNotNull(coordinate);
        }

        int i = 0;
        while (reader.hasNext()) {
            i++;
            shape = reader.nextRecord().shape();
            assertNotNull(shape);
            assertTrue(shape instanceof Geometry);
        }
        assertEquals(ds.getFeatureSource().getCount(Query.ALL) - 1, i);
    }

}
