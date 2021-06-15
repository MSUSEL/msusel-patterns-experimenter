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
 *    (C) 2002-2011, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.sfs;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Iterator;
import java.util.List;

import org.geotools.data.DataStoreFactorySpi;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.sfs.SFSDataStore;
import org.geotools.data.sfs.SFSDataStoreFactory;
import org.geotools.data.sfs.SFSLayer;
import org.geotools.feature.NameImpl;
import org.geotools.referencing.CRS;
import org.opengis.feature.type.Name;

import com.vividsolutions.jts.geom.Envelope;

/**
 * 
 *
 * @source $URL$
 */
public class SFSDataStoreTest extends OnlineTest {

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /* We are not runing this test
     * Test spi mechanism
     */
    public void testSPI() throws MalformedURLException, IOException {

        DataStoreFinder.scanForPlugins();
        final Iterator<DataStoreFactorySpi> it = DataStoreFinder.getAvailableDataStores();
        DataStoreFactorySpi spi = null;

        while (it.hasNext()) {
        	spi=it.next();
            if ( spi instanceof SFSDataStoreFactory) {
                break;
            }
        }
        /* we did not find it? */
        assertNotNull(spi);

        /* try with params */
        assertTrue(spi.canProcess(createParams()));
    
    }

    /*
     * Setup mock service to run test with URL
     */
    public void testConstructorWithURL() throws Exception {
        if (super.onlineTest("testConstrcutorWithURL")) {

            /* If mock service is available then URL can be tested otherwise see
             * the next test
             */
            SFSDataStoreFactory factory = new SFSDataStoreFactory();
            SFSDataStore ods = (SFSDataStore) factory.createDataStore(createParams());

            process(ods);

        } else {
            return;
        }
    }


    /*
     * This test is just with JSON response from the URL
     */
    public void testConstructorWithJSON() throws Exception {
        String _jsonText = "[{"
                + "   \"name\": \"layerAsia\","
                + "   \"bbox\": [-10,-40,30,80],"
                + "   \"crs\": \"urn:ogc:def:crs:EPSG:4326\","
                + "   \"axisorder\": \"yx\"},"
                + "{"
                + "   \"name\": \"layerAmerica\","
                + "   \"crs\": \"urn:ogc:def:crs:EPSG:32632\" ,"
                + "   \"axisorder\": \"xy\" },"
                + "{"
                + "   \"name\": \"layerEurope\","
                + "   \"bbox\": [15000000,49000000,18000000,52000000],"
                + "   \"crs\": \"urn:ogc:def:crs:EPSG:32632\" "
                + " }]";

        SFSDataStore ods = new SFSDataStore(_jsonText, NAMESPACE);
        process(ods);
    }

    /* This method test for data store contents once it has been constructed*/
    private void process(SFSDataStore ods) throws Exception {

        assertNotNull(ods);

        /* LayerAsia, LayerAmerica and Layer Europe == 3 */
        assertEquals(3, ods.getTypeNames().length);

        List<Name> _layerNames = ods.createTypeNames();

        Iterator<Name> it = _layerNames.iterator();

        /*Test layer Names*/
        assertEquals(new NameImpl(NAMESPACE, "layerAsia"), it.next());
        assertEquals("layerAmerica", it.next().getLocalPart());
        assertEquals("layerEurope", it.next().getLocalPart());

        /*Test CRS String*/
        SFSLayer layerAsia = ods.getLayer(new NameImpl(NAMESPACE, "layerAsia"));
        assertEquals("urn:ogc:def:crs:EPSG:4326", layerAsia.getLayerSRS());
        assertEquals(false, layerAsia.isXYOrder());
        assertEquals(CRS.decode("EPSG:4326", true), layerAsia.getCoordinateReferenceSystem());
        assertEquals(new Envelope(-40, 80, -10, 30), layerAsia.getBounds());
        
        SFSLayer layerAmerica = ods.getLayer(new NameImpl(NAMESPACE, "layerAmerica"));
        assertEquals("urn:ogc:def:crs:EPSG:32632", layerAmerica.getLayerSRS());
        assertEquals(true, layerAmerica.isXYOrder());
        assertEquals(CRS.decode("EPSG:32632", true), layerAmerica.getCoordinateReferenceSystem());
        assertNull(layerAmerica.getBounds());
        
        SFSLayer layerEurope = ods.getLayer(new NameImpl(NAMESPACE, "layerEurope"));
        assertEquals("urn:ogc:def:crs:EPSG:32632", layerEurope.getLayerSRS());
        assertEquals(true, layerEurope.isXYOrder());
        assertEquals(CRS.decode("EPSG:32632", true), layerEurope.getCoordinateReferenceSystem());
        assertEquals(new Envelope(15000000, 18000000, 49000000, 52000000), layerEurope.getBounds());
    }
}
