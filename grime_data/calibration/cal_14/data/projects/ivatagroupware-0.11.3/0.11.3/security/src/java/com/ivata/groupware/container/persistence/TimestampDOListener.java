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

import org.apache.log4j.Logger;

import com.ivata.groupware.admin.security.server.SecuritySession;
import com.ivata.groupware.container.persistence.listener.AddPersistenceListener;
import com.ivata.groupware.container.persistence.listener.AmendPersistenceListener;
import com.ivata.mask.persistence.PersistenceException;
import com.ivata.mask.persistence.PersistenceSession;
import com.ivata.mask.valueobject.ValueObject;

/**
 * This class handles the timestamp properties when objects are added to or
 * amended in the store.
 * @since ivata groupware 0.10 (2005-01-17)
 * @author Colin MacLeod
 * <a href="mailto:colin.macleod@ivata.com">colin.macleod@ivata.com</a>
 * @version $Revision: 1.2 $
 */
public class TimestampDOListener implements AddPersistenceListener,
        AmendPersistenceListener {
    /**
     * Refer to {@link Logger}.
     */
    private Logger log = Logger.getLogger(TimestampDOListener.class);
    /**
     * Constructor. Registers this listener with the persistence manager.
     * @param persistenceManager used to register this listener against
     * subclasses of <code>TimestampDO</code>.
     */
    public TimestampDOListener(QueryPersistenceManager persistenceManager) {
        persistenceManager.addAddListener(TimestampDO.class,
                this);
        persistenceManager.addAmendListener(TimestampDO.class,
                this);
    }

    /**
     * Refer to {@link AddPersistenceListener#onAdd}.
     *
     * @param session Refer to {@link AddPersistenceListener#onAdd}.
     * @param valueObject Refer to {@link AddPersistenceListener#onAdd}.
     * @throws PersistenceException Refer to {@link
     * AddPersistenceListener#onAdd}.
     */
    public void onAdd(PersistenceSession session, ValueObject valueObject)
            throws PersistenceException {
        if (log.isDebugEnabled()) {
            log.debug("onAdd: before: "
                    + valueObject);
        }
        TimestampDO timestampDO = (TimestampDO)valueObject;
        SecuritySession securitySession = (SecuritySession)
            session.getSystemSession();
        assert (securitySession != null);
        TimestampDOHandling.add(securitySession, timestampDO);
        if (log.isDebugEnabled()) {
            log.debug("onAdd: after: "
                    + valueObject);
        }
    }

    /**
     * Refer to {@link AmendPersistenceListener#onAmend}.
     *
     * @param session Refer to {@link AmendPersistenceListener#onAmend}.
     * @param valueObject Refer to {@link AmendPersistenceListener#onAmend}.
     * @throws PersistenceException Refer to {@link
     * AmendPersistenceListener#onAmend}.
     */
    public void onAmend(PersistenceSession session, ValueObject valueObject)
            throws PersistenceException {
        if (log.isDebugEnabled()) {
            log.debug("onAmend: before: "
                    + valueObject);
        }
        TimestampDO timestampDO = (TimestampDO)valueObject;
        SecuritySession securitySession = (SecuritySession)
            session.getSystemSession();
        assert (securitySession != null);
        TimestampDOHandling.amend(securitySession, timestampDO);
        if (log.isDebugEnabled()) {
            log.debug("onAmend: after: "
                    + valueObject);
        }
    }
}
