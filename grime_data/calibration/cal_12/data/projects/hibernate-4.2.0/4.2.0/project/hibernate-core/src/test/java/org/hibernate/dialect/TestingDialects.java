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
package org.hibernate.dialect;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import org.hibernate.HibernateException;
import org.hibernate.service.jdbc.dialect.internal.AbstractDialectResolver;
import org.hibernate.service.jdbc.dialect.internal.BasicDialectResolver;

/**
 * @author Steve Ebersole
 */
public class TestingDialects {

	public static class MyDialect1 extends Dialect {
	}

	public static class MyDialect21 extends Dialect {
	}

	public static class MyDialect22 extends Dialect {
	}

	public static class MySpecialDB2Dialect extends Dialect {
	}

	public static class MyDialectResolver1 extends AbstractDialectResolver {
		protected Dialect resolveDialectInternal(DatabaseMetaData metaData) throws SQLException {
			String databaseName = metaData.getDatabaseProductName();
			int databaseMajorVersion = metaData.getDatabaseMajorVersion();
			if ( "MyDatabase1".equals( databaseName ) ) {
				return new MyDialect1();
			}
			if ( "MyDatabase2".equals( databaseName ) ) {
				if ( databaseMajorVersion >= 2 ) {
					return new MyDialect22();
				}
				if ( databaseMajorVersion >= 1 ) {
					return new MyDialect21();
				}
			}
			return null;
		}
	}

	public static class MyDialectResolver2 extends BasicDialectResolver {
		public MyDialectResolver2() {
			super( "MyTrickyDatabase1", MyDialect1.class );
		}
	}

	public static class ErrorDialectResolver1 extends AbstractDialectResolver {
		public Dialect resolveDialectInternal(DatabaseMetaData metaData) throws SQLException {
			String databaseName = metaData.getDatabaseProductName();
			if ( databaseName.equals( "ConnectionErrorDatabase1" ) ) {
				throw new SQLException( "Simulated connection error", "08001" );
			}
			else {
				throw new SQLException();
			}
		}
	}

	public static class ErrorDialectResolver2 extends AbstractDialectResolver {
		public Dialect resolveDialectInternal(DatabaseMetaData metaData) throws SQLException {
			String databaseName = metaData.getDatabaseProductName();
			if ( databaseName.equals( "ErrorDatabase1" ) ) {
				throw new SQLException();
			}
			if ( databaseName.equals( "ErrorDatabase2" ) ) {
				throw new HibernateException( "This is a trap!" );
			}
			return null;
		}
	}

	public static class MyOverridingDialectResolver1 extends BasicDialectResolver {
		public MyOverridingDialectResolver1() {
			super( "DB2/MySpecialPlatform", MySpecialDB2Dialect.class );
		}
	}

}
