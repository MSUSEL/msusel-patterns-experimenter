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

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import org.hibernate.ConnectionReleaseMode;
import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.action.spi.BeforeTransactionCompletionProcess;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.envers.revisioninfo.RevisionInfoGenerator;
import org.hibernate.envers.synchronization.work.AuditWorkUnit;
import org.hibernate.envers.tools.Pair;

/**
 * @author Adam Warski (adam at warski dot org)
 */
public class AuditProcess implements BeforeTransactionCompletionProcess {
    private final RevisionInfoGenerator revisionInfoGenerator;
    private final SessionImplementor session;

    private final LinkedList<AuditWorkUnit> workUnits;
    private final Queue<AuditWorkUnit> undoQueue;
    private final Map<Pair<String, Object>, AuditWorkUnit> usedIds;
    private final EntityChangeNotifier entityChangeNotifier;

    private Object revisionData;

    public AuditProcess(RevisionInfoGenerator revisionInfoGenerator, SessionImplementor session) {
        this.revisionInfoGenerator = revisionInfoGenerator;
        this.session = session;

        workUnits = new LinkedList<AuditWorkUnit>();
        undoQueue = new LinkedList<AuditWorkUnit>();
        usedIds = new HashMap<Pair<String, Object>, AuditWorkUnit>();
        entityChangeNotifier = new EntityChangeNotifier(revisionInfoGenerator, session);
    }

    private void removeWorkUnit(AuditWorkUnit vwu) {
        workUnits.remove(vwu);
        if (vwu.isPerformed()) {
            // If this work unit has already been performed, it must be deleted (undone) first.
            undoQueue.offer(vwu);
        }
    }

    public void addWorkUnit(AuditWorkUnit vwu) {
        if (vwu.containsWork()) {
            Object entityId = vwu.getEntityId();

            if (entityId == null) {
                // Just adding the work unit - it's not associated with any persistent entity.
                workUnits.offer(vwu);
            } else {
                String entityName = vwu.getEntityName();
                Pair<String, Object> usedIdsKey = Pair.make(entityName, entityId);

                if (usedIds.containsKey(usedIdsKey)) {
                    AuditWorkUnit other = usedIds.get(usedIdsKey);

                    AuditWorkUnit result = vwu.dispatch(other);

                    if (result != other) {
                        removeWorkUnit(other);

                        if (result != null) {
                            usedIds.put(usedIdsKey, result);
                            workUnits.offer(result);
                        } // else: a null result means that no work unit should be kept
                    } // else: the result is the same as the work unit already added. No need to do anything.
                } else {
                    usedIds.put(usedIdsKey, vwu);
                    workUnits.offer(vwu);
                }
            }
        }
    }

    private void executeInSession(Session session) {
		// Making sure the revision data is persisted.
        Object currentRevisionData = getCurrentRevisionData(session, true);

        AuditWorkUnit vwu;

        // First undoing any performed work units
        while ((vwu = undoQueue.poll()) != null) {
            vwu.undo(session);
        }

        while ((vwu = workUnits.poll()) != null) {
            vwu.perform(session, revisionData);
            entityChangeNotifier.entityChanged(session, currentRevisionData, vwu);
        }
    }

	public Object getCurrentRevisionData(Session session, boolean persist) {
		// Generating the revision data if not yet generated
		if (revisionData == null) {
            revisionData = revisionInfoGenerator.generate();
        }

		// Saving the revision data, if not yet saved and persist is true
		if (!session.contains(revisionData) && persist) {
			revisionInfoGenerator.saveRevisionData(session, revisionData);
		}

		return revisionData;
	}

    public void doBeforeTransactionCompletion(SessionImplementor session) {
        if (workUnits.size() == 0 && undoQueue.size() == 0) {
            return;
        }

        // see: http://www.jboss.com/index.html?module=bb&op=viewtopic&p=4178431
        if (FlushMode.isManualFlushMode(session.getFlushMode())) {
            Session temporarySession = null;
            try {
                temporarySession = ((Session) session).sessionWithOptions().transactionContext().autoClose(false)
                                                                           .connectionReleaseMode(ConnectionReleaseMode.AFTER_TRANSACTION)
                                                                           .openSession();
                executeInSession(temporarySession);
                temporarySession.flush();
            } finally {
                if (temporarySession != null) {
                    temporarySession.close();
                }
            }
        } else {
            executeInSession((Session) session);

            // Explicitly flushing the session, as the auto-flush may have already happened.
            session.flush();
        }
    }
}
