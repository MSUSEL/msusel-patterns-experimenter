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
import org.hibernate.envers.configuration.AuditConfiguration;
import org.hibernate.envers.query.property.PropertyNameGetter;
import org.hibernate.envers.reader.AuditReaderImplementor;
import org.hibernate.envers.tools.query.Parameters;
import org.hibernate.envers.tools.query.QueryBuilder;

/**
 * @author Adam Warski (adam at warski dot org)
 */
public class BetweenAuditExpression implements AuditCriterion {
    private PropertyNameGetter propertyNameGetter;
    private Object lo;
    private Object hi;

    public BetweenAuditExpression(PropertyNameGetter propertyNameGetter, Object lo, Object hi) {
        this.propertyNameGetter = propertyNameGetter;
        this.lo = lo;
        this.hi = hi;
    }

    public void addToQuery(AuditConfiguration auditCfg, AuditReaderImplementor versionsReader, String entityName,
						   QueryBuilder qb, Parameters parameters) {
		String propertyName = CriteriaTools.determinePropertyName( auditCfg, versionsReader, entityName, propertyNameGetter );
        CriteriaTools.checkPropertyNotARelation(auditCfg, entityName, propertyName);
        
        Parameters subParams = parameters.addSubParameters(Parameters.AND);
        subParams.addWhereWithParam(propertyName, ">=", lo);
        subParams.addWhereWithParam(propertyName, "<=", hi);
    }
}
