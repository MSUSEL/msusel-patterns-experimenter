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
package org.hibernate.criterion;
import org.hibernate.Criteria;
import org.hibernate.type.Type;


/**
 * A single-column projection that may be aliased
 * @author Gavin King
 */
public abstract class SimpleProjection implements EnhancedProjection {

	private static final int NUM_REUSABLE_ALIASES = 40;
	private static final String[] reusableAliases = initializeReusableAliases();

	public Projection as(String alias) {
		return Projections.alias(this, alias);
	}

	private static String[] initializeReusableAliases() {
		String[] aliases = new String[NUM_REUSABLE_ALIASES];
		for ( int i = 0; i < NUM_REUSABLE_ALIASES; i++ ) {
			aliases[i] = aliasForLocation( i );
		}
		return aliases;
	}

	private static String aliasForLocation(final int loc) {
		return "y" + loc + "_";
	}

	private static String getAliasForLocation(final int loc) {
		if ( loc >= NUM_REUSABLE_ALIASES ) {
			return aliasForLocation( loc );
		}
		else {
			return reusableAliases[loc];
		}
	}

	public String[] getColumnAliases(String alias, int loc) {
		return null;
	}

	public String[] getColumnAliases(String alias, int loc, Criteria criteria, CriteriaQuery criteriaQuery) {
		return getColumnAliases( alias, loc );
	}

	public Type[] getTypes(String alias, Criteria criteria, CriteriaQuery criteriaQuery) {
		return null;
	}

	public String[] getColumnAliases(int loc) {
		return new String[] { getAliasForLocation( loc ) };
	}

	public int getColumnCount(Criteria criteria, CriteriaQuery criteriaQuery) {
		Type types[] = getTypes( criteria, criteriaQuery );
		int count = 0;
		for ( int i=0; i<types.length; i++ ) {
			count += types[ i ].getColumnSpan( criteriaQuery.getFactory() );
		}
		return count;
	}

	public String[] getColumnAliases(int loc, Criteria criteria, CriteriaQuery criteriaQuery) {
		int numColumns =  getColumnCount( criteria, criteriaQuery );
		String[] aliases = new String[ numColumns ];
		for (int i = 0; i < numColumns; i++) {
			aliases[i] = getAliasForLocation( loc );
			loc++;
		}
		return aliases;
	}

	public String[] getAliases() {
		return new String[1];
	}

	public String toGroupSqlString(Criteria criteria, CriteriaQuery criteriaQuery) {
		throw new UnsupportedOperationException("not a grouping projection");
	}

	public boolean isGrouped() {
		return false;
	}

}
