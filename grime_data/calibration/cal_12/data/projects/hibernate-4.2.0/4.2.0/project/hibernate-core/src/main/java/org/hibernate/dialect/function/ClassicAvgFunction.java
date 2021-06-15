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
package org.hibernate.dialect.function;
import java.sql.Types;

import org.hibernate.MappingException;
import org.hibernate.QueryException;
import org.hibernate.engine.spi.Mapping;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.Type;

/**
 * Classic AVG sqlfunction that return types as it was done in Hibernate 3.1 
 * 
 * @author Max Rydahl Andersen
 *
 */
public class ClassicAvgFunction extends StandardSQLFunction {
	public ClassicAvgFunction() {
		super( "avg" );
	}

	public Type getReturnType(Type columnType, Mapping mapping) throws QueryException {
		int[] sqlTypes;
		try {
			sqlTypes = columnType.sqlTypes( mapping );
		}
		catch ( MappingException me ) {
			throw new QueryException( me );
		}
		if ( sqlTypes.length != 1 ) throw new QueryException( "multi-column type in avg()" );
		int sqlType = sqlTypes[0];
		if ( sqlType == Types.INTEGER || sqlType == Types.BIGINT || sqlType == Types.TINYINT ) {
			return StandardBasicTypes.FLOAT;
		}
		else {
			return columnType;
		}
	}
}