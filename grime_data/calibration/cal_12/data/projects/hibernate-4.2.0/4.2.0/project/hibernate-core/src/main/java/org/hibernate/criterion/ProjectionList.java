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
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.internal.util.collections.ArrayHelper;
import org.hibernate.type.Type;

/**
 * @author Gavin King
 */
public class ProjectionList implements EnhancedProjection {
	
	private List elements = new ArrayList();
	
	protected ProjectionList() {}
	
	public ProjectionList create() {
		return new ProjectionList();
	}
	
	public ProjectionList add(Projection proj) {
		elements.add(proj);
		return this;
	}

	public ProjectionList add(Projection projection, String alias) {
		return add( Projections.alias( projection, alias ) );
	}

	public Type[] getTypes(Criteria criteria, CriteriaQuery criteriaQuery)
	throws HibernateException {
		List types = new ArrayList( getLength() );
		for ( int i=0; i<getLength(); i++ ) {
			Type[] elemTypes = getProjection(i).getTypes(criteria, criteriaQuery);
			ArrayHelper.addAll(types, elemTypes);
		}
		return ArrayHelper.toTypeArray(types);
	}
	
	public String toSqlString(Criteria criteria, int loc, CriteriaQuery criteriaQuery) 
	throws HibernateException {
		StringBuilder buf = new StringBuilder();
		for ( int i=0; i<getLength(); i++ ) {
			Projection proj = getProjection(i);
			buf.append( proj.toSqlString(criteria, loc, criteriaQuery) );
			loc += getColumnAliases(loc, criteria, criteriaQuery, proj ).length;
			if ( i<elements.size()-1 ) buf.append(", ");
		}
		return buf.toString();
	}
	
	public String toGroupSqlString(Criteria criteria, CriteriaQuery criteriaQuery) 
	throws HibernateException {
		StringBuilder buf = new StringBuilder();
		for ( int i=0; i<getLength(); i++ ) {
			Projection proj = getProjection(i);
			if ( proj.isGrouped() ) {
				buf.append( proj.toGroupSqlString(criteria, criteriaQuery) )
					.append(", ");
			}
		}
		if ( buf.length()>2 ) buf.setLength( buf.length()-2 ); //pull off the last ", "
		return buf.toString();
	}
	
	public String[] getColumnAliases(int loc) {
		List result = new ArrayList( getLength() );
		for ( int i=0; i<getLength(); i++ ) {
			String[] colAliases = getProjection(i).getColumnAliases(loc);
			ArrayHelper.addAll(result, colAliases);
			loc+=colAliases.length;
		}
		return ArrayHelper.toStringArray(result);
	}

	public String[] getColumnAliases(int loc, Criteria criteria, CriteriaQuery criteriaQuery) {
		List result = new ArrayList( getLength() );
		for ( int i=0; i<getLength(); i++ ) {
			String[] colAliases = getColumnAliases( loc, criteria, criteriaQuery, getProjection( i ) );
			ArrayHelper.addAll(result, colAliases);
			loc+=colAliases.length;
		}
		return ArrayHelper.toStringArray(result);
	}

	public String[] getColumnAliases(String alias, int loc) {
		for ( int i=0; i<getLength(); i++ ) {
			String[] result = getProjection(i).getColumnAliases(alias, loc);
			if (result!=null) return result;
			loc += getProjection(i).getColumnAliases(loc).length;
		}
		return null;
	}

	public String[] getColumnAliases(String alias, int loc, Criteria criteria, CriteriaQuery criteriaQuery) {
		for ( int i=0; i<getLength(); i++ ) {
			String[] result = getColumnAliases( alias, loc, criteria, criteriaQuery, getProjection(i) );
			if (result!=null) return result;
			loc += getColumnAliases( loc, criteria, criteriaQuery, getProjection( i ) ).length;
		}
		return null;
	}

	private static String[] getColumnAliases(int loc, Criteria criteria, CriteriaQuery criteriaQuery, Projection projection) {
		return projection instanceof EnhancedProjection ?
				( ( EnhancedProjection ) projection ).getColumnAliases( loc, criteria, criteriaQuery ) :
				projection.getColumnAliases( loc );
	}

	private static String[] getColumnAliases(String alias, int loc, Criteria criteria, CriteriaQuery criteriaQuery, Projection projection) {
		return projection instanceof EnhancedProjection ?
				( ( EnhancedProjection ) projection ).getColumnAliases( alias, loc, criteria, criteriaQuery ) :
				projection.getColumnAliases( alias, loc );
	}

	public Type[] getTypes(String alias, Criteria criteria, CriteriaQuery criteriaQuery) {
		for ( int i=0; i<getLength(); i++ ) {
			Type[] result = getProjection(i).getTypes(alias, criteria, criteriaQuery);
			if (result!=null) return result;
		}
		return null;
	}

	public String[] getAliases() {
		List result = new ArrayList( getLength() );
		for ( int i=0; i<getLength(); i++ ) {
			String[] aliases = getProjection(i).getAliases();
			ArrayHelper.addAll( result, aliases );
		}
		return ArrayHelper.toStringArray(result);

	}
	
	public Projection getProjection(int i) {
		return (Projection) elements.get(i);
	}
	
	public int getLength() {
		return elements.size();
	}

	public String toString() {
		return elements.toString();
	}

	public boolean isGrouped() {
		for ( int i=0; i<getLength(); i++ ) {
			if ( getProjection(i).isGrouped() ) return true;
		}
		return false;
	}
}
