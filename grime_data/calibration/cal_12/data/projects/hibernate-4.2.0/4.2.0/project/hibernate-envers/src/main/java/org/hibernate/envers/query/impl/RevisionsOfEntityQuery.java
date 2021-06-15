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
package org.hibernate.envers.query.impl;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.envers.RevisionType;
import org.hibernate.envers.configuration.AuditConfiguration;
import org.hibernate.envers.configuration.AuditEntitiesConfiguration;
import org.hibernate.envers.exception.AuditException;
import org.hibernate.envers.query.criteria.AuditCriterion;
import org.hibernate.envers.reader.AuditReaderImplementor;
import org.hibernate.proxy.HibernateProxy;

/**
 * @author Adam Warski (adam at warski dot org)
 * @author HernпїЅn Chanfreau
 */
public class RevisionsOfEntityQuery extends AbstractAuditQuery {
    private final boolean selectEntitiesOnly;
    private final boolean selectDeletedEntities;

    public RevisionsOfEntityQuery(AuditConfiguration verCfg,
                                  AuditReaderImplementor versionsReader,
                                  Class<?> cls, boolean selectEntitiesOnly,
                                  boolean selectDeletedEntities) {
        super(verCfg, versionsReader, cls);

        this.selectEntitiesOnly = selectEntitiesOnly;
        this.selectDeletedEntities = selectDeletedEntities;
    }

	public RevisionsOfEntityQuery(AuditConfiguration verCfg,
			AuditReaderImplementor versionsReader, Class<?> cls, String entityName,
			boolean selectEntitiesOnly, boolean selectDeletedEntities) {
		super(verCfg, versionsReader, cls, entityName);

		this.selectEntitiesOnly = selectEntitiesOnly;
		this.selectDeletedEntities = selectDeletedEntities;
	}
	
    private Number getRevisionNumber(Map versionsEntity) {
        AuditEntitiesConfiguration verEntCfg = verCfg.getAuditEntCfg();

        String originalId = verEntCfg.getOriginalIdPropName();
        String revisionPropertyName = verEntCfg.getRevisionFieldName();

        Object revisionInfoObject = ((Map) versionsEntity.get(originalId)).get(revisionPropertyName);

        if (revisionInfoObject instanceof HibernateProxy) {
            return (Number) ((HibernateProxy) revisionInfoObject).getHibernateLazyInitializer().getIdentifier();
        } else {
            // Not a proxy - must be read from cache or with a join
            return verCfg.getRevisionInfoNumberReader().getRevisionNumber(revisionInfoObject);   
        }
    }

    @SuppressWarnings({"unchecked"})
    public List list() throws AuditException {
        AuditEntitiesConfiguration verEntCfg = verCfg.getAuditEntCfg();

        /*
        The query that should be executed in the versions table:
        SELECT e (unless another projection is specified) FROM ent_ver e, rev_entity r WHERE
          e.revision_type != DEL (if selectDeletedEntities == false) AND
          e.revision = r.revision AND
          (all specified conditions, transformed, on the "e" entity)
          ORDER BY e.revision ASC (unless another order or projection is specified)
         */      
        if (!selectDeletedEntities) {
            // e.revision_type != DEL AND
            qb.getRootParameters().addWhereWithParam(verEntCfg.getRevisionTypePropName(), "<>", RevisionType.DEL);
        }

        // all specified conditions, transformed
        for (AuditCriterion criterion : criterions) {
            criterion.addToQuery(verCfg, versionsReader, entityName, qb, qb.getRootParameters());
        }

        if (!hasProjection && !hasOrder) {
            String revisionPropertyPath = verEntCfg.getRevisionNumberPath();
            qb.addOrder(revisionPropertyPath, true);
        }

        if (!selectEntitiesOnly) {
            qb.addFrom(verCfg.getAuditEntCfg().getRevisionInfoEntityName(), "r");
            qb.getRootParameters().addWhere(verCfg.getAuditEntCfg().getRevisionNumberPath(), true, "=", "r.id", false);
        }

        List<Object> queryResult = buildAndExecuteQuery();
        if (hasProjection) {
            return queryResult;
        } else {
            List entities = new ArrayList();
            String revisionTypePropertyName = verEntCfg.getRevisionTypePropName();

            for (Object resultRow : queryResult) {
                Map versionsEntity;
                Object revisionData;

                if (selectEntitiesOnly) {
                    versionsEntity = (Map) resultRow;
                    revisionData = null;
                } else {
                    Object[] arrayResultRow = (Object[]) resultRow;
                    versionsEntity = (Map) arrayResultRow[0];
                    revisionData = arrayResultRow[1];
                }

                Number revision = getRevisionNumber(versionsEntity);
                
                Object entity = entityInstantiator.createInstanceFromVersionsEntity(entityName, versionsEntity, revision);

                if (!selectEntitiesOnly) {
                    entities.add(new Object[] { entity, revisionData, versionsEntity.get(revisionTypePropertyName) });
                } else {
                    entities.add(entity);
                }
            }

            return entities;
        }
    }
}
