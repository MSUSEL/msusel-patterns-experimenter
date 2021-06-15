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
package org.hibernate.metamodel.relational;

import java.util.Set;

import org.hibernate.dialect.Dialect;
import org.hibernate.internal.util.StringHelper;

/**
 * @author Steve Ebersole
 * @author Gail Badner
 */
public class BasicAuxiliaryDatabaseObjectImpl extends AbstractAuxiliaryDatabaseObject {
	private static final String CATALOG_NAME_PLACEHOLDER = "${catalog}";
	private static final String SCHEMA_NAME_PLACEHOLDER = "${schema}";
	private final Schema defaultSchema;
	private final String createString;
	private final String dropString;

	public BasicAuxiliaryDatabaseObjectImpl(
			Schema defaultSchema,
			String createString,
			String dropString,
			Set<String> dialectScopes) {
		super( dialectScopes );
		// keep track of the default schema and the raw create/drop strings;
		// we may want to allow copying into a database with a different default schema in the future;
		this.defaultSchema = defaultSchema;
		this.createString = createString;
		this.dropString = dropString;
	}

	@Override
	public String[] sqlCreateStrings(Dialect dialect) {
		return new String[] { injectCatalogAndSchema( createString, defaultSchema ) };
	}

	@Override
	public String[] sqlDropStrings(Dialect dialect) {
		return new String[] { injectCatalogAndSchema( dropString, defaultSchema ) };
	}

	private static String injectCatalogAndSchema(String ddlString, Schema schema) {
		String rtn = StringHelper.replace( ddlString, CATALOG_NAME_PLACEHOLDER, schema.getName().getCatalog().getName() );
		rtn = StringHelper.replace( rtn, SCHEMA_NAME_PLACEHOLDER, schema.getName().getSchema().getName() );
		return rtn;
	}
}
