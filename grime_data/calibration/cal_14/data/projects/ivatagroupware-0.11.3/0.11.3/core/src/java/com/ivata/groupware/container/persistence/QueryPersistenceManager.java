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
package com.ivata.groupware.container.persistence;

import java.util.List;

import com.ivata.groupware.container.persistence.listener.AddPersistenceListener;
import com.ivata.groupware.container.persistence.listener.AmendPersistenceListener;
import com.ivata.groupware.container.persistence.listener.RemovePersistenceListener;
import com.ivata.mask.persistence.PersistenceException;
import com.ivata.mask.persistence.PersistenceManager;
import com.ivata.mask.persistence.PersistenceSession;
import com.ivata.mask.valueobject.ValueObject;

/**
 * Extends the <strong>ivata masks</strong> persistence manager to include
 * facilities to execute queries against the persistence store, and adds
 * listeners.
 *
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 * @since Mar 27, 2004
 * @version $Revision: 1.2 $
 */
public interface QueryPersistenceManager extends PersistenceManager {
    void addAddListener(Class dOClass, AddPersistenceListener listener);
    void addAmendListener(Class dOClass, AmendPersistenceListener listener);
    void addRemoveListener(Class dOClass, RemovePersistenceListener listener);
    List find(final PersistenceSession session,
            final String queryName,
            final Object [] queryArguments) throws PersistenceException;
    List find(final PersistenceSession session,
            final String queryName,
            final Object [] queryArguments,
            final Integer pageSize,
            final Integer pageNumber) throws PersistenceException;
    ValueObject findInstance(final PersistenceSession session,
            final String queryName,
            final Object [] queryArguments) throws PersistenceException;
    Integer findInteger(final PersistenceSession session,
            final String queryName,
            final Object [] queryArguments) throws PersistenceException;
    String findString(final PersistenceSession session,
            final String queryName,
            final Object [] queryArguments) throws PersistenceException;
    void remove(final PersistenceSession session,
            final ValueObject valueObject) throws PersistenceException;
    void removeAll(final PersistenceSession session,
            final String queryName,
            final Object [] queryArguments) throws PersistenceException;
}
