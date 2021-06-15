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
package org.geotools.map;

import java.io.IOException;

import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * 
 *
 * @source $URL$
 */
public class DefaultMapContextTest {
    @Test
    public void testDispose() {
        DefaultMapContext mapContext = new DefaultMapContext();
        mapContext.dispose();

        mapContext = new DefaultMapContext(DefaultGeographicCRS.WGS84);
        mapContext.dispose();

    }

    /**
     * Test DefaultMapContext handles layers that return null bounds.
     */
    @Test
    public void testNPELayerBounds() throws IOException {

        MapLayer mapLayerBoundsNull = new MapLayer(new Layer() {
            public ReferencedEnvelope getBounds() {
                return null;
            }
        });
        DefaultMapContext mapContext = new DefaultMapContext(DefaultGeographicCRS.WGS84);
        mapContext.addLayer(mapLayerBoundsNull);
        ReferencedEnvelope layerBounds = mapContext.getLayerBounds();
        assertNull(layerBounds);
        
        ReferencedEnvelope maxBounds = mapContext.getMaxBounds();
        assertNotNull(maxBounds);
        assertEquals( DefaultGeographicCRS.WGS84, maxBounds.getCoordinateReferenceSystem() );
        assertTrue( maxBounds.isEmpty() );
        
    }
}
