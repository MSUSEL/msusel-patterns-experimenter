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
package org.hibernate.usertype;

import org.hibernate.metamodel.relational.Size;

/**
 * Extends dictated/default column size declarations from {@link org.hibernate.type.Type} to the {@link UserType}
 * hierarchy as well via an optional interface.
 *
 * @author Steve Ebersole
 */
public interface Sized {
	/**
	 * Return the column sizes dictated by this type.  For example, the mapping for a {@code char}/{@link Character} would
	 * have a dictated length limit of 1; for a string-based {@link java.util.UUID} would have a size limit of 36; etc.
	 *
	 * @todo Would be much much better to have this aware of Dialect once the service/metamodel split is done
	 *
	 * @return The dictated sizes.
	 *
	 * @see org.hibernate.type.Type#dictatedSizes
	 */
	public Size[] dictatedSizes();

	/**
	 * Defines the column sizes to use according to this type if the user did not explicitly say (and if no
	 * {@link #dictatedSizes} were given).
	 *
	 * @todo Would be much much better to have this aware of Dialect once the service/metamodel split is done
	 *
	 * @return The default sizes.
	 *
	 * @see org.hibernate.type.Type#defaultSizes
	 */
	public Size[] defaultSizes();
}
