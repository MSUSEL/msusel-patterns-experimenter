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

import java.io.Serializable;

import org.hibernate.Session;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.envers.RevisionType;
import org.hibernate.envers.revisioninfo.RevisionInfoGenerator;
import org.hibernate.envers.synchronization.work.AuditWorkUnit;
import org.hibernate.envers.synchronization.work.PersistentCollectionChangeWorkUnit;
import org.hibernate.envers.tools.Tools;

/**
 * Notifies {@link RevisionInfoGenerator} about changes made in the current revision.
 * @author Lukasz Antoniak (lukasz dot antoniak at gmail dot com)
 */
public class EntityChangeNotifier {
    private final RevisionInfoGenerator revisionInfoGenerator;
    private final SessionImplementor sessionImplementor;

    public EntityChangeNotifier(RevisionInfoGenerator revisionInfoGenerator, SessionImplementor sessionImplementor) {
        this.revisionInfoGenerator = revisionInfoGenerator;
        this.sessionImplementor = sessionImplementor;
    }

    /**
     * Notifies {@link RevisionInfoGenerator} about changes made in the current revision. Provides information
     * about modified entity class, entity name and its id, as well as {@link RevisionType} and revision log entity.
     * @param session Active session.
     * @param currentRevisionData Revision log entity.
     * @param vwu Performed work unit.
     */
    public void entityChanged(Session session, Object currentRevisionData, AuditWorkUnit vwu) {
        Serializable entityId = vwu.getEntityId();
        if (entityId instanceof PersistentCollectionChangeWorkUnit.PersistentCollectionChangeWorkUnitId) {
            // Notify about a change in collection owner entity.
            entityId = ((PersistentCollectionChangeWorkUnit.PersistentCollectionChangeWorkUnitId) entityId).getOwnerId();
        }
        Class entityClass = Tools.getEntityClass(sessionImplementor, session, vwu.getEntityName());
        revisionInfoGenerator.entityChanged(entityClass, vwu.getEntityName(), entityId, vwu.getRevisionType(),
                                            currentRevisionData);
    }
}
