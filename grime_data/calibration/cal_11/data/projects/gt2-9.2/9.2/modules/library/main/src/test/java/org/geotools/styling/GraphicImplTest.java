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
package org.geotools.styling;

import java.net.URL;

import junit.framework.TestCase;

/**
 * This test case captures specific problems encountered with the GraphicImpl code.
 *
 *
 *
 *
 * @source $URL$
 */
public class GraphicImplTest extends TestCase {

	/**
	 * Checks if creating a Graphic with an ExternalGraphics works.
	 */
	public void testWithExternalGraphics() throws Exception {
		StyleBuilder sb = new StyleBuilder();
		
        URL urlExternal = getClass().getResource("/data/sld/blob.gif");
        ExternalGraphic extg = sb.createExternalGraphic(urlExternal, "image/svg+xml");
        Graphic graphic = sb.createGraphic(extg, null, null);
        
        
		assertEquals(1, graphic.graphicalSymbols().size());
	}
	
	/**
	 * Checks if the Displacement settings are exported to XML
	 */
	public void testDisplacement() throws Exception {
           StyleBuilder sb = new StyleBuilder();
           
           Graphic graphic;
           {
               graphic = sb.createGraphic();
               Displacement disp = sb.createDisplacement(10.1, -5.5);
               graphic.setDisplacement(disp);
           }
           
           Displacement disp = graphic.getDisplacement();
           assertNotNull(disp);
           
           assertEquals(disp.getDisplacementX().toString(),"10.1");
           assertEquals(disp.getDisplacementY().toString(),"-5.5");
           
	}
}
