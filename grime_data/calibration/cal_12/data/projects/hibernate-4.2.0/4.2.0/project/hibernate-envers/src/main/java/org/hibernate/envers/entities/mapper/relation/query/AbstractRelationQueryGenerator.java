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
package org.hibernate.envers.entities.mapper.relation.query;

import org.hibernate.Query;
import org.hibernate.envers.RevisionType;
import org.hibernate.envers.configuration.AuditEntitiesConfiguration;
import org.hibernate.envers.entities.mapper.id.QueryParameterData;
import org.hibernate.envers.entities.mapper.relation.MiddleIdData;
import org.hibernate.envers.reader.AuditReaderImplementor;

import static org.hibernate.envers.entities.mapper.relation.query.QueryConstants.DEL_REVISION_TYPE_PARAMETER;
import static org.hibernate.envers.entities.mapper.relation.query.QueryConstants.REVISION_PARAMETER;

/**
 * Base class for implementers of {@code RelationQueryGenerator} contract.
 *
 * @author Lukasz Antoniak (lukasz dot antoniak at gmail dot com)
 */
public abstract class AbstractRelationQueryGenerator implements RelationQueryGenerator {
	protected final AuditEntitiesConfiguration verEntCfg;
	protected final MiddleIdData referencingIdData;
	protected final boolean revisionTypeInId;

	protected AbstractRelationQueryGenerator(AuditEntitiesConfiguration verEntCfg, MiddleIdData referencingIdData,
											 boolean revisionTypeInId) {
		this.verEntCfg = verEntCfg;
		this.referencingIdData = referencingIdData;
		this.revisionTypeInId = revisionTypeInId;
	}

	protected abstract String getQueryString();

	public Query getQuery(AuditReaderImplementor versionsReader, Object primaryKey, Number revision) {
		Query query = versionsReader.getSession().createQuery( getQueryString() );
		query.setParameter( REVISION_PARAMETER, revision );
		query.setParameter( DEL_REVISION_TYPE_PARAMETER, RevisionType.DEL );
		for ( QueryParameterData paramData : referencingIdData.getPrefixedMapper().mapToQueryParametersFromId( primaryKey ) ) {
			paramData.setParameterValue( query );
		}
		return query;
	}

	protected String getRevisionTypePath() {
		return revisionTypeInId
				? verEntCfg.getOriginalIdPropName() + "." + verEntCfg.getRevisionTypePropName()
				: verEntCfg.getRevisionTypePropName();
	}
}
