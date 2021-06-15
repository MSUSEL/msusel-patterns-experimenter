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
import org.hibernate.AssertionFailure;

/**
 * An ANSI-style join
 *
 * @author Gavin King
 */
public class ANSIJoinFragment extends JoinFragment {

	private StringBuilder buffer = new StringBuilder();
	private StringBuilder conditions = new StringBuilder();

	public void addJoin(String tableName, String alias, String[] fkColumns, String[] pkColumns, JoinType joinType) {
		addJoin(tableName, alias, fkColumns, pkColumns, joinType, null);
	}

	public void addJoin(String tableName, String alias, String[] fkColumns, String[] pkColumns, JoinType joinType, String on) {
		String joinString;
		switch (joinType) {
			case INNER_JOIN:
				joinString = " inner join ";
				break;
			case LEFT_OUTER_JOIN:
				joinString = " left outer join ";
				break;
			case RIGHT_OUTER_JOIN:
				joinString = " right outer join ";
				break;
			case FULL_JOIN:
				joinString = " full outer join ";
				break;
			default:
				throw new AssertionFailure("undefined join type");
		}

		buffer.append(joinString)
			.append(tableName)
			.append(' ')
			.append(alias)
			.append(" on ");


		for ( int j=0; j<fkColumns.length; j++) {
			/*if ( fkColumns[j].indexOf('.')<1 ) {
				throw new AssertionFailure("missing alias");
			}*/
			buffer.append( fkColumns[j] )
				.append('=')
				.append(alias)
				.append('.')
				.append( pkColumns[j] );
			if ( j<fkColumns.length-1 ) buffer.append(" and ");
		}

		addCondition(buffer, on);

	}

	public String toFromFragmentString() {
		return buffer.toString();
	}

	public String toWhereFragmentString() {
		return conditions.toString();
	}

	public void addJoins(String fromFragment, String whereFragment) {
		buffer.append(fromFragment);
		//where fragment must be empty!
	}

	public JoinFragment copy() {
		ANSIJoinFragment copy = new ANSIJoinFragment();
		copy.buffer = new StringBuilder( buffer.toString() );
		return copy;
	}

	public void addCondition(String alias, String[] columns, String condition) {
		for ( int i=0; i<columns.length; i++ ) {
			conditions.append(" and ")
				.append(alias)
				.append('.')
				.append( columns[i] )
				.append(condition);
		}
	}

	public void addCrossJoin(String tableName, String alias) {
		buffer.append(", ")
			.append(tableName)
			.append(' ')
			.append(alias);
	}

	public void addCondition(String alias, String[] fkColumns, String[] pkColumns) {
		throw new UnsupportedOperationException();

	}

	public boolean addCondition(String condition) {
		return addCondition(conditions, condition);
	}

	public void addFromFragmentString(String fromFragmentString) {
		buffer.append(fromFragmentString);
	}

}






