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
package org.hibernate;
import java.util.Properties;

import org.hibernate.type.BasicType;
import org.hibernate.type.Type;

/**
 * Provides access to the various {@link Type} instances associated with the {@link SessionFactory}.
 * <p/>
 * This is intended for use by application developers.
 *
 * @author Steve Ebersole
 */
public interface TypeHelper {
	/**
	 * Retrieve the basic type registered against the given name.
	 *
	 * @param name The name of the basic type to retrieve
	 *
	 * @return The basic type, or null.
	 */
	public BasicType basic(String name);

	/**
	 * Convenience form of {@link #basic(String)}.  The intended use of this is something like
	 * {@code basic(Integer.class)} or {@code basic(int.class)}
	 *
	 * @param javaType The java type for which to retrieve the type instance.
	 *
	 * @return The basic type, or null.
	 */
	public BasicType basic(Class javaType);

	/**
	 * Uses heuristics to deduce the proper {@link Type} given a string naming the type or Java class.
	 * <p/>
	 * See {@link org.hibernate.type.TypeResolver#heuristicType(java.lang.String)} for a discussion of the
	 * heuristic algorithm.
	 *
	 * @param name The name of the type or Java class
	 *
	 * @return The deduced type, or null.
	 *
	 * @see org.hibernate.type.TypeResolver#heuristicType(java.lang.String)
	 */
	public Type heuristicType(String name);

	/**
	 * Retrieve a type representing the given entity.
	 *
	 * @param entityClass The entity Java type.
	 *
	 * @return The type, or null
	 */
	public Type entity(Class entityClass);

	/**
	 * Retrieve a type representing the given entity.
	 *
	 * @param entityName The entity name.
	 *
	 * @return The type, or null
	 */
	public Type entity(String entityName);

	/**
	 * Retrieve the type for the given user-type class ({@link org.hibernate.usertype.UserType} or
	 * {@link org.hibernate.usertype.CompositeUserType}).
	 *
	 * @param userTypeClass The user type class
	 *
	 * @return The type, or null
	 */
	public Type custom(Class userTypeClass);

	/**
	 * Retrieve the type for the given user-type class ({@link org.hibernate.usertype.UserType} or
	 * {@link org.hibernate.usertype.CompositeUserType}).
	 *
	 * @param userTypeClass The user type class
	 * @param properties Configuration properties.
	 *
	 * @return The type, or null
	 */
	public Type custom(Class userTypeClass, Properties properties);

	public Type any(Type metaType, Type identifierType);
}
