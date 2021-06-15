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
package org.geotools.referencing.factory.epsg;

import javax.sql.DataSource;

import junit.framework.TestCase;
import net.sourceforge.groboutils.junit.v1.MultiThreadedTestRunner;
import net.sourceforge.groboutils.junit.v1.TestRunnable;

import org.apache.commons.dbcp.BasicDataSource;
import org.geotools.factory.Hints;
import org.geotools.referencing.factory.epsg.HsqlDialectEpsgMediatorStressTest.ClientThread;

/**
 * Multi-threaded test to check that no connections are leaked by the EPSG
 * mediator/factory code.
 * 
 * @author Cory Horner (Refractions Research)
 *
 *
 *
 * @source $URL$
 */
public class HsqlDialectEpsgMediatorConnectionLeakTest extends TestCase {

    final static int RUNNER_COUNT = 3;
    final static int ITERATIONS = 3;
    final static int MAX_TIME = 2 * 60 * 1000;
    final static int MAX_WORKERS = 2;
    final static boolean VERBOSE = false;
    
    HsqlDialectEpsgMediator mediator;
    BasicDataSource datasource;
    //DataSourceWrapper datasource;
    String[] codes;
    Hints hints;
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        hints = new Hints(Hints.CACHE_POLICY, "none");
        hints.put(Hints.AUTHORITY_MAX_ACTIVE, Integer.valueOf(MAX_WORKERS));

        final DataSource database = HsqlEpsgDatabase.createDataSource();
        datasource = new BasicDataSource(){
        	{
        		this.dataSource = database;
        	}        	
        };
        mediator = new HsqlDialectEpsgMediator(80, hints, datasource);
        codes = HsqlDialectEpsgMediatorStressTest.getCodes();
    }

    public void testLeak() throws Throwable {
        TestRunnable runners[] = new TestRunnable[RUNNER_COUNT];
        for (int i = 0; i < RUNNER_COUNT; i++) {
            ClientThread thread = new HsqlDialectEpsgMediatorStressTest.ClientThread(i, mediator); 
            thread.iterations = ITERATIONS;
            runners[i] = thread;
        }
        MultiThreadedTestRunner mttr = new MultiThreadedTestRunner(runners, null);
        mttr.runTestRunnables(MAX_TIME);
        
        //count exceptions and metrics
        int exceptions = 0;
        for (int i = 0; i < RUNNER_COUNT; i++) {
            ClientThread thread = (ClientThread) runners[i];
            exceptions += thread.exceptions;
        }
        //destroy the mediator, check for open connections or exceptions
        mediator.dispose();
        assertEquals(0, datasource.getNumActive());
        assertEquals(0, exceptions);
    }
    
}
