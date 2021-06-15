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
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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

import org.geotools.factory.Hints;
import org.geotools.referencing.factory.AbstractCachedAuthorityFactory;
import org.geotools.referencing.factory.AbstractEpsgMediator;
import org.opengis.referencing.FactoryException;

/**
 * Mediator which delegates the creation of referencing objects to the
 * OracleDialectEpsgFactory.
 * 
 * @author Cory Horner (Refractions Research)
 *
 *
 *
 *
 * @source $URL$
 */
public class OracleDialectEpsgMediator extends AbstractEpsgMediator {

    Hints config;
    
    /**
     * No argument constructor - must not fail for factory finder registration.
     */
    public OracleDialectEpsgMediator() {
    }

    
    public OracleDialectEpsgMediator(Hints hints ) throws FactoryException {
        super(hints);
        config = hints;
    }
    public OracleDialectEpsgMediator(int priority, Hints hints, DataSource datasource) {
        super(hints, datasource);
        config = hints;
    }
    
    /**
     * Creates an OracleDialectEpsgMediator with a 60 second timeout, two workers,
     * and no cache.
     * 
     * @param priority
     * @param datasource
     */
    public OracleDialectEpsgMediator(int priority, DataSource datasource) {
        this(priority, 
             new Hints(Hints.AUTHORITY_MAX_ACTIVE, 
                 new Integer(2),
                 new Object[] {
                     Hints.AUTHORITY_MIN_EVICT_IDLETIME, new Integer(1 * 60 * 1000),
                     Hints.CACHE_POLICY, "none",
                     Hints.EPSG_DATA_SOURCE, datasource
                 }
             ),
             datasource
         );
        config = new Hints( Hints.EPSG_DATA_SOURCE, datasource );
    }


    /**
     * Reinitialize an instance to be returned by the pool.
     */
    protected void activateWorker(AbstractCachedAuthorityFactory obj) throws Exception {
        OracleDialectEpsgFactory factory = (OracleDialectEpsgFactory) obj;
        factory.connect();
    }

    /**
     * Destroys an instance no longer needed by the pool.
     */
    protected void destroyWorker(AbstractCachedAuthorityFactory obj) throws Exception {
        OracleDialectEpsgFactory factory = (OracleDialectEpsgFactory) obj;
        factory.disconnect();
        factory.dispose();
    }

    /**
     * Creates an instance that can be returned by the pool.
     */
    protected AbstractCachedAuthorityFactory makeWorker() throws Exception {
        OracleDialectEpsgFactory factory = new OracleDialectEpsgFactory( config, datasource);
        return factory;
    }

    /**
     * Uninitialized an instance to be returned to the pool.
     */
    protected void passivateWorker(AbstractCachedAuthorityFactory obj) throws Exception {
        // Each implementation has the choice of closing connections when they
        // are returned to the worker pool, or when the objects are destroyed.
        // In this implementation, we have chosen to keep connections open
        // during their idle time and close the connection when the worker is
        // evicted. If we wanted to change this, we would move the disconnect
        // statement to this method. Alternatively, we could also keep track
        // of the idle time, and configure the pool to validate idle workers
        // frequently. We would then do a check in the validateWorker method
        // to close the connection when a connection close threshold is hit,
        // prior to worker destruction.
    }

    /**
     * Ensures that the instance is safe to be returned by the pool.
     */
    protected boolean validateWorker(AbstractCachedAuthorityFactory obj) {
        return true;
    }

}
