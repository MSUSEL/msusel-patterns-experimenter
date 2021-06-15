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
import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.type.Type;

/**
 * Implemented by a <tt>EntityPersister</tt> that may be loaded
 * using <tt>Loader</tt>.
 *
 * @see org.hibernate.loader.Loader
 * @author Gavin King
 */
public interface Loadable extends EntityPersister {
	
	public static final String ROWID_ALIAS = "rowid_";

	/**
	 * Does this persistent class have subclasses?
	 */
	public boolean hasSubclasses();

	/**
	 * Get the discriminator type
	 */
	public Type getDiscriminatorType();

	/**
	 * Get the discriminator value
	 */
	public Object getDiscriminatorValue();

	/**
	 * Get the concrete subclass corresponding to the given discriminator
	 * value
	 */
	public String getSubclassForDiscriminatorValue(Object value);

	/**
	 * Get the names of columns used to persist the identifier
	 */
	public String[] getIdentifierColumnNames();

	/**
	 * Get the result set aliases used for the identifier columns, given a suffix
	 */
	public String[] getIdentifierAliases(String suffix);
	/**
	 * Get the result set aliases used for the property columns, given a suffix (properties of this class, only).
	 */
	public String[] getPropertyAliases(String suffix, int i);
	
	/**
	 * Get the result set column names mapped for this property (properties of this class, only).
	 */
	public String[] getPropertyColumnNames(int i);
	
	/**
	 * Get the result set aliases used for the identifier columns, given a suffix
	 */
	public String getDiscriminatorAlias(String suffix);
	
	/**
	 * @return the column name for the discriminator as specified in the mapping.
	 */
	public String getDiscriminatorColumnName();
	
	/**
	 * Does the result set contain rowids?
	 */
	public boolean hasRowId();
	
	/**
	 * Retrieve property values from one row of a result set
	 */
	public Object[] hydrate(
			ResultSet rs,
			Serializable id,
			Object object,
			Loadable rootLoadable,
			String[][] suffixedPropertyColumns,
			boolean allProperties, 
			SessionImplementor session)
	throws SQLException, HibernateException;

	public boolean isAbstract();

	/**
	 * Register the name of a fetch profile determined to have an affect on the
	 * underlying loadable in regards to the fact that the underlying load SQL
	 * needs to be adjust when the given fetch profile is enabled.
	 * 
	 * @param fetchProfileName The name of the profile affecting this.
	 */
	public void registerAffectingFetchProfile(String fetchProfileName);

	/**
	 * Given a column name and the root table alias in use for the entity hierarchy, determine the proper table alias
	 * for the table in that hierarchy that contains said column.
	 * <p/>
	 * NOTE : Generally speaking the column is not validated to exist.  Most implementations simply return the
	 * root alias; the exception is {@link JoinedSubclassEntityPersister}
	 *
	 * @param columnName The column name
	 * @param rootAlias The hierarchy root alias
	 *
	 * @return The proper table alias for qualifying the given column.
	 */
	public String getTableAliasForColumn(String columnName, String rootAlias);
}
