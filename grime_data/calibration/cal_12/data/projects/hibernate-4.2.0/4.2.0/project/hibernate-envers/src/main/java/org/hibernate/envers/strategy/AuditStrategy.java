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
package org.hibernate.envers.strategy;

import java.io.Serializable;

import org.hibernate.Session;
import org.hibernate.collection.spi.PersistentCollection;
import org.hibernate.envers.configuration.AuditConfiguration;
import org.hibernate.envers.configuration.GlobalConfiguration;
import org.hibernate.envers.entities.mapper.PersistentCollectionChangeData;
import org.hibernate.envers.entities.mapper.relation.MiddleComponentData;
import org.hibernate.envers.entities.mapper.relation.MiddleIdData;
import org.hibernate.envers.tools.query.QueryBuilder;

/**
 * Behaviours of different audit strategy for populating audit data.
 * 
 * @author Stephanie Pau
 * @author Adam Warski (adam at warski dot org)
 */
public interface AuditStrategy {
    /**
     * Perform the persistence of audited data for regular entities.
     * 
     * @param session Session, which can be used to persist the data.
     * @param entityName Name of the entity, in which the audited change happens
     * @param auditCfg Audit configuration
     * @param id Id of the entity.
     * @param data Audit data to persist
     * @param revision Current revision data
     */
    void perform(Session session, String entityName, AuditConfiguration auditCfg, Serializable id, Object data,
                 Object revision);

    /**
     * Perform the persistence of audited data for collection ("middle") entities.
     *
     * @param session Session, which can be used to persist the data.
	 * @param entityName Name of the entity, in which the audited change happens.
	 * @param propertyName The name of the property holding the {@link PersistentCollection}.
     * @param auditCfg Audit configuration
     * @param persistentCollectionChangeData Collection change data to be persisted.
     * @param revision Current revision data
     */
    void performCollectionChange(Session session, String entityName, String propertyName, AuditConfiguration auditCfg,
                                 PersistentCollectionChangeData persistentCollectionChangeData, Object revision);
    

    /**
	 * Update the rootQueryBuilder with an extra WHERE clause to restrict the revision for a two-entity relation.
	 * This WHERE clause depends on the AuditStrategy, as follows:
	 * <ul>
	 * <li>For {@link DefaultAuditStrategy} a subquery is created: 
	 * <p><code>e.revision = (SELECT max(...) ...)</code></p>
	 * </li>
	 * <li>for {@link ValidityAuditStrategy} the revision-end column is used: 
	 * <p><code>e.revision <= :revision and (e.endRevision > :revision or e.endRevision is null)</code></p>
	 * </li>
	 * </ul>
	 * 
	 * @param globalCfg the {@link GlobalConfiguration}
     * @param rootQueryBuilder the {@link QueryBuilder} that will be updated
     * @param revisionProperty property of the revision column
     * @param revisionEndProperty property of the revisionEnd column (only used for {@link ValidityAuditStrategy})
     * @param addAlias {@code boolean} indicator if a left alias is needed
     * @param idData id-information for the two-entity relation (only used for {@link DefaultAuditStrategy})
     * @param revisionPropertyPath path of the revision property (only used for {@link ValidityAuditStrategy})
     * @param originalIdPropertyName name of the id property (only used for {@link ValidityAuditStrategy})
     * @param alias1 an alias used for subquery (only used for {@link ValidityAuditStrategy})
     * @param alias2 an alias used for subquery (only used for {@link ValidityAuditStrategy})
     */
	void addEntityAtRevisionRestriction(GlobalConfiguration globalCfg, QueryBuilder rootQueryBuilder,
			String revisionProperty, String revisionEndProperty, boolean addAlias, MiddleIdData idData, 
			String revisionPropertyPath, String originalIdPropertyName, String alias1, String alias2);

	/**
	 * Update the rootQueryBuilder with an extra WHERE clause to restrict the revision for a middle-entity 
	 * association. This WHERE clause depends on the AuditStrategy, as follows:
	 * <ul>
	 * <li>For {@link DefaultAuditStrategy} a subquery is created: 
	 * <p><code>e.revision = (SELECT max(...) ...)</code></p>
	 * </li>
	 * <li>for {@link ValidityAuditStrategy} the revision-end column is used: 
	 * <p><code>e.revision <= :revision and (e.endRevision > :revision or e.endRevision is null)</code></p>
	 * </li>
	 * </ul>
	 * 
	 * @param rootQueryBuilder the {@link QueryBuilder} that will be updated
     * @param revisionProperty property of the revision column
     * @param revisionEndProperty property of the revisionEnd column (only used for {@link ValidityAuditStrategy})
     * @param addAlias {@code boolean} indicator if a left alias is needed
     * @param referencingIdData id-information for the middle-entity association (only used for {@link DefaultAuditStrategy})
	 * @param versionsMiddleEntityName name of the middle-entity
	 * @param eeOriginalIdPropertyPath name of the id property (only used for {@link ValidityAuditStrategy})
	 * @param revisionPropertyPath path of the revision property (only used for {@link ValidityAuditStrategy})
	 * @param originalIdPropertyName name of the id property (only used for {@link ValidityAuditStrategy})
	 * @param alias1 an alias used for subqueries (only used for {@link DefaultAuditStrategy})
	 * @param componentDatas information about the middle-entity relation
	 */
	void addAssociationAtRevisionRestriction(QueryBuilder rootQueryBuilder,  String revisionProperty, 
			String revisionEndProperty, boolean addAlias, MiddleIdData referencingIdData, 
			String versionsMiddleEntityName, String eeOriginalIdPropertyPath, String revisionPropertyPath,
          String originalIdPropertyName, String alias1, MiddleComponentData... componentDatas);

}
