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
package com.ivata.groupware.container.persistence.listener;

import com.ivata.mask.persistence.PersistenceException;
import com.ivata.mask.persistence.PersistenceSession;
import com.ivata.mask.valueobject.ValueObject;

/**
 * Called when a value object is about to be amended.
 * @since ivata groupware 0.10 (2005-01-17)
 * @author Colin MacLeod
 * <a href="mailto:colin.macleod@ivata.com">colin.macleod@ivata.com</a>
 * @version $Revision: 1.2 $
 */
public interface AmendPersistenceListener {
    /**
     * Called just before a previously stored value object is amended, after
     * changes have been made.
     *
     * @param session
     *            current persistence session. Should have been previously
     *            opened with {@link #openSession() openSession()}.
     * @param valueObject
     *            value object for which changes should be saved.
     * @throws PersistenceException
     *             if the object cannot be persisted.
     */
    void onAmend(PersistenceSession session, ValueObject valueObject)
            throws PersistenceException;
}
