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
import java.sql.Types;

import org.hibernate.LockMode;
import org.hibernate.LockOptions;
import org.hibernate.dialect.function.AnsiTrimEmulationFunction;
import org.hibernate.dialect.function.SQLFunctionTemplate;
import org.hibernate.dialect.function.StandardSQLFunction;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.descriptor.sql.SmallIntTypeDescriptor;
import org.hibernate.type.descriptor.sql.SqlTypeDescriptor;

/**
 * A dialect for Microsoft SQL Server 2000
 *
 * @author Gavin King
 */
public class SQLServerDialect extends AbstractTransactSQLDialect {
	
	private static final int PARAM_LIST_SIZE_LIMIT = 2100;

	public SQLServerDialect() {
		registerColumnType( Types.VARBINARY, "image" );
		registerColumnType( Types.VARBINARY, 8000, "varbinary($l)" );
		registerColumnType( Types.LONGVARBINARY, "image" );
		registerColumnType( Types.LONGVARCHAR, "text" );
		registerColumnType( Types.BOOLEAN, "bit" );

		registerFunction( "second", new SQLFunctionTemplate( StandardBasicTypes.INTEGER, "datepart(second, ?1)" ) );
		registerFunction( "minute", new SQLFunctionTemplate( StandardBasicTypes.INTEGER, "datepart(minute, ?1)" ) );
		registerFunction( "hour", new SQLFunctionTemplate( StandardBasicTypes.INTEGER, "datepart(hour, ?1)" ) );
		registerFunction( "locate", new StandardSQLFunction( "charindex", StandardBasicTypes.INTEGER ) );

		registerFunction( "extract", new SQLFunctionTemplate( StandardBasicTypes.INTEGER, "datepart(?1, ?3)" ) );
		registerFunction( "mod", new SQLFunctionTemplate( StandardBasicTypes.INTEGER, "?1 % ?2" ) );
		registerFunction( "bit_length", new SQLFunctionTemplate( StandardBasicTypes.INTEGER, "datalength(?1) * 8" ) );

		registerFunction( "trim", new AnsiTrimEmulationFunction() );

		registerKeyword( "top" );
	}

	@Override
    public String getNoColumnsInsertString() {
		return "default values";
	}

	static int getAfterSelectInsertPoint(String sql) {
		int selectIndex = sql.toLowerCase().indexOf( "select" );
		final int selectDistinctIndex = sql.toLowerCase().indexOf( "select distinct" );
		return selectIndex + ( selectDistinctIndex == selectIndex ? 15 : 6 );
	}

	@Override
    public String getLimitString(String querySelect, int offset, int limit) {
		if ( offset > 0 ) {
			throw new UnsupportedOperationException( "query result offset is not supported" );
		}
		return new StringBuilder( querySelect.length() + 8 )
				.append( querySelect )
				.insert( getAfterSelectInsertPoint( querySelect ), " top " + limit )
				.toString();
	}

	/**
	 * Use <tt>insert table(...) values(...) select SCOPE_IDENTITY()</tt>
	 */
	@Override
    public String appendIdentitySelectToInsert(String insertSQL) {
		return insertSQL + " select scope_identity()";
	}

	@Override
    public boolean supportsLimit() {
		return true;
	}

	@Override
    public boolean useMaxForLimit() {
		return true;
	}

	@Override
    public boolean supportsLimitOffset() {
		return false;
	}

	@Override
    public boolean supportsVariableLimit() {
		return false;
	}

	@Override
    public char closeQuote() {
		return ']';
	}

	@Override
    public char openQuote() {
		return '[';
	}

	@Override
    public String appendLockHint(LockOptions lockOptions, String tableName) {
		LockMode mode = lockOptions.getLockMode();
		switch ( mode ) {
			case UPGRADE:
			case UPGRADE_NOWAIT:
			case PESSIMISTIC_WRITE:
			case WRITE:
				return tableName + " with (updlock, rowlock)";
			case PESSIMISTIC_READ:
				return tableName + " with (holdlock, rowlock)";
			default:
				return tableName;
		}
	}

	// The current_timestamp is more accurate, but only known to be supported
	// in SQL Server 7.0 and later (i.e., Sybase not known to support it at all)
	@Override
    public String getCurrentTimestampSelectString() {
		return "select current_timestamp";
	}

	// Overridden informational metadata ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	@Override
    public boolean areStringComparisonsCaseInsensitive() {
		return true;
	}

	@Override
    public boolean supportsResultSetPositionQueryMethodsOnForwardOnlyCursor() {
		return false;
	}

	@Override
    public boolean supportsCircularCascadeDeleteConstraints() {
		// SQL Server (at least up through 2005) does not support defining
		// cascade delete constraints which can circle back to the mutating
		// table
		return false;
	}

	@Override
    public boolean supportsLobValueChangePropogation() {
		// note: at least my local SQL Server 2005 Express shows this not working...
		return false;
	}

	@Override
    public boolean doesReadCommittedCauseWritersToBlockReaders() {
		return false; // here assume SQLServer2005 using snapshot isolation, which does not have this problem
	}

	@Override
    public boolean doesRepeatableReadCauseReadersToBlockWriters() {
		return false; // here assume SQLServer2005 using snapshot isolation, which does not have this problem
	}

    /**
     * {@inheritDoc}
     *
     * @see org.hibernate.dialect.Dialect#getSqlTypeDescriptorOverride(int)
     */
    @Override
    protected SqlTypeDescriptor getSqlTypeDescriptorOverride( int sqlCode ) {
        return sqlCode == Types.TINYINT ? SmallIntTypeDescriptor.INSTANCE : super.getSqlTypeDescriptorOverride(sqlCode);
    }

	/* (non-Javadoc)
		 * @see org.hibernate.dialect.Dialect#getInExpressionCountLimit()
		 */
	@Override
	public int getInExpressionCountLimit() {
		return PARAM_LIST_SIZE_LIMIT;
	}
}

