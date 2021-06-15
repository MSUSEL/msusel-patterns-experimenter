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
package org.hibernate.metamodel.source;

import org.hibernate.cache.spi.access.AccessType;

/**
 * Defines a (contextual) set of values to use as defaults in the absence of related mapping information.  The
 * context here is conceptually a stack.  The "global" level is configuration settings.
 *
 * @author Steve Ebersole
 * @author Gail Badner
 */
public interface MappingDefaults {
	/**
	 * Identifies the default package name to use if none specified in the mapping.  Really only pertinent for
	 * {@code hbm.xml} mappings.
	 *
	 * @return The default package name.
	 */
	public String getPackageName();

	/**
	 * Identifies the default database schema name to use if none specified in the mapping.
	 *
	 * @return The default schema name
	 */
	public String getSchemaName();

	/**
	 * Identifies the default database catalog name to use if none specified in the mapping.
	 *
	 * @return The default catalog name
	 */
	public String getCatalogName();

	/**
	 * Identifies the default column name to use for the identifier column if none specified in the mapping.
	 *
	 * @return The default identifier column name
	 */
	public String getIdColumnName();

	/**
	 * Identifies the default column name to use for the discriminator column if none specified in the mapping.
	 *
	 * @return The default discriminator column name
	 */
	public String getDiscriminatorColumnName();

	/**
	 * Identifies the default cascade style to apply to associations if none specified in the mapping.
	 *
	 * @return The default cascade style
	 */
	public String getCascadeStyle();

	/**
	 * Identifies the default {@link org.hibernate.property.PropertyAccessor} name to use if none specified in the
	 * mapping.
	 *
	 * @return The default property accessor name
	 * @see org.hibernate.property.PropertyAccessorFactory
	 */
	public String getPropertyAccessorName();

	/**
	 * Identifies whether associations are lazy by default if not specified in the mapping.
	 *
	 * @return The default association laziness
	 */
	public boolean areAssociationsLazy();

	/**
	 * The default cache access type to use
	 *
	 * @return The default cache access type.
	 */
	public AccessType getCacheAccessType();
}
