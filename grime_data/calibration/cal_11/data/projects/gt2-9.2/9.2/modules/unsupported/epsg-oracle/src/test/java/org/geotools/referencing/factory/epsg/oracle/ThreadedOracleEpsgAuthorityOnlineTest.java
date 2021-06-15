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
 */
package org.geotools.referencing.factory.epsg.oracle;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.geotools.factory.GeoTools;
import org.geotools.referencing.CRS;
import org.geotools.referencing.factory.epsg.ThreadedOracleEpsgFactory;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.datum.GeodeticDatum;

/**
 * This one tests Factory<b>On</b>OracleSQL - ie it has a buffer and delegates to a
 * OracleDialectEpsgFactory when the buffer needs to be fed.
 * 
 * @author Jody
 *
 *
 *
 *
 * @source $URL$
 */
public class ThreadedOracleEpsgAuthorityOnlineTest extends OracleOnlineTestCase {

    public void testWSG84() throws Exception {
        CoordinateReferenceSystem crs = CRS.decode("EPSG:4326");
        assertNotNull(crs);
    }
    public void testJNDIConfiguredProperlyForTest() throws Exception {
        InitialContext context = GeoTools.getInitialContext(null);
        String name = "jdbc/EPSG";
        //name = GeoTools.fixName(context,"jdbc/EPSG");        
        DataSource source = (DataSource) context.lookup( name);
        assertNotNull(source);
        assertSame(source, this.datasource);
    }
    /**
     * It is a little hard to test this thing, the DefaultAuthorityFactory holds a field "buffered"
     * that is an AbstractAuthorityFactory which in turn is an FactoryUsing
     * 
     * @throws Exception
     */
    public void testCRSCreation() throws Exception {
        ThreadedOracleEpsgFactory oracle = new ThreadedOracleEpsgFactory();

        
        CoordinateReferenceSystem crs = oracle.createCoordinateReferenceSystem("4326");
        assertNotNull(crs);
    }
    
    public void testDatumCreation() throws Exception {
        ThreadedOracleEpsgFactory oracle = new ThreadedOracleEpsgFactory();
                
        GeodeticDatum datum = oracle.createGeodeticDatum("6326");
        assertNotNull( datum );
    }
}
