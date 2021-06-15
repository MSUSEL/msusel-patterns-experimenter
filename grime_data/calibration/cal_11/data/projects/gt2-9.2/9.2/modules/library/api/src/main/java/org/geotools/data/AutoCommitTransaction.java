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
 *    (C) 2008, Open Source Geospatial Foundation (OSGeo)
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

import java.io.IOException;
import java.util.Set;


/**
 * This is used to represent the absense of a Transaction and the use of
 * AutoCommit.
 *
 * <p>
 * This class serves as the implementation of the constant Transaction.NONE.
 * It is a NullObject and we feel no need to make this class public.
 * </p>
 *
 * @source $URL$
 */
class AutoCommitTransaction implements Transaction {
    /**
     * Authorization IDs are not stored by AutoCommit.
     *
     * <p>
     * Authorization IDs are only stored for the duration of a Transaction.
     * </p>
     *
     * @return Set of authorizations
     *
     * @throws UnsupportedOperationException AUTO_COMMIT does not support this
     */
    public Set<String> getAuthorizations() {
        throw new UnsupportedOperationException(
            "Authorization IDs are not valid for AutoCommit Transaction");
    }

    /**
     * AutoCommit does not save State.
     *
     * <p>
     * While symetry would be good, state should be commited not stored for
     * later.
     * </p>
     *
     * @param key Key that is not used to Store State
     * @param state State we are not going to externalize
     *
     * @throws UnsupportedOperationException AutoCommit does not support State
     */
    public void putState(Object key, State state) {
        throw new UnsupportedOperationException(
            "AutoCommit does not support the putState opperations");
    }

    /**
     * AutoCommit does not save State.
     *
     * <p>
     * While symetry would be good, state should be commited not stored for
     * later.
     * </p>
     *
     * @param key Key that is not used to Store State
     *
     * @throws UnsupportedOperationException AutoCommit does not support State
     */
    public void removeState(Object key) {
        throw new UnsupportedOperationException(
            "AutoCommit does not support the removeState opperations");
    }

    /**
     * I am not sure should AutoCommit be able to save sate?
     *
     * <p>
     * While symetry would be good, state should be commited not stored for
     * later.
     * </p>
     *
     * @param key Key used to retrieve State
     *
     * @return State earlier provided with putState
     *
     * @throws UnsupportedOperationException As Autocommit does not support
     *         State
     */
    public State getState(Object key) {
        throw new UnsupportedOperationException(
            "AutoCommit does not support the getState opperations");
    }

    /**
     * Implemented as a NOP since this Transaction always commits.
     *
     * <p>
     * This allows the following workflow:
     * </p>
     * <pre>
     * <code>
     * Transaction t = roads.getTransaction();
     * try{
     *     roads.addFeatures( features );
     *     roads.getTransaction().commit();
     * }
     * catch( IOException erp ){
     *     //something went wrong;
     *     roads.getTransaction().rollback();
     * }
     * </code>
     * </pre>
     */
    public void commit() {
        // implement a NOP
    }

    /**
     * Implements a NOP since AUTO_COMMIT does not maintain State.
     */
    public void close() {
        // no state to clean up after
    }

    /**
     * Auto commit mode cannot support the rollback opperation.
     *
     * @throws IOException if Rollback fails
     */
    public void rollback() throws IOException {
        throw new IOException("AutoCommit cannot support the rollback opperation");
    }

    /**
     * Authorization IDs are not stored by AutoCommit.
     *
     * <p>
     * Authorization IDs are only stored for the duration of a Transaction.
     * </p>
     *
     * @param authID Authorization ID
     *
     * @throws IOException If set authorization fails
     */
    public void addAuthorization(String authID) throws IOException {
        throw new IOException("Authorization IDs are not valid for AutoCommit Transaction");
    }

    /**
     * AutoCommit does not save State.
     *
     * <p>
     * While symetry would be good, state should be commited not stored for
     * later.
     * </p>
     *
     * @param key Key that is not used to Store Property
     *
     * @return Property associated with key, or null
     *
     * @throws UnsupportedOperationException AutoCommit does not support State
     */
    public Object getProperty(Object key) {
        throw new UnsupportedOperationException(
            "AutoCommit does not support the getProperty opperations");
    }

    /**
     * Implementation of addProperty.
     *
     * @param key
     * @param value
     *
     * @throws UnsupportedOperationException
     *
     * @see org.geotools.data.Transaction#addProperty(java.lang.Object,
     *      java.lang.Object)
     */
    public void putProperty(Object key, Object value) {
        throw new UnsupportedOperationException(
            "AutoCommit does not support the addProperty opperations");
    }
}
