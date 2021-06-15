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
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
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
 *
 */
package org.geotools.coverageio.jp2k;

import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.logging.Logger;

import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.coverageio.jp2k.Granule;
import org.geotools.coverageio.jp2k.JP2KReader;
import org.geotools.coverageio.jp2k.Granule.Level;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.test.TestData;
import org.junit.Test;

/**
 * @author Daniele Romagnoli, GeoSolutions
 * @author Simone Giannecchini (simboss), GeoSolutions
 *
 * Testing {@link Granule}
 *
 *
 *
 * @source $URL$
 */
public final class GranuleTest extends BaseJP2K {

	private final static Logger LOGGER = org.geotools.util.logging.Logging.getLogger(GranuleTest.class);
    /**
     * Creates a new instance of GranuleTest
     *
     * @param name
     */
    public GranuleTest() {
    }

    @Test
    public void test() throws Exception {
    	if (!testingEnabled()) {
            return;
        }
    	File file = null;
        try{
             file = TestData.file(this, "sample.jp2");
         }catch (FileNotFoundException fnfe){
             LOGGER.warning("test-data not found: sample.jp2 \nTests are skipped");
             return;
        }
         
		final AbstractGridCoverage2DReader reader = new JP2KReader(file);
		final GeneralEnvelope envelope = reader.getOriginalEnvelope();
		final Granule granule = new Granule(new ReferencedEnvelope(envelope), file);
		final Level level = granule.getLevel(0);
		if (level != null){
			final AffineTransform btl = level.getBaseToLevelTransform();
	    	assertTrue(btl.isIdentity());
	    	final Rectangle bounds = level.getBounds();
	    	assertEquals(bounds.width, 400);
	        assertEquals(bounds.height, 200);
	    	final int h = level.getHeight();
	    	final int w = level.getWidth();
	    	assertEquals(bounds.width, w);
	        assertEquals(bounds.height, h);
	        
	    	final double sx = level.getScaleX();
	    	final double sy = level.getScaleY();
	    	assertEquals(sx, 1.0, DELTA);
	        assertEquals(sy, 1.0, DELTA);
	         
	    	final String levelS = level.toString();
	    	if (TestData.isInteractiveTest()){
	         	if (LOGGER.isLoggable(java.util.logging.Level.INFO))
	         		LOGGER.info(levelS);
	        }
	    }
    	
    	final String granuleS = granule.toString();
    	if (TestData.isInteractiveTest()){
         	if (LOGGER.isLoggable(java.util.logging.Level.INFO))
         		LOGGER.info(granuleS);
        }
    	
    }
}
