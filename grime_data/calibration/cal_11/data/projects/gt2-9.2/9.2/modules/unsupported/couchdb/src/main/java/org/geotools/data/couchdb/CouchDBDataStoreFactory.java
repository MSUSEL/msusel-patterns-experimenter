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
 *    (C) 2004-2011, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.couchdb;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.data.AbstractDataStoreFactory;
import org.geotools.data.DataStore;

/**
 *
 * @author Ian Schneider (OpenGeo)
 *
 * @source $URL$
 */
public class CouchDBDataStoreFactory extends AbstractDataStoreFactory {
    
    public static final Param SERVER_URL = new Param("serverURL", String.class, "Server URL", true);
    public static final Param DB_NAME = new Param("dbName", String.class, "Database Name", true);

    public DataStore createDataStore(Map<String, Serializable> params) throws IOException {
        CouchDBDataStore dataStore = new CouchDBDataStore();
        dataStore.setCouchURL((String)SERVER_URL.lookUp(params));
        dataStore.setDatabaseName((String)DB_NAME.lookUp(params));
        try {
            dataStore.init();
        } catch (Exception ex) {
            throw new IOException("Error initializing datastore",ex);
        }
        return dataStore;
    }

    public DataStore createNewDataStore(Map<String, Serializable> params) throws IOException {
        // @todo - this could be possible
        throw new UnsupportedOperationException();
    }
    
    @Override
    public String getDisplayName() {
        return "CouchDB Datastore";
    }

    public String getDescription() {
        return "Datastore backed by CouchDB";
    }

    public Param[] getParametersInfo() {
        return new Param[] {
            SERVER_URL,
            DB_NAME
        };
    }
}
