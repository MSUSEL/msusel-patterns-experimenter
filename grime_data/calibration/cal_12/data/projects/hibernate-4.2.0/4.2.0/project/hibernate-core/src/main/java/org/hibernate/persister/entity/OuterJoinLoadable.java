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
package org.hibernate.persister.entity;
import org.hibernate.FetchMode;
import org.hibernate.engine.spi.CascadeStyle;
import org.hibernate.type.EntityType;
import org.hibernate.type.Type;

/**
 * A <tt>EntityPersister</tt> that may be loaded by outer join using
 * the <tt>OuterJoinLoader</tt> hierarchy and may be an element
 * of a one-to-many association.
 *
 * @see org.hibernate.loader.OuterJoinLoader
 * @author Gavin King
 */
public interface OuterJoinLoadable extends Loadable, Joinable {

	/**
	 * Generate a list of collection index, key and element columns
	 */
	public String selectFragment(String alias, String suffix);
	/**
	 * How many properties are there, for this class and all subclasses?
	 */
	public int countSubclassProperties();

	/**
	 * May this (subclass closure) property be fetched using an SQL outerjoin?
	 */
	public FetchMode getFetchMode(int i);
	/**
	 * Get the cascade style of this (subclass closure) property
	 */
	public CascadeStyle getCascadeStyle(int i);

	/**
	 * Is this property defined on a subclass of the mapped class.
	 */
	public boolean isDefinedOnSubclass(int i);

	/**
	 * Get the type of the numbered property of the class or a subclass.
	 */
	public Type getSubclassPropertyType(int i);

	/**
	 * Get the name of the numbered property of the class or a subclass.
	 */
	public String getSubclassPropertyName(int i);
	
	/**
	 * Is the numbered property of the class of subclass nullable?
	 */
	public boolean isSubclassPropertyNullable(int i);

	/**
	 * Return the column names used to persist the numbered property of the
	 * class or a subclass.
	 */
	public String[] getSubclassPropertyColumnNames(int i);

	/**
	 * Return the table name used to persist the numbered property of the
	 * class or a subclass.
	 */
	public String getSubclassPropertyTableName(int i);
	/**
	 * Given the number of a property of a subclass, and a table alias,
	 * return the aliased column names.
	 */
	public String[] toColumns(String name, int i);

	/**
	 * Get the main from table fragment, given a query alias.
	 */
	public String fromTableFragment(String alias);

	/**
	 * Get the column names for the given property path
	 */
	public String[] getPropertyColumnNames(String propertyPath);
	/**
	 * Get the table name for the given property path
	 */
	public String getPropertyTableName(String propertyName);
	
	public EntityType getEntityType();

}
