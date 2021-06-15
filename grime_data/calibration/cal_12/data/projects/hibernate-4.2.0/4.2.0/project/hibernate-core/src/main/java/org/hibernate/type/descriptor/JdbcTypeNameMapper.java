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

import java.lang.reflect.Field;
import java.sql.Types;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.jboss.logging.Logger;

import org.hibernate.HibernateException;
import org.hibernate.internal.CoreMessageLogger;

/**
 * TODO : javadoc
 *
 * @author Steve Ebersole
 */
public class JdbcTypeNameMapper {

    private static final CoreMessageLogger LOG = Logger.getMessageLogger(CoreMessageLogger.class, JdbcTypeNameMapper.class.getName());
	private static Map<Integer,String> JDBC_TYPE_MAP = buildJdbcTypeMap();

	private static Map<Integer, String> buildJdbcTypeMap() {
		HashMap<Integer, String> map = new HashMap<Integer, String>();
		Field[] fields = Types.class.getFields();
		if ( fields == null ) {
			throw new HibernateException( "Unexpected problem extracting JDBC type mapping codes from java.sql.Types" );
		}
		for ( Field field : fields ) {
			try {
				final int code = field.getInt( null );
				String old = map.put( code, field.getName() );
                if (old != null) LOG.JavaSqlTypesMappedSameCodeMultipleTimes(code, old, field.getName());
			}
			catch ( IllegalAccessException e ) {
				throw new HibernateException( "Unable to access JDBC type mapping [" + field.getName() + "]", e );
			}
		}
		return Collections.unmodifiableMap( map );
	}

	public static String getTypeName(Integer code) {
		String name = JDBC_TYPE_MAP.get( code );
		if ( name == null ) {
			return "UNKNOWN(" + code + ")";
		}
		return name;
	}
}
