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
package org.hibernate.service.jdbc.dialect.internal;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import org.hibernate.dialect.CUBRIDDialect;
import org.hibernate.dialect.DB2400Dialect;
import org.hibernate.dialect.DB2Dialect;
import org.hibernate.dialect.DerbyDialect;
import org.hibernate.dialect.DerbyTenFiveDialect;
import org.hibernate.dialect.DerbyTenSevenDialect;
import org.hibernate.dialect.DerbyTenSixDialect;
import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.H2Dialect;
import org.hibernate.dialect.HSQLDialect;
import org.hibernate.dialect.InformixDialect;
import org.hibernate.dialect.Ingres10Dialect;
import org.hibernate.dialect.Ingres9Dialect;
import org.hibernate.dialect.IngresDialect;
import org.hibernate.dialect.MySQLDialect;
import org.hibernate.dialect.Oracle10gDialect;
import org.hibernate.dialect.Oracle8iDialect;
import org.hibernate.dialect.Oracle9iDialect;
import org.hibernate.dialect.PostgreSQL81Dialect;
import org.hibernate.dialect.PostgreSQL82Dialect;
import org.hibernate.dialect.SQLServer2005Dialect;
import org.hibernate.dialect.SQLServer2008Dialect;
import org.hibernate.dialect.SQLServerDialect;
import org.hibernate.dialect.SybaseASE15Dialect;
import org.hibernate.dialect.SybaseAnywhereDialect;
import org.hibernate.internal.CoreMessageLogger;
import org.jboss.logging.Logger;

/**
 * The standard Hibernate Dialect resolver.
 *
 * @author Steve Ebersole
 */
public class StandardDialectResolver extends AbstractDialectResolver {

    private static final CoreMessageLogger LOG = Logger.getMessageLogger(CoreMessageLogger.class,
                                                                       StandardDialectResolver.class.getName());

	@Override
    protected Dialect resolveDialectInternal(DatabaseMetaData metaData) throws SQLException {
		String databaseName = metaData.getDatabaseProductName();
		int databaseMajorVersion = metaData.getDatabaseMajorVersion();

		if ( "CUBRID".equalsIgnoreCase( databaseName ) ) {
			return new CUBRIDDialect();
		}

		if ( "HSQL Database Engine".equals( databaseName ) ) {
			return new HSQLDialect();
		}

		if ( "H2".equals( databaseName ) ) {
			return new H2Dialect();
		}

		if ( "MySQL".equals( databaseName ) ) {
			return new MySQLDialect();
		}

		if ( "PostgreSQL".equals( databaseName ) ) {
			final int databaseMinorVersion = metaData.getDatabaseMinorVersion();
			if ( databaseMajorVersion > 8 || ( databaseMajorVersion == 8 && databaseMinorVersion >= 2 ) ) {
				return new PostgreSQL82Dialect();
			}
			return new PostgreSQL81Dialect();
		}

		if ( "Apache Derby".equals( databaseName ) ) {
			final int databaseMinorVersion = metaData.getDatabaseMinorVersion();
            if ( databaseMajorVersion > 10 || ( databaseMajorVersion == 10 && databaseMinorVersion >= 7 ) ) {
				return new DerbyTenSevenDialect();
			}
			else if ( databaseMajorVersion == 10 && databaseMinorVersion == 6 ) {
				return new DerbyTenSixDialect();
			}
			else if ( databaseMajorVersion == 10 && databaseMinorVersion == 5 ) {
				return new DerbyTenFiveDialect();
			}
			else {
				return new DerbyDialect();
			}
		}

		if ( "ingres".equalsIgnoreCase( databaseName ) ) {
            switch ( databaseMajorVersion ) {
                case 9:
                    int databaseMinorVersion = metaData.getDatabaseMinorVersion();
                    if (databaseMinorVersion > 2) {
                        return new Ingres9Dialect();
                    }
                    return new IngresDialect();
                case 10:
                    return new Ingres10Dialect();
                default:
                    LOG.unknownIngresVersion(databaseMajorVersion);
            }
			return new IngresDialect();
		}

		if ( databaseName.startsWith( "Microsoft SQL Server" ) ) {
			switch ( databaseMajorVersion ) {
                case 8:
                    return new SQLServerDialect();
                case 9:
                    return new SQLServer2005Dialect();
                case 10:
                case 11:
                    return new SQLServer2008Dialect();
                default:
                    LOG.unknownSqlServerVersion(databaseMajorVersion);
			}
			return new SQLServerDialect();
		}

		if ( "Sybase SQL Server".equals( databaseName ) || "Adaptive Server Enterprise".equals( databaseName ) ) {
			return new SybaseASE15Dialect();
		}

		if ( databaseName.startsWith( "Adaptive Server Anywhere" ) ) {
			return new SybaseAnywhereDialect();
		}

		if ( "Informix Dynamic Server".equals( databaseName ) ) {
			return new InformixDialect();
		}
		
		if ( databaseName.equals("DB2 UDB for AS/400" ) ) {
			return new DB2400Dialect();
		}

		if ( databaseName.startsWith( "DB2/" ) ) {
			return new DB2Dialect();
		}

		if ( "Oracle".equals( databaseName ) ) {
			switch ( databaseMajorVersion ) {
				case 11:
					return new Oracle10gDialect();
				case 10:
					return new Oracle10gDialect();
				case 9:
					return new Oracle9iDialect();
				case 8:
					return new Oracle8iDialect();
				default:
                    LOG.unknownOracleVersion(databaseMajorVersion);
			}
		}

		return null;
	}
}
