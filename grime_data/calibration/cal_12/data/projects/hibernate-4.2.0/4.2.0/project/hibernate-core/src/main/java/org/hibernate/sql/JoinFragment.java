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

import org.hibernate.internal.util.StringHelper;

/**
 * An abstract SQL join fragment renderer
 *
 * @author Gavin King
 */
public abstract class JoinFragment {

	public abstract void addJoin(String tableName, String alias, String[] fkColumns, String[] pkColumns, JoinType joinType);

	public abstract void addJoin(String tableName, String alias, String[] fkColumns, String[] pkColumns, JoinType joinType, String on);

	public abstract void addCrossJoin(String tableName, String alias);

	public abstract void addJoins(String fromFragment, String whereFragment);

	public abstract String toFromFragmentString();

	public abstract String toWhereFragmentString();

	// --Commented out by Inspection (12/4/04 9:10 AM): public abstract void addCondition(String alias, String[] columns, String condition);
	public abstract void addCondition(String alias, String[] fkColumns, String[] pkColumns);

	public abstract boolean addCondition(String condition);
	// --Commented out by Inspection (12/4/04 9:10 AM): public abstract void addFromFragmentString(String fromFragmentString);

	public abstract JoinFragment copy();

	/**
	 * @deprecated use {@link JoinType#INNER_JOIN} instead.
	 */
	@Deprecated
	public static final int INNER_JOIN = 0;
	/**
	 * @deprecated use {@link JoinType#FULL_JOIN} instead.
	 */
	@Deprecated
	public static final int FULL_JOIN = 4;
	/**
	 * @deprecated use {@link JoinType#LEFT_OUTER_JOIN} instead.
	 */
	@Deprecated
	public static final int LEFT_OUTER_JOIN = 1;
	/**
	 * @deprecated use {@link JoinType#RIGHT_OUTER_JOIN} instead.
	 */
	@Deprecated
	public static final int RIGHT_OUTER_JOIN = 2;
	private boolean hasFilterCondition = false;
	private boolean hasThetaJoins = false;

	public void addFragment(JoinFragment ojf) {
		if ( ojf.hasThetaJoins() ) {
			hasThetaJoins = true;
		}
		addJoins( ojf.toFromFragmentString(), ojf.toWhereFragmentString() );
	}

	/**
	 * Appends the 'on' condition to the buffer, returning true if the condition was added.
	 * Returns false if the 'on' condition was empty.
	 *
	 * @param buffer The buffer to append the 'on' condition to.
	 * @param on     The 'on' condition.
	 * @return Returns true if the condition was added, false if the condition was already in 'on' string.
	 */
	protected boolean addCondition(StringBuilder buffer, String on) {
		if ( StringHelper.isNotEmpty( on ) ) {
			if ( !on.startsWith( " and" ) ) buffer.append( " and " );
			buffer.append( on );
			return true;
		}
		else {
			return false;
		}
	}

	/**
	 * True if the where fragment is from a filter condition.
	 *
	 * @return True if the where fragment is from a filter condition.
	 */
	public boolean hasFilterCondition() {
		return hasFilterCondition;
	}

	public void setHasFilterCondition(boolean b) {
		this.hasFilterCondition = b;
	}

	public boolean hasThetaJoins() {
		return hasThetaJoins;
	}

	public void setHasThetaJoins(boolean hasThetaJoins) {
		this.hasThetaJoins = hasThetaJoins;
	}
}
