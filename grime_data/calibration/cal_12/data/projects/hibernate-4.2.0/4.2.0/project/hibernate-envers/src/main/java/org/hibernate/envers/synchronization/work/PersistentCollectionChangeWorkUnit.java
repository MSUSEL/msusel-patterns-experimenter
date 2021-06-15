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
package org.hibernate.envers.synchronization.work;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.collection.spi.PersistentCollection;
import org.hibernate.engine.spi.CollectionEntry;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.envers.RevisionType;
import org.hibernate.envers.configuration.AuditConfiguration;
import org.hibernate.envers.configuration.AuditEntitiesConfiguration;
import org.hibernate.envers.entities.mapper.PersistentCollectionChangeData;

/**
 * @author Adam Warski (adam at warski dot org)
 */
public class PersistentCollectionChangeWorkUnit extends AbstractAuditWorkUnit implements AuditWorkUnit {
    private final List<PersistentCollectionChangeData> collectionChanges;
    private final String referencingPropertyName;

    public PersistentCollectionChangeWorkUnit(SessionImplementor sessionImplementor, String entityName,
											  AuditConfiguration auditCfg, PersistentCollection collection,
											  CollectionEntry collectionEntry, Serializable snapshot, Serializable id,
                                              String referencingPropertyName) {
        super(sessionImplementor, entityName, auditCfg, new PersistentCollectionChangeWorkUnitId(id, collectionEntry.getRole()), RevisionType.MOD);

		this.referencingPropertyName = referencingPropertyName;

        collectionChanges = auditCfg.getEntCfg().get(getEntityName()).getPropertyMapper()
                .mapCollectionChanges(sessionImplementor, referencingPropertyName, collection, snapshot, id);
    }

    public PersistentCollectionChangeWorkUnit(SessionImplementor sessionImplementor, String entityName,
                                              AuditConfiguration verCfg, Serializable id,
                                              List<PersistentCollectionChangeData> collectionChanges,
                                              String referencingPropertyName) {
        super(sessionImplementor, entityName, verCfg, id, RevisionType.MOD);

        this.collectionChanges = collectionChanges;
        this.referencingPropertyName = referencingPropertyName;
    }

    public boolean containsWork() {
        return collectionChanges != null && collectionChanges.size() != 0;
    }

    public Map<String, Object> generateData(Object revisionData) {
        throw new UnsupportedOperationException("Cannot generate data for a collection change work unit!");
    }

    @SuppressWarnings({"unchecked"})
    public void perform(Session session, Object revisionData) {
        AuditEntitiesConfiguration entitiesCfg = verCfg.getAuditEntCfg();

        for (PersistentCollectionChangeData persistentCollectionChangeData : collectionChanges) {
            // Setting the revision number
            ((Map<String, Object>) persistentCollectionChangeData.getData().get(entitiesCfg.getOriginalIdPropName()))
                    .put(entitiesCfg.getRevisionFieldName(), revisionData);

            auditStrategy.performCollectionChange(session, getEntityName(), referencingPropertyName, verCfg, persistentCollectionChangeData, revisionData);
        }
    }

    public String getReferencingPropertyName() {
        return referencingPropertyName;
    }

    public List<PersistentCollectionChangeData> getCollectionChanges() {
        return collectionChanges;
    }

    public AuditWorkUnit merge(AddWorkUnit second) {
        return null;
    }

    public AuditWorkUnit merge(ModWorkUnit second) {
        return null;
    }

    public AuditWorkUnit merge(DelWorkUnit second) {
        return null;
    }

    public AuditWorkUnit merge(CollectionChangeWorkUnit second) {
        return null;
    }

    public AuditWorkUnit merge(FakeBidirectionalRelationWorkUnit second) {
        return null;
    }

    public AuditWorkUnit dispatch(WorkUnitMergeVisitor first) {
        if (first instanceof PersistentCollectionChangeWorkUnit) {
            PersistentCollectionChangeWorkUnit original = (PersistentCollectionChangeWorkUnit) first;

            // Merging the collection changes in both work units.

            // First building a map from the ids of the collection-entry-entities from the "second" collection changes,
            // to the PCCD objects. That way, we will be later able to check if an "original" collection change
            // should be added, or if it is overshadowed by a new one.
            Map<Object, PersistentCollectionChangeData> newChangesIdMap = new HashMap<Object, PersistentCollectionChangeData>();
            for (PersistentCollectionChangeData persistentCollectionChangeData : getCollectionChanges()) {
                newChangesIdMap.put(
                        getOriginalId(persistentCollectionChangeData),
                        persistentCollectionChangeData);
            }

            // This will be the list with the resulting (merged) changes.
            List<PersistentCollectionChangeData> mergedChanges = new ArrayList<PersistentCollectionChangeData>();

            // Including only those original changes, which are not overshadowed by new ones.
            for (PersistentCollectionChangeData originalCollectionChangeData : original.getCollectionChanges()) {
                Object originalOriginalId = getOriginalId(originalCollectionChangeData);
                if (!newChangesIdMap.containsKey(originalOriginalId)) {
                    mergedChanges.add(originalCollectionChangeData);
                } else {
                    // If the changes collide, checking if the first one isn't a DEL, and the second a subsequent ADD
                    // If so, removing the change alltogether.
                    String revTypePropName = verCfg.getAuditEntCfg().getRevisionTypePropName();
                    if (RevisionType.ADD.equals(newChangesIdMap.get(originalOriginalId).getData().get(
                            revTypePropName)) && RevisionType.DEL.equals(originalCollectionChangeData.getData().get(
                            revTypePropName))) {
                        newChangesIdMap.remove(originalOriginalId);
                    }
                }
            }

            // Finally adding all of the new changes to the end of the list (the map values may differ from
            // getCollectionChanges() because of the last operation above).
            mergedChanges.addAll(newChangesIdMap.values());

            return new PersistentCollectionChangeWorkUnit(sessionImplementor, entityName, verCfg, id, mergedChanges, 
                    referencingPropertyName);
        } else {
            throw new RuntimeException("Trying to merge a " + first + " with a PersitentCollectionChangeWorkUnit. " +
                    "This is not really possible.");
        }
    }

    private Object getOriginalId(PersistentCollectionChangeData persistentCollectionChangeData) {
        return persistentCollectionChangeData.getData().get(verCfg.getAuditEntCfg().getOriginalIdPropName());
    }

    /**
     * A unique identifier for a collection work unit. Consists of an id of the owning entity and the name of
     * the entity plus the name of the field (the role). This is needed because such collections aren't entities
     * in the "normal" mapping, but they are entities for Envers.
     */
    public static class PersistentCollectionChangeWorkUnitId implements Serializable {
        private static final long serialVersionUID = -8007831518629167537L;
        
        private final Serializable ownerId;
        private final String role;

        public PersistentCollectionChangeWorkUnitId(Serializable ownerId, String role) {
            this.ownerId = ownerId;
            this.role = role;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            PersistentCollectionChangeWorkUnitId that = (PersistentCollectionChangeWorkUnitId) o;

            if (ownerId != null ? !ownerId.equals(that.ownerId) : that.ownerId != null) return false;
            //noinspection RedundantIfStatement
            if (role != null ? !role.equals(that.role) : that.role != null) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = ownerId != null ? ownerId.hashCode() : 0;
            result = 31 * result + (role != null ? role.hashCode() : 0);
            return result;
        }

        public Serializable getOwnerId() {
            return ownerId;
        }
    }
}
