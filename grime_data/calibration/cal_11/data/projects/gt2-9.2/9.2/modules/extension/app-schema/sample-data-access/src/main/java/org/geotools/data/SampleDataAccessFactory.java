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
 *    (C) 2008-2011, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.data;

import java.awt.RenderingHints.Key;
import java.io.IOException;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.opengis.feature.Feature;
import org.opengis.feature.type.FeatureType;

/**
 * Sample implementation of a {@link DataAccessFactory} for testing.
 * 
 * <p>
 * 
 * Enabled with a connection parameter "dbtype" of "sample-data-access".
 * 
 * @author Ben Caradoc-Davies (CSIRO Earth Science and Resource Engineering)
 * @version $Id$
 *
 *
 *
 * @source $URL$
 * @since 2.6
 */
public class SampleDataAccessFactory implements DataAccessFactory {

    /**
     * The "dbtype" connection string required to use this factory.
     */
    public static final String DBTYPE_STRING = "sample-data-access";

    public static final DataAccessFactory.Param DBTYPE = new DataAccessFactory.Param("dbtype",
            String.class, "Fixed value '" + DBTYPE_STRING + "'", true, DBTYPE_STRING);

    /**
     * The connection parameters required to use this factory.
     */
    @SuppressWarnings("serial")
    public static final HashMap<String, Serializable> PARAMS = new HashMap<String, Serializable>() {
        {
            put(SampleDataAccessFactory.DBTYPE.key, SampleDataAccessFactory.DBTYPE_STRING);
        }
    };

    /**
     * Are these parameters for us?
     * 
     * @see org.geotools.data.DataAccessFactory#canProcess(java.util.Map)
     */
    public boolean canProcess(Map<String, Serializable> params) {
        return DBTYPE_STRING.equals(params.get(SampleDataAccessFactory.DBTYPE.key));
    }

    /**
     * Create a {@link SampleDataAccess}.
     * 
     * @see org.geotools.data.DataAccessFactory#createDataStore(java.util.Map)
     */
    public DataAccess<? extends FeatureType, ? extends Feature> createDataStore(
            Map<String, Serializable> params) throws IOException {
        return new SampleDataAccess();
    }

    /**
     * Need to implement this.
     * 
     * @see org.geotools.data.DataAccessFactory#getDescription()
     */
    public String getDescription() {
        // FIXME implement this
        return null;
    }

    /**
     * Need to implement this.
     * 
     * @see org.geotools.data.DataAccessFactory#getDisplayName()
     */
    public String getDisplayName() {
        // FIXME implement this
        return null;
    }

    /**
     * Need to implement this.
     * 
     * @see org.geotools.data.DataAccessFactory#getParametersInfo()
     */
    public Param[] getParametersInfo() {
        // FIXME implement this
        return null;
    }

    /**
     * Returns true, as this implementation is always available.
     * 
     * @see org.geotools.data.DataAccessFactory#isAvailable()
     */
    public boolean isAvailable() {
        return true;
    }

    /**
     * Returns an empty list, containing no hints.
     * 
     * @see org.geotools.factory.Factory#getImplementationHints()
     */
    public Map<Key, ?> getImplementationHints() {
        return Collections.emptyMap();
    }

}
