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
package org.geotools.renderer.lite;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.geotools.data.property.PropertyDataStore;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.DefaultMapContext;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.renderer.RenderListener;
import org.geotools.styling.Style;
import org.geotools.test.TestData;
import org.opengis.feature.simple.SimpleFeature;

/**
 * Tests the optimized data loading does merge the filters properly (was never released,
 * but a certain point in time only the first one was passed down to the datastore) 
 *
 *
 *
 * @source $URL$
 */
public class QueryOptimizeTest extends TestCase {
    
    private static final long TIME = 2000;
    
    SimpleFeatureSource squareFS;
    ReferencedEnvelope bounds;
    StreamingRenderer renderer;
    DefaultMapContext context;
    int count = 0;
    

    @Override
    protected void setUp() throws Exception {
        // setup data
        File property = new File(TestData.getResource(this, "square.properties").toURI());
        PropertyDataStore ds = new PropertyDataStore(property.getParentFile());
        squareFS = ds.getFeatureSource("square");
        bounds = new ReferencedEnvelope(0, 10, 0, 10, DefaultGeographicCRS.WGS84);
        
        renderer = new StreamingRenderer();
        context = new DefaultMapContext(DefaultGeographicCRS.WGS84);
        renderer.setContext(context);
        Map hints = new HashMap();
        hints.put("maxFiltersToSendToDatastore", 2);
        hints.put("optimizedDataLoadingEnabled", true);
        renderer.setRendererHints(hints);
                
//        System.setProperty("org.geotools.test.interactive", "true");
    }

    
    
    public void testLessFilters() throws Exception {
        Style style = RendererBaseTest.loadStyle(this, "fillSolidTwoRules.sld");
        
        DefaultMapContext mc = new DefaultMapContext(DefaultGeographicCRS.WGS84);
        mc.addLayer(squareFS, style);
        
        renderer.setContext(mc);
        renderer.addRenderListener(new RenderListener() {
        
            public void featureRenderer(SimpleFeature feature) {
                count++;
            }
        
            public void errorOccurred(Exception e) {
            }
        });
        
        RendererBaseTest.showRender("OneSquare", renderer, TIME, bounds);
        assertEquals(2, count);
    }
}

