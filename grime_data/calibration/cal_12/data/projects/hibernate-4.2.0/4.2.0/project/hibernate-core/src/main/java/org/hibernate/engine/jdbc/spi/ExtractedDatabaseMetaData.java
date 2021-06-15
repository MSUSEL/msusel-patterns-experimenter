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
package org.hibernate.engine.jdbc.spi;

import java.util.LinkedHashSet;
import java.util.Set;

import org.hibernate.engine.jdbc.internal.TypeInfo;

/**
 * Information extracted from {@link java.sql.DatabaseMetaData} regarding what the JDBC driver reports as
 * being supported or not.  Obviously {@link java.sql.DatabaseMetaData} reports many things, these are a few in
 * which we have particular interest.
 *
 * @author Steve Ebersole
 */
@SuppressWarnings( {"UnusedDeclaration"})
public interface ExtractedDatabaseMetaData {

	public enum SQLStateType {
		XOpen,
		SQL99,
		UNKOWN
	}

	/**
	 * Did the driver report to supporting scrollable result sets?
	 *
	 * @return True if the driver reported to support {@link java.sql.ResultSet#TYPE_SCROLL_INSENSITIVE}.
	 *
	 * @see java.sql.DatabaseMetaData#supportsResultSetType
	 */
	public boolean supportsScrollableResults();

	/**
	 * Did the driver report to supporting retrieval of generated keys?
	 *
	 * @return True if the if the driver reported to support calls to {@link java.sql.Statement#getGeneratedKeys}
	 *
	 * @see java.sql.DatabaseMetaData#supportsGetGeneratedKeys
	 */
	public boolean supportsGetGeneratedKeys();

	/**
	 * Did the driver report to supporting batched updates?
	 *
	 * @return True if the driver supports batched updates
	 *
	 * @see java.sql.DatabaseMetaData#supportsBatchUpdates
	 */
	public boolean supportsBatchUpdates();

	/**
	 * Did the driver report to support performing DDL within transactions?
	 *
	 * @return True if the drivers supports DDL statements within transactions.
	 *
	 * @see java.sql.DatabaseMetaData#dataDefinitionIgnoredInTransactions
	 */
	public boolean supportsDataDefinitionInTransaction();

	/**
	 * Did the driver report to DDL statements performed within a transaction performing an implicit commit of the
	 * transaction.
	 *
	 * @return True if the driver/database performs an implicit commit of transaction when DDL statement is
	 * performed
	 *
	 * @see java.sql.DatabaseMetaData#dataDefinitionCausesTransactionCommit()
	 */
	public boolean doesDataDefinitionCauseTransactionCommit();

	/**
	 * Get the list of extra keywords (beyond standard SQL92 keywords) reported by the driver.
	 *
	 * @return The extra keywords used by this database.
	 *
	 * @see java.sql.DatabaseMetaData#getSQLKeywords()
	 */
	public Set<String> getExtraKeywords();

	/**
	 * Retrieve the type of codes the driver says it uses for {@code SQLState}.  They might follow either
	 * the X/Open standard or the SQL92 standard.
	 *
	 * @return The SQLState strategy reportedly used by this driver/database.
	 *
	 * @see java.sql.DatabaseMetaData#getSQLStateType()
	 */
	public SQLStateType getSqlStateType();

	/**
	 * Did the driver report that updates to a LOB locator affect a copy of the LOB?
	 *
	 * @return True if updates to the state of a LOB locator update only a copy.
	 *
	 * @see java.sql.DatabaseMetaData#locatorsUpdateCopy()
	 */
	public boolean doesLobLocatorUpdateCopy();

	/**
	 * Retrieve the name of the schema in effect when we connected to the database.
	 *
	 * @return The schema name
	 */
	public String getConnectionSchemaName();

	/**
	 * Retrieve the name of the catalog in effect when we connected to the database.
	 *
	 * @return The catalog name
	 */
	public String getConnectionCatalogName();

	/**
	 * Set of type info reported by the driver.
	 *
	 * @return The type information obtained from the driver.
	 *
	 * @see java.sql.DatabaseMetaData#getTypeInfo()
	 */
	public LinkedHashSet<TypeInfo> getTypeInfoSet();
}
