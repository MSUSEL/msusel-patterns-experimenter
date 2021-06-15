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
package org.hibernate.engine.jdbc.internal;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashSet;

import org.jboss.logging.Logger;

import org.hibernate.internal.CoreMessageLogger;
import org.hibernate.internal.util.collections.ArrayHelper;

/**
 * Helper to extract type information from {@link DatabaseMetaData JDBC metadata}
 *
 * @author Steve Ebersole
 */
public class TypeInfoExtracter {

    private static final CoreMessageLogger LOG = Logger.getMessageLogger(CoreMessageLogger.class, TypeInfoExtracter.class.getName());

	private TypeInfoExtracter() {
	}

	/**
	 * Perform the extraction
	 *
	 * @param metaData The JDBC metadata
	 *
	 * @return The extracted metadata
	 */
	public static LinkedHashSet<TypeInfo> extractTypeInfo(DatabaseMetaData metaData) {
		LinkedHashSet<TypeInfo> typeInfoSet = new LinkedHashSet<TypeInfo>();
		try {
			ResultSet resultSet = metaData.getTypeInfo();
			try {
				while ( resultSet.next() ) {
					typeInfoSet.add(
							new TypeInfo(
									resultSet.getString( "TYPE_NAME" ),
									resultSet.getInt( "DATA_TYPE" ),
									interpretCreateParams( resultSet.getString( "CREATE_PARAMS" ) ),
									resultSet.getBoolean( "UNSIGNED_ATTRIBUTE" ),
									resultSet.getInt( "PRECISION" ),
									resultSet.getShort( "MINIMUM_SCALE" ),
									resultSet.getShort( "MAXIMUM_SCALE" ),
									resultSet.getBoolean( "FIXED_PREC_SCALE" ),
									resultSet.getString( "LITERAL_PREFIX" ),
									resultSet.getString( "LITERAL_SUFFIX" ),
									resultSet.getBoolean( "CASE_SENSITIVE" ),
									TypeSearchability.interpret( resultSet.getShort( "SEARCHABLE" ) ),
									TypeNullability.interpret( resultSet.getShort( "NULLABLE" ) )
							)
					);
				}
			}
			catch ( SQLException e ) {
				LOG.unableToAccessTypeInfoResultSet( e.toString() );
			}
			finally {
				try {
					resultSet.close();
				}
				catch ( SQLException e ) {
					LOG.unableToReleaseTypeInfoResultSet();
				}
			}
		}
		catch ( SQLException e ) {
			LOG.unableToRetrieveTypeInfoResultSet( e.toString() );
		}

		return typeInfoSet;
	}

	private static String[] interpretCreateParams(String value) {
		if ( value == null || value.length() == 0 ) {
			return ArrayHelper.EMPTY_STRING_ARRAY;
		}
		return value.split( "," );
	}
}
