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
package org.hibernate.type.descriptor;

import org.hibernate.engine.jdbc.LobCreator;
import org.hibernate.type.descriptor.sql.SqlTypeDescriptor;

/**
 * Gives binding (nullSafeSet) and extracting (nullSafeGet) code access to options.
 *
 * @todo Definitely could use a better name
 *
 * @author Steve Ebersole
 */
public interface WrapperOptions {
	/**
	 * Should streams be used for binding LOB values.
	 *
	 * @return {@code true}/{@code false}
	 */
	public boolean useStreamForLobBinding();

	/**
	 * Obtain access to the {@link LobCreator}
	 *
	 * @return The LOB creator
	 */
	public LobCreator getLobCreator();

	/**
	 * Allow remapping of descriptors for dealing with sql type.
	 *
	 * @param sqlTypeDescriptor The known descriptor
	 *
	 * @return The remapped descriptor.  May be the same as the known descriptor indicating no remapping.
	 */
	public SqlTypeDescriptor remapSqlTypeDescriptor(SqlTypeDescriptor sqlTypeDescriptor);
}
