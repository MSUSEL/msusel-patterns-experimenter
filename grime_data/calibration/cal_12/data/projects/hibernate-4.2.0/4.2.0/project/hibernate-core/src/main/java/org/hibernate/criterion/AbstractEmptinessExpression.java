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
import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.QueryException;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.engine.spi.TypedValue;
import org.hibernate.persister.collection.QueryableCollection;
import org.hibernate.persister.entity.Loadable;
import org.hibernate.persister.entity.PropertyMapping;
import org.hibernate.sql.ConditionFragment;
import org.hibernate.type.CollectionType;
import org.hibernate.type.Type;

/**
 * Implementation of AbstractEmptinessExpression.
 *
 * @author Steve Ebersole
 */
public abstract class AbstractEmptinessExpression implements Criterion {

	private static final TypedValue[] NO_VALUES = new TypedValue[0];

	protected final String propertyName;

	protected AbstractEmptinessExpression(String propertyName) {
		this.propertyName = propertyName;
	}

	protected abstract boolean excludeEmpty();

	public final String toSqlString(Criteria criteria, CriteriaQuery criteriaQuery) throws HibernateException {
		String entityName = criteriaQuery.getEntityName( criteria, propertyName );
		String actualPropertyName = criteriaQuery.getPropertyName( propertyName );
		String sqlAlias = criteriaQuery.getSQLAlias( criteria, propertyName );

		SessionFactoryImplementor factory = criteriaQuery.getFactory();
		QueryableCollection collectionPersister = getQueryableCollection( entityName, actualPropertyName, factory );

		String[] collectionKeys = collectionPersister.getKeyColumnNames();
		String[] ownerKeys = ( ( Loadable ) factory.getEntityPersister( entityName ) ).getIdentifierColumnNames();

		String innerSelect = "(select 1 from " + collectionPersister.getTableName()
		        + " where "
		        + new ConditionFragment().setTableAlias( sqlAlias ).setCondition( ownerKeys, collectionKeys ).toFragmentString()
		        + ")";

		return excludeEmpty()
		        ? "exists " + innerSelect
		        : "not exists " + innerSelect;
	}


	protected QueryableCollection getQueryableCollection(String entityName, String propertyName, SessionFactoryImplementor factory)
	        throws HibernateException {
		PropertyMapping ownerMapping = ( PropertyMapping ) factory.getEntityPersister( entityName );
		Type type = ownerMapping.toType( propertyName );
		if ( !type.isCollectionType() ) {
			throw new MappingException(
			        "Property path [" + entityName + "." + propertyName + "] does not reference a collection"
			);
		}

		String role = ( ( CollectionType ) type ).getRole();
		try {
			return ( QueryableCollection ) factory.getCollectionPersister( role );
		}
		catch ( ClassCastException cce ) {
			throw new QueryException( "collection role is not queryable: " + role );
		}
		catch ( Exception e ) {
			throw new QueryException( "collection role not found: " + role );
		}
	}

	public final TypedValue[] getTypedValues(Criteria criteria, CriteriaQuery criteriaQuery)
	        throws HibernateException {
		return NO_VALUES;
	}

	public final String toString() {
		return propertyName + ( excludeEmpty() ? " is not empty" : " is empty" );
	}
}
