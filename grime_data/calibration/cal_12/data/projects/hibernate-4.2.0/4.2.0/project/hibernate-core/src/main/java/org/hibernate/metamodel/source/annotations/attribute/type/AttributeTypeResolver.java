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
package org.hibernate.metamodel.source.annotations.attribute.type;

import java.util.Map;

/**
 * Determines explicit Hibernate type information for JPA mapped attributes when additional type information is
 * provided via annotations like {@link javax.persistence.Lob}, {@link javax.persistence.Enumerated} and
 * {@link javax.persistence.Temporal}.
 *
 * @author Strong Liu
 */
public interface AttributeTypeResolver {
	/**
	 * @return returns an explicit hibernate type name in case the mapped attribute has an additional
	 *         {@link org.hibernate.annotations.Type} annotation or an implicit type is given via the use of annotations like
	 *         {@link javax.persistence.Lob}, {@link javax.persistence.Enumerated} and
	 *         {@link javax.persistence.Temporal}.
	 */
	String getExplicitHibernateTypeName();

	/**
	 * @return Returns a map of optional type parameters. See {@link #getExplicitHibernateTypeName()}.
	 */
	Map<String, String> getExplicitHibernateTypeParameters();
}
