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
package org.hibernate.loader;
import org.hibernate.persister.entity.Loadable;

/**
 * Metadata describing the SQL result set column aliases
 * for a particular entity.
 * 
 * @author Gavin King
 */
public interface EntityAliases {
	/**
	 * The result set column aliases for the primary key columns
	 */
	public String[] getSuffixedKeyAliases();
	/**
	 * The result set column aliases for the discriminator columns
	 */
	public String getSuffixedDiscriminatorAlias();
	/**
	 * The result set column aliases for the version columns
	 */
	public String[] getSuffixedVersionAliases();
	/**
	 * The result set column aliases for the property columns
	 */
	public String[][] getSuffixedPropertyAliases();
	/**
	 * The result set column aliases for the property columns of a subclass
	 */
	public String[][] getSuffixedPropertyAliases(Loadable persister);
	/**
	 * The result set column alias for the Oracle row id
	 */
	public String getRowIdAlias();

}
