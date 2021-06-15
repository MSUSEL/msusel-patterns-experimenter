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
 *    (C) 2002-2009, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.arcsde.util;

import static org.junit.Assert.assertSame;

import java.io.File;
import java.io.PrintWriter;
import java.util.Map;
import java.util.TreeMap;

import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultEngineeringCRS;
import org.junit.Test;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.esri.sde.sdk.client.SeCoordinateReference;
import com.esri.sde.sdk.client.SeObjectId;
import com.esri.sde.sdk.pe.PeFactory;
import com.esri.sde.sdk.pe.PeGeographicCS;
import com.esri.sde.sdk.pe.PeProjectedCS;
import com.esri.sde.sdk.pe.PeProjectionException;

/**
 * 
 *
 * @source $URL$
 */
public class ArcSDEUtilsTest {
    @Test
    public void testFindCompatibleCRS_Projected() throws Exception {

        SeCoordinateReference seCoordRefSys = new SeCoordinateReference();
        seCoordRefSys.setCoordSysByID(new SeObjectId(23030));

        CoordinateReferenceSystem expectedCRS = CRS.decode("EPSG:23030");
        CoordinateReferenceSystem compatibleCRS = ArcSDEUtils.findCompatibleCRS(seCoordRefSys);

        assertSame(expectedCRS, compatibleCRS);
    }

    @Test
    public void testFindCompatibleCRS_Geographic() throws Exception {
        SeCoordinateReference seCoordRefSys = new SeCoordinateReference();
        seCoordRefSys.setCoordSysByID(new SeObjectId(4326));

        CoordinateReferenceSystem expectedCRS = CRS.decode("EPSG:4326");
        CoordinateReferenceSystem compatibleCRS = ArcSDEUtils.findCompatibleCRS(seCoordRefSys);

        assertSame(expectedCRS, compatibleCRS);
    }

    @Test
    public void testFindCompatibleCRS_Null() throws Exception {
        CoordinateReferenceSystem compatibleCRS = ArcSDEUtils.findCompatibleCRS(null);

        assertSame(DefaultEngineeringCRS.CARTESIAN_2D, compatibleCRS);

        SeCoordinateReference seCoordRefSys = new SeCoordinateReference();

        compatibleCRS = ArcSDEUtils.findCompatibleCRS(seCoordRefSys);

        assertSame(DefaultEngineeringCRS.CARTESIAN_2D, compatibleCRS);
    }

    public static void main(String argv[]) {
        try {
            int[] projcsCodelist = PeFactory.projcsCodelist();
            int[] geogtranCodelist = PeFactory.geogcsCodelist();
            Map<Integer, String> coordsystems = new TreeMap<Integer, String>();
            for (int i : projcsCodelist) {
                PeProjectedCS coordsys = PeFactory.projcs(i);
                if (coordsys != null) {
                    coordsystems.put(i, coordsys.toString());
                } else {
                    System.err.println("No PeProjectedCS found for code " + i);
                }
            }
            for (int i : geogtranCodelist) {
                PeGeographicCS coordsys = PeFactory.geogcs(i);
                if (coordsys != null) {
                    coordsystems.put(i, coordsys.toString());
                } else {
                    System.err.println("No PeGeographicCS found for code " + i);
                }
            }

            PrintWriter p = new PrintWriter(new File("/Users/groldan/esri.properties"));
            for (Map.Entry<Integer, String> e : coordsystems.entrySet()) {
                p.print(e.getKey());
                p.print("=");
                p.println(e.getValue());
            }
            p.flush();
            p.close();

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (PeProjectionException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
