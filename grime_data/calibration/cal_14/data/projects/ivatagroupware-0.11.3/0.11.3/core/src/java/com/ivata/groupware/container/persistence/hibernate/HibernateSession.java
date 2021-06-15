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
package com.ivata.groupware.container.persistence.hibernate;

import java.sql.Connection;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;

import org.apache.log4j.Logger;

import com.ivata.mask.persistence.PersistenceException;
import com.ivata.mask.persistence.PersistenceSession;

/**
 * <p>
 * Adaptor to wrap the <strong>Hibernate</strong> session.
 * </p>
 *
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 * @since Mar 27, 2004
 * @version $Revision: 1.2 $
 */
public class HibernateSession implements PersistenceSession {
    /**
     * Refer to {@link Logger}.
     */
    private static Logger log = Logger.getLogger(HibernateSession.class);
    /**
     * If <code>true</code> the transaction will be rolled back.
     */
    private boolean cancel = false;
    /**
     * <p>
     * The Hibernate session we're adapting.
     * </p>
     */
    private Session session;

    private Object systemSession;

    /**
     * Hibernate transaction being wrapped.
     */
    private Transaction transaction;

    /**
     * Create a new instance wrapping the given hibernate session, and
     * transaction,
     */
    public HibernateSession(Session sessionParam, Transaction transactionParam,
            Object systemSessionParam) {
        this.session = sessionParam;
        this.transaction = transactionParam;
        this.systemSession = systemSessionParam;
    }

    /**
     * Refer to {@link com.ivata.mask.persistence.PersistenceSession#cancel}.
     *
     * @throws PersistenceException
     * Refer to {@link com.ivata.mask.persistence.PersistenceSession#cancel}.
     */
    public void cancel() throws PersistenceException {
        cancel = true;
    }

    /**
     * @see com.ivata.mask.persistence.PersistenceSession#commit()
     */
    public void close() throws PersistenceException {
        HibernateException hibernateException = null;
        if (cancel) {
            try {
                transaction.rollback();
            } catch (HibernateException e) {
                log.error("("
                        + e.getClass().getName()
                        + ") ROLLING BACK TRANSACTION: "
                        + e.getMessage(), e);
            } finally {
                try {
                    session.close();
                } catch (HibernateException e) {
                    if (hibernateException != null) {
                        hibernateException = e;
                    }
                }
            }

        } else {
            try {
                if (!transaction.wasRolledBack()) {
                    transaction.commit();
                }
            } catch (HibernateException e) {
                hibernateException = e;
                try {
                    transaction.rollback();
                } catch (Exception eRollback) {
                    log.error("("
                            + e.getClass().getName()
                            + ") ROLLING BACK TRANSACTION: "
                            + e.getMessage(), e);
                }
            } finally {
                try {
                    session.close();
                } catch (HibernateException e) {
                    if (hibernateException != null) {
                        hibernateException = e;
                    }
                }
            }
        }
        if (hibernateException != null) {
            throw new PersistenceException("Error closing hibernate persistence session: ",
                hibernateException);
        }
    }

    /**
     * TODO
     *
     * @see com.ivata.mask.persistence.PersistenceSession#getConnection()
     */
    public final Connection getConnection() throws PersistenceException {
        try {
            return session.connection();
        } catch (HibernateException e) {
            throw new PersistenceException(e);
        }
    }

    /**
     * Get the hibernate session this session adapts.
     *
     * @param session valid hibernate session.
     */
    Session getSession() {
        return session;
    }

    /**
     * @return Returns the systemSession.
     */
    public Object getSystemSession() {
        return systemSession;
    }
    /**
     * @return transaction
     */
    public Transaction getTransaction() {
        return transaction;
    }
}
