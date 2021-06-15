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

import org.hibernate.dialect.function.NoArgSQLFunction;
import org.hibernate.dialect.function.VarArgsSQLFunction;
import org.hibernate.type.StandardBasicTypes;

/**
 * A SQL dialect for Ingres 9.3 and later versions.
 * <p />
 * Changes:
 * <ul>
 * <li>Support for the SQL functions current_time, current_timestamp and current_date added</li>
 * <li>Type mapping of <code>Types.TIMESTAMP</code> changed from "timestamp with time zone" to "timestamp(9) with time zone"</li>
 * <li>Improved handling of "SELECT...FOR UPDATE" statements</li>
 * <li>Added support for pooled sequences</li>
 * <li>Added support for SELECT queries with limit and offset</li>
 * <li>Added getIdentitySelectString</li>
 * <li>Modified concatination operator</li>
 * </ul>
 * 
 * @author Enrico Schenk
 * @author Raymond Fan
 */
public class Ingres9Dialect extends IngresDialect {
    public Ingres9Dialect() {
        super();
        registerDateTimeFunctions();
        registerDateTimeColumnTypes();
        registerFunction( "concat", new VarArgsSQLFunction( StandardBasicTypes.STRING, "(", "||", ")" ) );
    }

	/**
	 * Register functions current_time, current_timestamp, current_date
	 */
	protected void registerDateTimeFunctions() {
		registerFunction("current_time", new NoArgSQLFunction("current_time", StandardBasicTypes.TIME, false));
		registerFunction("current_timestamp", new NoArgSQLFunction("current_timestamp", StandardBasicTypes.TIMESTAMP, false));
		registerFunction("current_date", new NoArgSQLFunction("current_date", StandardBasicTypes.DATE, false));
	}

	/**
	 * Register column types date, time, timestamp
	 */
	protected void registerDateTimeColumnTypes() {
		registerColumnType(Types.DATE, "ansidate");
		registerColumnType(Types.TIMESTAMP, "timestamp(9) with time zone");
	}

	// lock acquisition support ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Does this dialect support <tt>FOR UPDATE</tt> in conjunction with outer
	 * joined rows?
	 * 
	 * @return True if outer joined rows can be locked via <tt>FOR UPDATE</tt>.
	 */
	public boolean supportsOuterJoinForUpdate() {
		return false;
	}

	/**
	 * Is <tt>FOR UPDATE OF</tt> syntax supported?
	 * 
	 * @return True if the database supports <tt>FOR UPDATE OF</tt> syntax;
	 *         false otherwise.
	 */
	public boolean forUpdateOfColumns() {
		return true;
	}

	// SEQUENCE support ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Get the select command used to retrieve the last generated sequence
     * value.
     *
     * @return Statement to retrieve last generated sequence value
     */
    public String getIdentitySelectString() {
         return "select last_identity()";
    }

	/**
	 * Get the select command used retrieve the names of all sequences.
	 * 
	 * @return The select command; or null if sequences are not supported.
	 * @see org.hibernate.tool.hbm2ddl.SchemaUpdate
	 */
	public String getQuerySequencesString() {
		return "select seq_name from iisequences";
	}

	/**
	 * Does this dialect support "pooled" sequences. Not aware of a better name
	 * for this. Essentially can we specify the initial and increment values?
	 * 
	 * @return True if such "pooled" sequences are supported; false otherwise.
	 * @see #getCreateSequenceStrings(String, int, int)
	 * @see #getCreateSequenceString(String, int, int)
	 */
	public boolean supportsPooledSequences() {
		return true;
	}

	// current timestamp support ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Should the value returned by {@link #getCurrentTimestampSelectString} be
	 * treated as callable. Typically this indicates that JDBC escape sytnax is
	 * being used...
	 * 
	 * @return True if the {@link #getCurrentTimestampSelectString} return is
	 *         callable; false otherwise.
	 */
	public boolean isCurrentTimestampSelectStringCallable() {
		return false;
	}

	/**
	 * Does this dialect support a way to retrieve the database's current
	 * timestamp value?
	 * 
	 * @return True if the current timestamp can be retrieved; false otherwise.
	 */
	public boolean supportsCurrentTimestampSelection() {
		return true;
	}

	/**
	 * Retrieve the command used to retrieve the current timestammp from the
	 * database.
	 * 
	 * @return The command.
	 */
	public String getCurrentTimestampSelectString() {
		return "select current_timestamp";
	}

	/**
	 * Expression for current_timestamp
	 */
	public String getCurrentTimestampSQLFunctionName() {
		return "current_timestamp";
	}

	// union subclass support ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Does this dialect support UNION ALL, which is generally a faster variant
	 * of UNION?
	 * 
	 * @return True if UNION ALL is supported; false otherwise.
	 */
	public boolean supportsUnionAll() {
		return true;
	}

	// Informational metadata ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * For the underlying database, is READ_COMMITTED isolation implemented by
	 * forcing readers to wait for write locks to be released?
	 * 
	 * @return true
	 */
	public boolean doesReadCommittedCauseWritersToBlockReaders() {
		return true;
	}

	/**
	 * For the underlying database, is REPEATABLE_READ isolation implemented by
	 * forcing writers to wait for read locks to be released?
	 * 
	 * @return true
	 */
	public boolean doesRepeatableReadCauseReadersToBlockWriters() {
		return true;
	}

	// limit/offset support ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Does this dialect's LIMIT support (if any) additionally support
	 * specifying an offset?
	 * 
	 * @return true
	 */
	public boolean supportsLimitOffset() {
		return true;
	}

	/**
	 * Does this dialect support bind variables (i.e., prepared statememnt
	 * parameters) for its limit/offset?
	 * 
	 * @return false
	 */
	public boolean supportsVariableLimit() {
		return false;
	}

	/**
	 * Does the <tt>LIMIT</tt> clause take a "maximum" row number instead
	 * of a total number of returned rows?
	 */
	public boolean useMaxForLimit() {
		return false;
	}

	/**
	 * Add a <tt>LIMIT</tt> clause to the given SQL <tt>SELECT</tt>
	 * 
	 * @return the modified SQL
	 */
	public String getLimitString(String querySelect, int offset, int limit) {
		StringBuilder soff = new StringBuilder(" offset " + offset);
		StringBuilder slim = new StringBuilder(" fetch first " + limit + " rows only");
		StringBuilder sb = new StringBuilder(querySelect.length() +
            soff.length() + slim.length()).append(querySelect);
		if (offset > 0) {
			sb.append(soff);
		}
        if (limit > 0) {
            sb.append(slim);
        }
		return sb.toString();
	}
}
