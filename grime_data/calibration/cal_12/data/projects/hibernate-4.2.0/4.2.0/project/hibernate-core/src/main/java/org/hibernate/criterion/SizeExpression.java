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
package org.hibernate.criterion;

import org.hibernate.Criteria;
import org.hibernate.EntityMode;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.TypedValue;
import org.hibernate.persister.collection.QueryableCollection;
import org.hibernate.persister.entity.Loadable;
import org.hibernate.sql.ConditionFragment;
import org.hibernate.type.StandardBasicTypes;

/**
 * @author Gavin King
 */
public class SizeExpression implements Criterion {
	
	private final String propertyName;
	private final int size;
	private final String op;
	
	protected SizeExpression(String propertyName, int size, String op) {
		this.propertyName = propertyName;
		this.size = size;
		this.op = op;
	}

	public String toString() {
		return propertyName + ".size" + op + size;
	}

	public String toSqlString(Criteria criteria, CriteriaQuery criteriaQuery)
	throws HibernateException {
		String role = criteriaQuery.getEntityName(criteria, propertyName) + 
				'.' +  
				criteriaQuery.getPropertyName(propertyName);
		QueryableCollection cp = (QueryableCollection) criteriaQuery.getFactory()
				.getCollectionPersister(role);
		//String[] fk = StringHelper.qualify( "collection_", cp.getKeyColumnNames() );
		String[] fk = cp.getKeyColumnNames();
		String[] pk = ( (Loadable) cp.getOwnerEntityPersister() ).getIdentifierColumnNames(); //TODO: handle property-ref
		return "? " + 
				op + 
				" (select count(*) from " +
				cp.getTableName() +
				//" collection_ where " +
				" where " +
				new ConditionFragment()
						.setTableAlias( criteriaQuery.getSQLAlias(criteria, propertyName) )
						.setCondition(pk, fk)
						.toFragmentString() +
				")";
	}

	public TypedValue[] getTypedValues(Criteria criteria, CriteriaQuery criteriaQuery) 
	throws HibernateException {
		return new TypedValue[] {
			new TypedValue( StandardBasicTypes.INTEGER, size, EntityMode.POJO )
		};
	}

}
