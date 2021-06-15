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

import org.geotools.factory.GeoTools;
import org.geotools.factory.Hints;
import org.geotools.referencing.factory.AbstractCachedAuthorityFactory;
import org.geotools.referencing.factory.AbstractEpsgMediator;
import org.opengis.referencing.FactoryException;

/**
 * Mediator which delegates the creation of referencing objects to the
 * HsqlDialectEpsgFactory.
 * 
 * @author Cory Horner (Refractions Research)
 *
 *
 *
 * @source $URL$
 */
public class HsqlDialectEpsgMediator extends AbstractEpsgMediator {

    Hints hints;
    
    /**
     * Creates a new instance of this factory.
     */
    public HsqlDialectEpsgMediator() throws FactoryException {
        this( GeoTools.getDefaultHints() );
    }
    
    /**
     * Creates a new instance of this data source using the specified hints. The priority
     * is set to a lower value than the {@linkplain FactoryOnAccess}'s one in order to give
     * precedence to the Access-backed database, if presents. Priorities are set that way
     * because:
     * <ul>
     *   <li>The MS-Access format is the primary EPSG database format.</li>
     *   <li>If a user downloads the MS-Access database himself, he probably wants to use it.</li>
     * </ul>
     */
    public HsqlDialectEpsgMediator(final Hints hints) throws FactoryException {
        super(hints, HsqlEpsgDatabase.createDataSource( hints ));
    }
    
    /**
     * Creates an HsqlDialectEpsgMediator with a 20 min timeout, single worker,
     * and no cache.
     * 
     * @param priority
     * @param datasource
     */
    public HsqlDialectEpsgMediator(int priority, DataSource datasource) {
        this( priority, 
             new Hints(Hints.AUTHORITY_MAX_ACTIVE, 
                 Integer.valueOf(1),
                 new Object[] {
                     Hints.AUTHORITY_MIN_EVICT_IDLETIME, Integer.valueOf(20 * 60 * 1000),
                     Hints.CACHE_POLICY, "none"
                 }
             ),
             datasource
         );
    }
    
    public HsqlDialectEpsgMediator(int priority, Hints hints, DataSource datasource) {
        super(hints, datasource);
        this.hints = hints;
    }

    /**
     * Reinitialize an instance to be returned by the pool.
     */
    protected void activateWorker(AbstractCachedAuthorityFactory obj) throws Exception {
        HsqlDialectEpsgFactory factory = (HsqlDialectEpsgFactory) obj;
        factory.connect();
    }

    /**
     * Destroys an instance no longer needed by the pool.
     */
    protected void destroyWorker(AbstractCachedAuthorityFactory obj) throws Exception {
        HsqlDialectEpsgFactory factory = (HsqlDialectEpsgFactory) obj;
        factory.disconnect();
        factory.dispose();
        factory = null;
    }

    /**
     * Creates an instance that can be returned by the pool.
     */
    protected AbstractCachedAuthorityFactory makeWorker() throws Exception {
        HsqlDialectEpsgFactory factory = new HsqlDialectEpsgFactory(hints, datasource);
        return factory;
    }

    /**
     * Uninitialize an instance to be returned to the pool.
     */
    protected void passivateWorker(AbstractCachedAuthorityFactory obj) throws Exception {
        HsqlDialectEpsgFactory factory = (HsqlDialectEpsgFactory) obj;
        factory.disconnect();
    }

    /**
     * Ensures that the instance is safe to be returned by the pool.
     */
    protected boolean validateWorker(AbstractCachedAuthorityFactory obj) {
        return true;
    }

}
