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
package org.hibernate.envers.query;

import org.hibernate.criterion.Restrictions;
import org.hibernate.envers.RevisionType;
import org.hibernate.envers.query.criteria.AuditConjunction;
import org.hibernate.envers.query.criteria.AuditCriterion;
import org.hibernate.envers.query.criteria.AuditDisjunction;
import org.hibernate.envers.query.criteria.AuditId;
import org.hibernate.envers.query.criteria.AuditProperty;
import org.hibernate.envers.query.criteria.AuditRelatedId;
import org.hibernate.envers.query.criteria.LogicalAuditExpression;
import org.hibernate.envers.query.criteria.NotAuditExpression;
import org.hibernate.envers.query.property.EntityPropertyName;
import org.hibernate.envers.query.property.RevisionNumberPropertyName;
import org.hibernate.envers.query.property.RevisionPropertyPropertyName;
import org.hibernate.envers.query.property.RevisionTypePropertyName;

/**
 * TODO: ilike
 * @author Adam Warski (adam at warski dot org)
 * @see Restrictions
 */
@SuppressWarnings({"JavaDoc"})
public class AuditEntity {
    private AuditEntity() { }

    public static AuditId id() {
        return new AuditId();
    }

    /**
     * Create restrictions, projections and specify order for a property of an audited entity.
     * @param propertyName Name of the property.
     */
    public static AuditProperty<Object> property(String propertyName) {
        return new AuditProperty<Object>(new EntityPropertyName(propertyName));
    }

   /**
     * Create restrictions, projections and specify order for the revision number, corresponding to an
     * audited entity.
     */
    public static AuditProperty<Number> revisionNumber() {
        return new AuditProperty<Number>(new RevisionNumberPropertyName());
    }

    /**
     * Create restrictions, projections and specify order for a property of the revision entity,
     * corresponding to an audited entity.
     * @param propertyName Name of the property.
     */
    public static AuditProperty<Object> revisionProperty(String propertyName) {
        return new AuditProperty<Object>(new RevisionPropertyPropertyName(propertyName));
    }

    /**
     * Create restrictions, projections and specify order for the revision type, corresponding to an
     * audited entity.
     */
    public static AuditProperty<RevisionType> revisionType() {
        return new AuditProperty<RevisionType>(new RevisionTypePropertyName());
    }

    /**
	 * Create restrictions on an id of a related entity.
     * @param propertyName Name of the property, which is the relation.
	 */
	public static AuditRelatedId relatedId(String propertyName) {
		return new AuditRelatedId(new EntityPropertyName(propertyName));
	}

    /**
	 * Return the conjuction of two criterions.
	 */
	public static AuditCriterion and(AuditCriterion lhs, AuditCriterion rhs) {
		return new LogicalAuditExpression(lhs, rhs, "and");
	}

    /**
	 * Return the disjuction of two criterions.
	 */
	public static AuditCriterion or(AuditCriterion lhs, AuditCriterion rhs) {
		return new LogicalAuditExpression(lhs, rhs, "or");
	}

    /**
	 * Return the negation of a criterion.
	 */
	public static AuditCriterion not(AuditCriterion expression) {
		return new NotAuditExpression(expression);
	}

	/**
	 * Group criterions together in a single conjunction (A and B and C...).
	 */
	public static AuditConjunction conjunction() {
		return new AuditConjunction();
	}

	/**
	 * Group criterions together in a single disjunction (A or B or C...).
	 */
	public static AuditDisjunction disjunction() {
		return new AuditDisjunction();
	}
}
