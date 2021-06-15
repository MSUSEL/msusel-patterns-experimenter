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
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.engine.spi.TypedValue;
import org.hibernate.type.Type;

/**
 * An instance of <tt>CriteriaQuery</tt> is passed to criterion, 
 * order and projection instances when actually compiling and
 * executing the query. This interface is not used by application
 * code.
 * 
 * @author Gavin King
 */
public interface CriteriaQuery {
	public SessionFactoryImplementor getFactory();
	
	/**
	 * Get the names of the columns mapped by a property path,
	 * ignoring projection aliases
	 * @throws org.hibernate.QueryException if the property maps to more than 1 column
	 */
	public String getColumn(Criteria criteria, String propertyPath) 
	throws HibernateException;
	
	/**
	 * Get the names of the columns mapped by a property path,
	 * ignoring projection aliases
	 */
	public String[] getColumns(String propertyPath, Criteria criteria)
	throws HibernateException;

	/**
	 * Get the names of the columns mapped by a property path; if the
	 * property path is not found in criteria, try the "outer" query.
	 * Projection aliases are ignored.
	 */
	public String[] findColumns(String propertyPath, Criteria criteria)
	throws HibernateException;

	/**
	 * Get the type of a property path, ignoring projection aliases
	 */
	public Type getType(Criteria criteria, String propertyPath)
	throws HibernateException;

	/**
	 * Get the names of the columns mapped by a property path
	 */
	public String[] getColumnsUsingProjection(Criteria criteria, String propertyPath) 
	throws HibernateException;

	/**
	 * Get the type of a property path
	 */
	public Type getTypeUsingProjection(Criteria criteria, String propertyPath)
	throws HibernateException;

	/**
	 * Get the a typed value for the given property value.
	 */
	public TypedValue getTypedValue(Criteria criteria, String propertyPath, Object value)
	throws HibernateException;
	
	/**
	 * Get the entity name of an entity
	 */
	public String getEntityName(Criteria criteria);
	
	/**
	 * Get the entity name of an entity, taking into account
	 * the qualifier of the property path
	 */
	public String getEntityName(Criteria criteria, String propertyPath);

	/**
	 * Get the root table alias of an entity
	 */
	public String getSQLAlias(Criteria subcriteria);

	/**
	 * Get the root table alias of an entity, taking into account
	 * the qualifier of the property path
	 */
	public String getSQLAlias(Criteria criteria, String propertyPath);
	
	/**
	 * Get the property name, given a possibly qualified property name
	 */
	public String getPropertyName(String propertyName);
	
	/**
	 * Get the identifier column names of this entity
	 */
	public String[] getIdentifierColumns(Criteria subcriteria);
	
	/**
	 * Get the identifier type of this entity
	 */
	public Type getIdentifierType(Criteria subcriteria);
	
	public TypedValue getTypedIdentifierValue(Criteria subcriteria, Object value);
	
	public String generateSQLAlias();
}