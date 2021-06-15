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
package org.geotools.renderer.style;

import java.net.URL;

import junit.framework.TestCase;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.renderer.lite.OpacityFinder;
import org.geotools.styling.RasterSymbolizer;
import org.geotools.styling.SLDParser;
import org.geotools.styling.Style;
import org.geotools.styling.StyleFactory;
import org.geotools.test.TestData;


public class OpacityFinderTest extends TestCase {

    public void testRasterOpacity() throws Exception {
        StyleFactory factory = CommonFactoryFinder.getStyleFactory(null);
        URL styleURL = TestData.getResource(this, "raster.sld");

        SLDParser stylereader = new SLDParser(factory, styleURL);

        Style style = stylereader.readXML()[0];

        OpacityFinder opacityFinder = new OpacityFinder(new Class[] { RasterSymbolizer.class });

        style.accept(opacityFinder);

        org.junit.Assert.assertTrue(opacityFinder.hasOpacity);
    }
    
    public void testColorMapOpacity() throws Exception {
        StyleFactory factory = CommonFactoryFinder.getStyleFactory(null);
        URL styleURL = TestData.getResource(this, "raster-cmalpha.sld");

        SLDParser stylereader = new SLDParser(factory, styleURL);

        Style style = stylereader.readXML()[0];

        OpacityFinder opacityFinder = new OpacityFinder(new Class[] { RasterSymbolizer.class });

        style.accept(opacityFinder);

        org.junit.Assert.assertTrue(opacityFinder.hasOpacity);
    }
}
