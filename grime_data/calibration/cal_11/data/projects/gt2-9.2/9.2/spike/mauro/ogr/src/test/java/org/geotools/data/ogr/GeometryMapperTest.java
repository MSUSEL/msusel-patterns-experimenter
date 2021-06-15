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
package org.geotools.data.ogr;

import static org.geotools.data.ogr.bridj.OgrLibrary.OGR_G_DestroyGeometry;

import java.io.IOException;

import org.bridj.Pointer;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.io.WKTReader;

/**
 * 
 *
 * @source $URL: https://svn.osgeo.org/geotools/trunk/modules/unsupported/ogr/src/test/java/org/geotools/data/ogr/GeometryMapperTest.java $
 */
public class GeometryMapperTest extends TestCaseSupport {
    
	GeometryFactory gf = new GeometryFactory();

    @Override
    protected void setUp() throws Exception {
        GdalInit.init();
    }

    public void testLine() throws Exception {
        checkRoundTrip("LINESTRING(0 0, 10 10)");
    }

    public void testPolygon() throws Exception {
        checkRoundTrip("POLYGON((0 0, 0 10, 10 10, 10 0, 0 0))");
    }
    
    public void testPoint() throws Exception {
        checkRoundTrip("POINT(0 0)");
    }
    
    void checkRoundTrip(String geometryWkt) throws Exception {
        checkRoundTrip(geometryWkt, new GeometryMapper.WKB(gf));
        checkRoundTrip(geometryWkt, new GeometryMapper.WKT(gf));
    }

    void checkRoundTrip(String geometryWkt, GeometryMapper mapper) throws Exception {
        Geometry geometry = new WKTReader().read(geometryWkt);

        // to ogr and back
        Pointer ogrGeometry = mapper.parseGTGeometry(geometry);
        Geometry remapped = mapper.parseOgrGeometry(ogrGeometry);
        OGR_G_DestroyGeometry(ogrGeometry);

        assertEquals(geometry, remapped);
    }

   

}
