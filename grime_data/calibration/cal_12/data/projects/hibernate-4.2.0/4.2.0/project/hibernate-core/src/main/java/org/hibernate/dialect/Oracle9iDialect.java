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

import org.hibernate.LockOptions;
import org.hibernate.sql.ANSICaseFragment;
import org.hibernate.sql.CaseFragment;

/**
 * A dialect for Oracle 9i databases.
 * <p/>
 * Specifies to not use "ANSI join syntax" because 9i does not seem to properly handle it in all cases.
 *
 * @author Steve Ebersole
 */
public class Oracle9iDialect extends Oracle8iDialect {
	@Override
	protected void registerCharacterTypeMappings() {
		registerColumnType( Types.CHAR, "char(1 char)" );
		registerColumnType( Types.VARCHAR, 4000, "varchar2($l char)" );
		registerColumnType( Types.VARCHAR, "long" );
	}
	@Override
	protected void registerDateTimeTypeMappings() {
		registerColumnType( Types.DATE, "date" );
		registerColumnType( Types.TIME, "date" );
		registerColumnType( Types.TIMESTAMP, "timestamp" );
	}
	@Override
	public CaseFragment createCaseFragment() {
		// Oracle did add support for ANSI CASE statements in 9i
		return new ANSICaseFragment();
	}
	@Override
	public String getLimitString(String sql, boolean hasOffset) {
		sql = sql.trim();
		String forUpdateClause = null;
		boolean isForUpdate = false;
		final int forUpdateIndex = sql.toLowerCase().lastIndexOf( "for update") ;
		if ( forUpdateIndex > -1 ) {
			// save 'for update ...' and then remove it
			forUpdateClause = sql.substring( forUpdateIndex );
			sql = sql.substring( 0, forUpdateIndex-1 );
			isForUpdate = true;
		}

		StringBuilder pagingSelect = new StringBuilder( sql.length() + 100 );
		if (hasOffset) {
			pagingSelect.append("select * from ( select row_.*, rownum rownum_ from ( ");
		}
		else {
			pagingSelect.append("select * from ( ");
		}
		pagingSelect.append(sql);
		if (hasOffset) {
			pagingSelect.append(" ) row_ where rownum <= ?) where rownum_ > ?");
		}
		else {
			pagingSelect.append(" ) where rownum <= ?");
		}

		if ( isForUpdate ) {
			pagingSelect.append( " " );
			pagingSelect.append( forUpdateClause );
		}

		return pagingSelect.toString();
	}
	@Override
	public String getSelectClauseNullString(int sqlType) {
		return getBasicSelectClauseNullString( sqlType );
	}
	@Override
	public String getCurrentTimestampSelectString() {
		return "select systimestamp from dual";
	}
	@Override
	public String getCurrentTimestampSQLFunctionName() {
		// the standard SQL function name is current_timestamp...
		return "current_timestamp";
	}

	// locking support
	@Override
	public String getForUpdateString() {
		return " for update";
	}
	@Override
	public String getWriteLockString(int timeout) {
		if ( timeout == LockOptions.NO_WAIT ) {
			return " for update nowait";
		}
		else if ( timeout > 0 ) {
			// convert from milliseconds to seconds
			float seconds = timeout / 1000.0f;
			timeout = Math.round(seconds);
			return " for update wait " + timeout;
		}
		else
			return " for update";
	}
	@Override
	public String getReadLockString(int timeout) {
		return getWriteLockString( timeout );
	}
	/**
	 * HHH-4907, I don't know if oracle 8 supports this syntax, so I'd think it is better add this 
	 * method here. Reopen this issue if you found/know 8 supports it.
	 */
	@Override
	public boolean supportsRowValueConstructorSyntaxInInList() {
		return true;
	}
	@Override
	public boolean supportsTupleDistinctCounts() {
		return false;
	}	
}
