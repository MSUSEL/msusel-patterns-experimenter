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
package org.hibernate.envers.event;

import org.hibernate.envers.configuration.AuditConfiguration;
import org.hibernate.envers.synchronization.AuditProcess;
import org.hibernate.envers.synchronization.work.AuditWorkUnit;
import org.hibernate.envers.synchronization.work.ModWorkUnit;
import org.hibernate.event.spi.PostUpdateEvent;
import org.hibernate.event.spi.PostUpdateEventListener;
import org.hibernate.persister.entity.EntityPersister;

/**
 * @author Adam Warski (adam at warski dot org)
 * @author HernпїЅn Chanfreau
 * @author Steve Ebersole
 */
public class EnversPostUpdateEventListenerImpl extends BaseEnversEventListener implements PostUpdateEventListener {
	protected EnversPostUpdateEventListenerImpl(AuditConfiguration enversConfiguration) {
		super( enversConfiguration );
	}

	@Override
	public void onPostUpdate(PostUpdateEvent event) {
        String entityName = event.getPersister().getEntityName();

        if ( getAuditConfiguration().getEntCfg().isVersioned(entityName) ) {
            checkIfTransactionInProgress(event.getSession());

            AuditProcess auditProcess = getAuditConfiguration().getSyncManager().get(event.getSession());

			final Object[] newDbState = postUpdateDBState( event );

            AuditWorkUnit workUnit = new ModWorkUnit(
					event.getSession(),
					event.getPersister().getEntityName(),
					getAuditConfiguration(),
                    event.getId(),
					event.getPersister(),
					newDbState,
					event.getOldState()
			);
            auditProcess.addWorkUnit( workUnit );

            if ( workUnit.containsWork() ) {
                generateBidirectionalCollectionChangeWorkUnits(
						auditProcess,
						event.getPersister(),
						entityName,
						newDbState,
                        event.getOldState(),
						event.getSession()
				);
            }
        }
	}

	private Object[] postUpdateDBState(PostUpdateEvent event) {
		Object[] newDbState = event.getState().clone();
		if ( event.getOldState() != null ) {
			EntityPersister entityPersister = event.getPersister();
			for ( int i = 0; i < entityPersister.getPropertyNames().length; ++i ) {
				if ( !entityPersister.getPropertyUpdateability()[i] ) {
					// Assuming that PostUpdateEvent#getOldState() returns database state of the record before modification.
					// Otherwise, we would have to execute SQL query to be sure of @Column(updatable = false) column value.
					newDbState[i] = event.getOldState()[i];
				}
			}
		}
		return newDbState;
	}
}
