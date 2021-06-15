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
package org.hibernate.envers.query.criteria;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.envers.configuration.AuditConfiguration;
import org.hibernate.envers.query.property.PropertyNameGetter;
import org.hibernate.envers.reader.AuditReaderImplementor;
import org.hibernate.envers.tools.query.Parameters;
import org.hibernate.envers.tools.query.QueryBuilder;

/**
 * @author Adam Warski (adam at warski dot org)
 */
public class AggregatedAuditExpression implements AuditCriterion, ExtendableCriterion {
    private PropertyNameGetter propertyNameGetter;
    private AggregatedMode mode;
    private List<AuditCriterion> criterions;

    public AggregatedAuditExpression(PropertyNameGetter propertyNameGetter, AggregatedMode mode) {
        this.propertyNameGetter = propertyNameGetter;
        this.mode = mode;
        criterions = new ArrayList<AuditCriterion>();
    }

    public static enum AggregatedMode {
        MAX,
        MIN
    }

    public AggregatedAuditExpression add(AuditCriterion criterion) {
        criterions.add(criterion);
        return this;
    }

    public void addToQuery(AuditConfiguration auditCfg, AuditReaderImplementor versionsReader, String entityName,
						   QueryBuilder qb, Parameters parameters) {
        String propertyName = CriteriaTools.determinePropertyName( auditCfg, versionsReader, entityName, propertyNameGetter );

        CriteriaTools.checkPropertyNotARelation(auditCfg, entityName, propertyName);

        // Make sure our conditions are ANDed together even if the parent Parameters have a different connective
        Parameters subParams = parameters.addSubParameters(Parameters.AND);
        // This will be the aggregated query, containing all the specified conditions
        QueryBuilder subQb = qb.newSubQueryBuilder();

        // Adding all specified conditions both to the main query, as well as to the
        // aggregated one.
        for (AuditCriterion versionsCriteria : criterions) {
            versionsCriteria.addToQuery(auditCfg, versionsReader, entityName, qb, subParams);
            versionsCriteria.addToQuery(auditCfg, versionsReader, entityName, subQb, subQb.getRootParameters());
        }

        // Setting the desired projection of the aggregated query
        switch (mode) {
            case MIN:
                subQb.addProjection("min", propertyName, false);
                break;
            case MAX:
                subQb.addProjection("max", propertyName, false);
        }

        // Adding the constrain on the result of the aggregated criteria
        subParams.addWhere(propertyName, "=", subQb);
    }
}