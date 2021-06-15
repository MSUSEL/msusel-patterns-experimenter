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
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.ows.test;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import junit.framework.TestCase;

import org.geotools.data.ows.Layer;
import org.geotools.data.ows.StyleImpl;
import org.geotools.data.ows.WMSCapabilities;
import org.geotools.data.wms.xml.WMSSchema;
import org.geotools.test.TestData;
import org.geotools.xml.DocumentFactory;
import org.geotools.xml.SchemaFactory;
import org.geotools.xml.handlers.DocumentHandler;
import org.geotools.xml.schema.Schema;

/**
 * 
 *
 * @source $URL$
 */
public class LayerInheritanceTest extends TestCase {

    public void testInheritCapabilities() throws Exception {

        File getCaps = TestData.file(this, "inheritCap.xml");
        URL getCapsURL = getCaps.toURI().toURL();

        Map hints = new HashMap();
        hints.put(DocumentHandler.DEFAULT_NAMESPACE_HINT_KEY, WMSSchema.getInstance());
        Object object = DocumentFactory.getInstance(getCapsURL.openStream(), hints, Level.WARNING);

        Schema schema = WMSSchema.getInstance();
        SchemaFactory.getInstance(WMSSchema.NAMESPACE);

        assertTrue("Capabilities failed to parse", object instanceof WMSCapabilities);

        WMSCapabilities capabilities = (WMSCapabilities) object;

        // Get first test layer
        Layer layer = (Layer) capabilities.getLayerList().get(0);
        assertNotNull(layer);
        assertEquals(3,layer.getDimensions().size());
        assertEquals("ISO8601",layer.getDimension("time").getUnits());

        // Get next test layer, it's nested 3 deep
        List<Layer> allLayers = capabilities.getLayerList();
        
        layer = (Layer) allLayers.get(2);
        assertNotNull(layer);
        assertNotNull(layer.getParent());
        assertEquals(3,layer.getDimensions().size());

        // Should be false by default since not specified in layer or ancestors
        assertFalse(layer.isQueryable());
        assertEquals(layer.getTitle(), "Coastlines");

        // Should be 5 total after accumulating all ancestors
        assertEquals(5,layer.getSrs().size());
        assertTrue(layer.getSrs().contains("EPSG:26906"));
        assertTrue(layer.getSrs().contains("EPSG:26905"));
        assertTrue(layer.getSrs().contains("EPSG:4326"));
        assertTrue(layer.getSrs().contains("AUTO:42003"));
        assertTrue(layer.getSrs().contains("AUTO:42005"));

        // 2 total, this layer plus top most layer
        assertEquals(layer.getStyles().size(), 2);
        assertTrue(layer.getStyles().contains(new StyleImpl("TestStyle")));
        assertTrue(layer.getStyles().contains(new StyleImpl("default")));

        // Next test layer, nested 3 deep but different path
        layer = (Layer) capabilities.getLayerList().get(4);
        assertNotNull(layer);
        assertNotNull(layer.getParent());
        assertEquals(3, layer.getDimensions().size());
        assertEquals(1, layer.getExtents().size());
        assertEquals(layer.getExtent("time").getName(), "time");

        // Should be true by default since inherited from parent
        assertEquals(layer.getName(), "RTOPO");
        assertTrue(layer.isQueryable());
    }
}
