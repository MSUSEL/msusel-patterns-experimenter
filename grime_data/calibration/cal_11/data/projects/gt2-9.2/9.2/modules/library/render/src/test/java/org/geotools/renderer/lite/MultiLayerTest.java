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

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.data.property.PropertyDataStore;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.FeatureLayer;
import org.geotools.map.GridCoverageLayer;
import org.geotools.map.MapContent;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.styling.Style;
import org.geotools.styling.StyleBuilder;
import org.geotools.test.TestData;
import org.junit.Test;

/**
 * 
 *
 * @source $URL$
 */
public class MultiLayerTest {

	@Test
	public void testRasterOpacity() throws Exception {
		// a polygon layer
        File property = new File(TestData.getResource(this, "buildings.properties").toURI());
        PropertyDataStore ds = new PropertyDataStore(property.getParentFile());
        SimpleFeatureSource fs = ds.getFeatureSource("buildings");
        ReferencedEnvelope bounds = new ReferencedEnvelope(0, 10, 0, 10, DefaultGeographicCRS.WGS84);

        StyleBuilder sb = new StyleBuilder();
        Style pst = sb.createStyle(sb.createPolygonSymbolizer(null, sb.createFill(Color.GRAY, 0.5)));
        
        // a raster layer
        BufferedImage bi = new BufferedImage(300, 300, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics g = bi.getGraphics();
        g.setColor(Color.RED);
        g.fillRect(0, 0, 300, 300);
        g.dispose();
        GridCoverage2D coverage = new GridCoverageFactory().create("test_red", bi, bounds);
        
        Style rst = sb.createStyle(sb.createRasterSymbolizer());
		
		MapContent mc = new MapContent();
		mc.addLayer(new FeatureLayer(fs, pst));
		mc.addLayer(new GridCoverageLayer(coverage, rst));
		
		StreamingRenderer renderer = new StreamingRenderer();
		renderer.setMapContent(mc);
		BufferedImage img = RendererBaseTest.renderImage(renderer, bounds, null);
		
		// check the red image fully covered the vector (GEOT-3812)
		int[] pixel = new int[4];
		img.getData().getPixel(100, 100, pixel);
		assertEquals(255, pixel[0]);
		assertEquals(0, pixel[1]);
		assertEquals(0, pixel[2]);
		assertEquals(255, pixel[3]);
	}
}
