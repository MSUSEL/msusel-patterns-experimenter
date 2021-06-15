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
import org.hibernate.dialect.Dialect;

/**
 * A join that appears in a translated HQL query
 *
 * @author Gavin King
 */
public class QueryJoinFragment extends JoinFragment {

	private StringBuilder afterFrom = new StringBuilder();
	private StringBuilder afterWhere = new StringBuilder();
	private Dialect dialect;
	private boolean useThetaStyleInnerJoins;

	public QueryJoinFragment(Dialect dialect, boolean useThetaStyleInnerJoins) {
		this.dialect = dialect;
		this.useThetaStyleInnerJoins = useThetaStyleInnerJoins;
	}

	public void addJoin(String tableName, String alias, String[] fkColumns, String[] pkColumns, JoinType joinType) {
		addJoin( tableName, alias, alias, fkColumns, pkColumns, joinType, null );
	}

	public void addJoin(String tableName, String alias, String[] fkColumns, String[] pkColumns, JoinType joinType, String on) {
		addJoin( tableName, alias, alias, fkColumns, pkColumns, joinType, on );
	}

	private void addJoin(String tableName, String alias, String concreteAlias, String[] fkColumns, String[] pkColumns, JoinType joinType, String on) {
		if ( !useThetaStyleInnerJoins || joinType != JoinType.INNER_JOIN ) {
			JoinFragment jf = dialect.createOuterJoinFragment();
			jf.addJoin( tableName, alias, fkColumns, pkColumns, joinType, on );
			addFragment( jf );
		}
		else {
			addCrossJoin( tableName, alias );
			addCondition( concreteAlias, fkColumns, pkColumns );
			addCondition( on );
		}
	}

	public String toFromFragmentString() {
		return afterFrom.toString();
	}

	public String toWhereFragmentString() {
		return afterWhere.toString();
	}

	public void addJoins(String fromFragment, String whereFragment) {
		afterFrom.append( fromFragment );
		afterWhere.append( whereFragment );
	}

	public JoinFragment copy() {
		QueryJoinFragment copy = new QueryJoinFragment( dialect, useThetaStyleInnerJoins );
		copy.afterFrom = new StringBuilder( afterFrom.toString() );
		copy.afterWhere = new StringBuilder( afterWhere.toString() );
		return copy;
	}

	public void addCondition(String alias, String[] columns, String condition) {
		for ( int i = 0; i < columns.length; i++ ) {
			afterWhere.append( " and " )
					.append( alias )
					.append( '.' )
					.append( columns[i] )
					.append( condition );
		}
	}


	public void addCrossJoin(String tableName, String alias) {
		afterFrom.append( ", " )
				.append( tableName )
				.append( ' ' )
				.append( alias );
	}

	public void addCondition(String alias, String[] fkColumns, String[] pkColumns) {
		for ( int j = 0; j < fkColumns.length; j++ ) {
			afterWhere.append( " and " )
					.append( fkColumns[j] )
					.append( '=' )
					.append( alias )
					.append( '.' )
					.append( pkColumns[j] );
		}
	}

	/**
	 * Add the condition string to the join fragment.
	 *
	 * @param condition
	 * @return true if the condition was added, false if it was already in the fragment.
	 */
	public boolean addCondition(String condition) {
		// if the condition is not already there...
		if (
				afterFrom.toString().indexOf( condition.trim() ) < 0 &&
				afterWhere.toString().indexOf( condition.trim() ) < 0
		) {
			if ( !condition.startsWith( " and " ) ) {
				afterWhere.append( " and " );
			}
			afterWhere.append( condition );
			return true;
		}
		else {
			return false;
		}
	}

	public void addFromFragmentString(String fromFragmentString) {
		afterFrom.append( fromFragmentString );
	}

	public void clearWherePart() {
		afterWhere.setLength( 0 );
	}
}






