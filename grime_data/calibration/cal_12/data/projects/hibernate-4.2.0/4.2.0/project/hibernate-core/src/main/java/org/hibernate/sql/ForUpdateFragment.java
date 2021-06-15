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
package org.hibernate.sql;

import java.util.Iterator;
import java.util.Map;

import org.hibernate.LockMode;
import org.hibernate.LockOptions;
import org.hibernate.QueryException;
import org.hibernate.dialect.Dialect;
import org.hibernate.internal.util.StringHelper;

/**
 * @author Gavin King
 */
public class ForUpdateFragment {
	private final StringBuilder aliases = new StringBuilder();
	private boolean isNowaitEnabled;
	private final Dialect dialect;
	private LockMode lockMode;
	private LockOptions lockOptions;

	public ForUpdateFragment(Dialect dialect) {
		this.dialect = dialect;
	}

	public ForUpdateFragment(Dialect dialect, LockOptions lockOptions, Map keyColumnNames) throws QueryException {
		this( dialect );
		LockMode upgradeType = null;
		Iterator iter = lockOptions.getAliasLockIterator();
		this.lockOptions =  lockOptions;

		if ( !iter.hasNext()) {  // no tables referenced
			final LockMode lockMode = lockOptions.getLockMode();
			if ( LockMode.READ.lessThan( lockMode ) ) {
				upgradeType = lockMode;
				this.lockMode = lockMode;
			}
		}

		while ( iter.hasNext() ) {
			final Map.Entry me = ( Map.Entry ) iter.next();
			final LockMode lockMode = ( LockMode ) me.getValue();
			if ( LockMode.READ.lessThan( lockMode ) ) {
				final String tableAlias = ( String ) me.getKey();
				if ( dialect.forUpdateOfColumns() ) {
					String[] keyColumns = ( String[] ) keyColumnNames.get( tableAlias ); //use the id column alias
					if ( keyColumns == null ) {
						throw new IllegalArgumentException( "alias not found: " + tableAlias );
					}
					keyColumns = StringHelper.qualify( tableAlias, keyColumns );
					for ( int i = 0; i < keyColumns.length; i++ ) {
						addTableAlias( keyColumns[i] );
					}
				}
				else {
					addTableAlias( tableAlias );
				}
				if ( upgradeType != null && lockMode != upgradeType ) {
					throw new QueryException( "mixed LockModes" );
				}
				upgradeType = lockMode;
			}
		}

		if ( upgradeType == LockMode.UPGRADE_NOWAIT ) {
			setNowaitEnabled( true );
		}
	}

	public ForUpdateFragment addTableAlias(String alias) {
		if ( aliases.length() > 0 ) {
			aliases.append( ", " );
		}
		aliases.append( alias );
		return this;
	}

	public String toFragmentString() {
		if ( lockOptions!= null ) {
			return dialect.getForUpdateString( aliases.toString(), lockOptions );
		}
		else if ( aliases.length() == 0) {
			if ( lockMode != null ) {
				return dialect.getForUpdateString( lockMode );
			}
			return "";
		}
		// TODO:  pass lockmode
		return isNowaitEnabled ?
				dialect.getForUpdateNowaitString( aliases.toString() ) :
				dialect.getForUpdateString( aliases.toString() );
	}

	public ForUpdateFragment setNowaitEnabled(boolean nowait) {
		isNowaitEnabled = nowait;
		return this;
	}
}
