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
package org.hibernate.mapping;
import java.util.HashSet;

import org.hibernate.HibernateException;
import org.hibernate.dialect.Dialect;
import org.hibernate.engine.spi.Mapping;
import org.hibernate.internal.util.StringHelper;

/**
 * A simple implementation of AbstractAuxiliaryDatabaseObject in which the CREATE and DROP strings are
 * provided up front.  Contains simple facilities for templating the catalog and schema
 * names into the provided strings.
 * <p/>
 * This is the form created when the mapping documents use &lt;create/&gt; and
 * &lt;drop/&gt;.
 *
 * @author Steve Ebersole
 */
public class SimpleAuxiliaryDatabaseObject extends AbstractAuxiliaryDatabaseObject {

	private final String sqlCreateString;
	private final String sqlDropString;

	public SimpleAuxiliaryDatabaseObject(String sqlCreateString, String sqlDropString) {
		this.sqlCreateString = sqlCreateString;
		this.sqlDropString = sqlDropString;
	}

	public SimpleAuxiliaryDatabaseObject(String sqlCreateString, String sqlDropString, HashSet dialectScopes) {
		super( dialectScopes );
		this.sqlCreateString = sqlCreateString;
		this.sqlDropString = sqlDropString;
	}

	public String sqlCreateString(
	        Dialect dialect,
	        Mapping p,
	        String defaultCatalog,
	        String defaultSchema) throws HibernateException {
		return injectCatalogAndSchema( sqlCreateString, defaultCatalog, defaultSchema );
	}

	public String sqlDropString(Dialect dialect, String defaultCatalog, String defaultSchema) {
		return injectCatalogAndSchema( sqlDropString, defaultCatalog, defaultSchema );
	}

	private String injectCatalogAndSchema(String ddlString, String defaultCatalog, String defaultSchema) {
		String rtn = StringHelper.replace( ddlString, "${catalog}", defaultCatalog );
		rtn = StringHelper.replace( rtn, "${schema}", defaultSchema );
		return rtn;
	}
}
