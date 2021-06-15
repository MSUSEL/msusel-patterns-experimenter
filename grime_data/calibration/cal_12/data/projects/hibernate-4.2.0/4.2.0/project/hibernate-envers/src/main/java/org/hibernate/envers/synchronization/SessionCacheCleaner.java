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
package org.hibernate.envers.synchronization;

import org.hibernate.Session;
import org.hibernate.action.spi.AfterTransactionCompletionProcess;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.event.spi.EventSource;

/**
 * Class responsible for evicting audit data entries that have been stored in the session level cache.
 * This operation increases Envers performance in case of massive entity updates without clearing persistence context.
 * @author Lukasz Antoniak (lukasz dot antoniak at gmail dot com)
 */
public class SessionCacheCleaner {
    /**
     * Schedules audit data removal from session level cache after transaction completion. The operation is performed
     * regardless of commit success.
     * @param session Active Hibernate session.
     * @param data Audit data that shall be evicted (e.g. revision data or entity snapshot)
     */
    public void scheduleAuditDataRemoval(final Session session, final Object data) {
        ((EventSource) session).getActionQueue().registerProcess(new AfterTransactionCompletionProcess() {
            public void doAfterTransactionCompletion(boolean success, SessionImplementor session) {
                if (!session.isClosed()) {
                    ((Session) session).evict(data);
                }
            }
        });
    }
}
