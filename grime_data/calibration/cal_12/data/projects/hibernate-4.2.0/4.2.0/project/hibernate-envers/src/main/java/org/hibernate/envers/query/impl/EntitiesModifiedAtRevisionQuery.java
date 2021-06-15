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

import org.hibernate.Query;
import org.hibernate.envers.configuration.AuditConfiguration;
import org.hibernate.envers.configuration.AuditEntitiesConfiguration;
import org.hibernate.envers.query.criteria.AuditCriterion;
import org.hibernate.envers.reader.AuditReaderImplementor;

/**
 * In comparison to {@link EntitiesAtRevisionQuery} this query returns an empty collection if an entity
 * of a certain type has not been changed in a given revision.
 * @see EntitiesAtRevisionQuery
 * @author Lukasz Antoniak (lukasz dot antoniak at gmail dot com)
 */
public class EntitiesModifiedAtRevisionQuery extends AbstractAuditQuery {
    private final Number revision;

    public EntitiesModifiedAtRevisionQuery(AuditConfiguration verCfg, AuditReaderImplementor versionsReader,
                                           Class<?> cls, Number revision) {
        super(verCfg, versionsReader, cls);
        this.revision = revision;
    }

    public EntitiesModifiedAtRevisionQuery(AuditConfiguration verCfg, AuditReaderImplementor versionsReader,
                                           Class<?> cls, String entityName, Number revision) {
        super(verCfg, versionsReader, cls, entityName);
        this.revision = revision;
    }

    @SuppressWarnings({"unchecked"})
    public List list() {
        /*
         * The query that we need to create:
         *   SELECT new list(e) FROM versionsReferencedEntity e
         *   WHERE
         * (all specified conditions, transformed, on the "e" entity) AND
         * e.revision = :revision
         */
        AuditEntitiesConfiguration verEntCfg = verCfg.getAuditEntCfg();
        String revisionPropertyPath = verEntCfg.getRevisionNumberPath();
        qb.getRootParameters().addWhereWithParam(revisionPropertyPath, "=", revision);

        // all specified conditions
        for (AuditCriterion criterion : criterions) {
            criterion.addToQuery(verCfg, versionsReader, entityName, qb, qb.getRootParameters());
        }

        Query query = buildQuery();
        List queryResult = query.list();

        if (hasProjection) {
            return queryResult;
        } else {
            List result = new ArrayList();
            entityInstantiator.addInstancesFromVersionsEntities(entityName, result, queryResult, revision);

            return result;
        }
    }
}