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
package org.geotools.data.directory;

import java.io.IOException;
import java.util.List;

import org.geotools.data.DataStore;
import org.geotools.data.FeatureLock;
import org.geotools.data.LockingManager;
import org.geotools.data.Transaction;

/**
 * Locking manager that will delegate its work to the locking managers of the 
 * delegate data stores 
 * @author Andrea Aime - OpenGeo
 *
 *
 *
 *
 * @source $URL$
 */
public class DirectoryLockingManager implements LockingManager {
    
    DirectoryTypeCache cache;
    
    public DirectoryLockingManager(DirectoryTypeCache cache) {
        this.cache = cache;
    }

    public boolean exists(String authID) {
        List<DataStore> stores = cache.getDataStores();
        for (DataStore store : stores) {
            if ((store.getLockingManager() != null)
                    && store.getLockingManager().exists(authID)) {
                return true;
            }
        }

        return false;
    }

    public boolean release(String authID, Transaction transaction)
        throws IOException {
        List<DataStore> stores = cache.getDataStores();
        for (DataStore store : stores) {
            if ((store.getLockingManager() != null)
                    && store.getLockingManager().exists(authID)) {
                return store.getLockingManager().release(authID, transaction);
            }
        }

        return false;
    }

    public boolean refresh(String authID, Transaction transaction)
        throws IOException {
        List<DataStore> stores = cache.getDataStores();
        for (DataStore store : stores) {
            if ((store.getLockingManager() != null)
                    && store.getLockingManager().exists(authID)) {
                return store.getLockingManager().refresh(authID, transaction);
            }
        }

        return false;
    }

    public void unLockFeatureID(String typeName, String authID,
        Transaction transaction, FeatureLock featureLock)
        throws IOException {
        DataStore store = cache.getDataStore(typeName, false);

        if ((store != null) && (store.getLockingManager() != null)) {
            store.getLockingManager().unLockFeatureID(typeName, authID,
                transaction, featureLock);
        }
    }

    public void lockFeatureID(String typeName, String authID,
        Transaction transaction, FeatureLock featureLock)
        throws IOException {
        DataStore store = cache.getDataStore(typeName, false);

        if ((store != null) && (store.getLockingManager() != null)) {
            store.getLockingManager().lockFeatureID(typeName, authID,
                transaction, featureLock);
        }
    }

}
