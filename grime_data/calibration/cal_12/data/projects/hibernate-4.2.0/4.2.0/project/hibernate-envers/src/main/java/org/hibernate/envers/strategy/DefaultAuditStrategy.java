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
import org.hibernate.envers.configuration.AuditConfiguration;
import org.hibernate.envers.configuration.GlobalConfiguration;
import org.hibernate.envers.entities.mapper.PersistentCollectionChangeData;
import org.hibernate.envers.entities.mapper.relation.MiddleComponentData;
import org.hibernate.envers.entities.mapper.relation.MiddleIdData;
import org.hibernate.envers.synchronization.SessionCacheCleaner;
import org.hibernate.envers.tools.query.Parameters;
import org.hibernate.envers.tools.query.QueryBuilder;

import static org.hibernate.envers.entities.mapper.relation.query.QueryConstants.MIDDLE_ENTITY_ALIAS_DEF_AUD_STR;
import static org.hibernate.envers.entities.mapper.relation.query.QueryConstants.REVISION_PARAMETER;

/**
 * Default strategy is to simply persist the audit data.
 *
 * @author Adam Warski
 * @author Stephanie Pau
 */
public class DefaultAuditStrategy implements AuditStrategy {
    private final SessionCacheCleaner sessionCacheCleaner;

    public DefaultAuditStrategy() {
        sessionCacheCleaner = new SessionCacheCleaner();
    }

    public void perform(Session session, String entityName, AuditConfiguration auditCfg, Serializable id, Object data,
                        Object revision) {
        session.save(auditCfg.getAuditEntCfg().getAuditEntityName(entityName), data);
        sessionCacheCleaner.scheduleAuditDataRemoval(session, data);
    }

    public void performCollectionChange(Session session, String entityName, String propertyName, AuditConfiguration auditCfg,
                                        PersistentCollectionChangeData persistentCollectionChangeData, Object revision) {
        session.save(persistentCollectionChangeData.getEntityName(), persistentCollectionChangeData.getData());
        sessionCacheCleaner.scheduleAuditDataRemoval(session, persistentCollectionChangeData.getData());
    }

    
	public void addEntityAtRevisionRestriction(GlobalConfiguration globalCfg, QueryBuilder rootQueryBuilder, String revisionProperty,
			String revisionEndProperty, boolean addAlias, MiddleIdData idData, String revisionPropertyPath, 
			String originalIdPropertyName, String alias1, String alias2) {
		Parameters rootParameters = rootQueryBuilder.getRootParameters();
		
		// create a subquery builder
        // SELECT max(e.revision) FROM versionsReferencedEntity e2
        QueryBuilder maxERevQb = rootQueryBuilder.newSubQueryBuilder(idData.getAuditEntityName(), alias2);
        maxERevQb.addProjection("max", revisionPropertyPath, false);
        // WHERE
        Parameters maxERevQbParameters = maxERevQb.getRootParameters();
        // e2.revision <= :revision
        maxERevQbParameters.addWhereWithNamedParam(revisionPropertyPath, "<=", REVISION_PARAMETER);
        // e2.id_ref_ed = e.id_ref_ed
        idData.getOriginalMapper().addIdsEqualToQuery(maxERevQbParameters,
                alias1 + "." + originalIdPropertyName, alias2 +"." + originalIdPropertyName);
		
		// add subquery to rootParameters
        String subqueryOperator = globalCfg.getCorrelatedSubqueryOperator();
		rootParameters.addWhere(revisionProperty, addAlias, subqueryOperator, maxERevQb);
	}

	public void addAssociationAtRevisionRestriction(QueryBuilder rootQueryBuilder,  String revisionProperty, 
	          String revisionEndProperty, boolean addAlias, MiddleIdData referencingIdData, String versionsMiddleEntityName,
	          String eeOriginalIdPropertyPath, String revisionPropertyPath,
	          String originalIdPropertyName, String alias1, MiddleComponentData... componentDatas) {
		Parameters rootParameters = rootQueryBuilder.getRootParameters();

    	// SELECT max(ee2.revision) FROM middleEntity ee2
        QueryBuilder maxEeRevQb = rootQueryBuilder.newSubQueryBuilder(versionsMiddleEntityName, MIDDLE_ENTITY_ALIAS_DEF_AUD_STR);
        maxEeRevQb.addProjection("max", revisionPropertyPath, false);
        // WHERE
        Parameters maxEeRevQbParameters = maxEeRevQb.getRootParameters();
        // ee2.revision <= :revision
        maxEeRevQbParameters.addWhereWithNamedParam(revisionPropertyPath, "<=", REVISION_PARAMETER);
        // ee2.originalId.* = ee.originalId.*
        String ee2OriginalIdPropertyPath = MIDDLE_ENTITY_ALIAS_DEF_AUD_STR + "." + originalIdPropertyName;
        referencingIdData.getPrefixedMapper().addIdsEqualToQuery(maxEeRevQbParameters, eeOriginalIdPropertyPath, ee2OriginalIdPropertyPath);
        for (MiddleComponentData componentData : componentDatas) {
            componentData.getComponentMapper().addMiddleEqualToQuery(maxEeRevQbParameters, eeOriginalIdPropertyPath, alias1, ee2OriginalIdPropertyPath, MIDDLE_ENTITY_ALIAS_DEF_AUD_STR);
        }

		// add subquery to rootParameters
        rootParameters.addWhere(revisionProperty, addAlias, "=", maxEeRevQb);
	}

}
