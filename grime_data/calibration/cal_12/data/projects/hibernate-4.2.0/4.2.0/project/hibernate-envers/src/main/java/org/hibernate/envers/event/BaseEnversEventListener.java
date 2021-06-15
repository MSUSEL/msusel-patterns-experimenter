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

import java.io.Serializable;
import java.util.Set;

import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.envers.configuration.AuditConfiguration;
import org.hibernate.envers.entities.RelationDescription;
import org.hibernate.envers.entities.RelationType;
import org.hibernate.envers.entities.mapper.id.IdMapper;
import org.hibernate.envers.exception.AuditException;
import org.hibernate.envers.synchronization.AuditProcess;
import org.hibernate.envers.synchronization.work.CollectionChangeWorkUnit;
import org.hibernate.envers.tools.Tools;
import org.hibernate.persister.entity.EntityPersister;
import org.hibernate.proxy.HibernateProxy;

/**
 * Base class for all Envers event listeners
 *
 * @author Adam Warski (adam at warski dot org)
 * @author HernпїЅn Chanfreau
 * @author Steve Ebersole
 * @author Michal Skowronek (mskowr at o2 dot pl)
 */
public abstract class BaseEnversEventListener implements EnversListener {
	private AuditConfiguration enversConfiguration;

	protected BaseEnversEventListener(AuditConfiguration enversConfiguration) {
		this.enversConfiguration = enversConfiguration;
	}

	@Override
	public AuditConfiguration getAuditConfiguration() {
		return enversConfiguration;
	}

	protected final void generateBidirectionalCollectionChangeWorkUnits(
			AuditProcess auditProcess,
			EntityPersister entityPersister,
			String entityName,
			Object[] newState,
			Object[] oldState,
			SessionImplementor session) {
		// Checking if this is enabled in configuration ...
		if ( ! enversConfiguration.getGlobalCfg().isGenerateRevisionsForCollections() ) {
			return;
		}

		// Checks every property of the entity, if it is an "owned" to-one relation to another entity.
		// If the value of that property changed, and the relation is bi-directional, a new revision
		// for the related entity is generated.
		String[] propertyNames = entityPersister.getPropertyNames();

		for ( int i=0; i<propertyNames.length; i++ ) {
			String propertyName = propertyNames[i];
			RelationDescription relDesc = enversConfiguration.getEntCfg().getRelationDescription(entityName, propertyName);
			if (relDesc != null && relDesc.isBidirectional() && relDesc.getRelationType() == RelationType.TO_ONE &&
					relDesc.isInsertable()) {
				// Checking for changes
				Object oldValue = oldState == null ? null : oldState[i];
				Object newValue = newState == null ? null : newState[i];

				if (!Tools.entitiesEqual( session, relDesc.getToEntityName(), oldValue, newValue )) {
					// We have to generate changes both in the old collection (size decreses) and new collection
					// (size increases).
					if (newValue != null) {
						addCollectionChangeWorkUnit(auditProcess, session, entityName, relDesc, newValue);
					}

					if (oldValue != null) {
						addCollectionChangeWorkUnit(auditProcess, session, entityName, relDesc, oldValue);
					}
				}
			}
		}
	}

	private void addCollectionChangeWorkUnit(AuditProcess auditProcess, SessionImplementor session,
											 String fromEntityName, RelationDescription relDesc, Object value) {
		// relDesc.getToEntityName() doesn't always return the entity name of the value - in case
		// of subclasses, this will be root class, no the actual class. So it can't be used here.
		String toEntityName;
		Serializable id;

		if (value instanceof HibernateProxy) {
		    HibernateProxy hibernateProxy = (HibernateProxy) value;
		    toEntityName = session.bestGuessEntityName(value);
		    id = hibernateProxy.getHibernateLazyInitializer().getIdentifier();
            // We've got to initialize the object from the proxy to later read its state.
            value = Tools.getTargetFromProxy(session.getFactory(), hibernateProxy);
		} else {
	        toEntityName =  session.guessEntityName(value);

            IdMapper idMapper = enversConfiguration.getEntCfg().get(toEntityName).getIdMapper();
            id = (Serializable) idMapper.mapToIdFromEntity(value);
		}

		Set<String> toPropertyNames = enversConfiguration.getEntCfg()
				.getToPropertyNames(fromEntityName, relDesc.getFromPropertyName(), toEntityName);
		String toPropertyName = toPropertyNames.iterator().next();

		auditProcess.addWorkUnit(new CollectionChangeWorkUnit(session, toEntityName,
				toPropertyName, enversConfiguration, id, value));
	}

    protected void checkIfTransactionInProgress(SessionImplementor session) {
        if (!session.isTransactionInProgress()) {
            // Historical data would not be flushed to audit tables if outside of active transaction
            // (AuditProcess#doBeforeTransactionCompletion(SessionImplementor) not executed). 
            throw new AuditException("Unable to create revision because of non-active transaction");
        }
    }
}
