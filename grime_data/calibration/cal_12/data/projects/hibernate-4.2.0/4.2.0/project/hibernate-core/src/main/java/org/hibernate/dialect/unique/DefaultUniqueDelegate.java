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
package org.hibernate.dialect.unique;

import java.util.Iterator;

import org.hibernate.dialect.Dialect;
import org.hibernate.metamodel.relational.Column;
import org.hibernate.metamodel.relational.Table;
import org.hibernate.metamodel.relational.UniqueKey;

/**
 * The default UniqueDelegate implementation for most dialects.  Uses
 * separate create/alter statements to apply uniqueness to a column.
 * 
 * @author Brett Meyer
 */
public class DefaultUniqueDelegate implements UniqueDelegate {
	
	protected final Dialect dialect;
	
	public DefaultUniqueDelegate( Dialect dialect ) {
		this.dialect = dialect;
	}
	
	@Override
	public String applyUniqueToColumn( org.hibernate.mapping.Column column ) {
		return "";
	}
	
	@Override
	public String applyUniqueToColumn( Column column ) {
		return "";
	}

	@Override
	public String applyUniquesToTable( org.hibernate.mapping.Table table ) {
		return "";
	}

	@Override
	public String applyUniquesToTable( Table table ) {
		return "";
	}
	
	@Override
	public String applyUniquesOnAlter( org.hibernate.mapping.UniqueKey uniqueKey,
			String defaultCatalog, String defaultSchema ) {
		// Do this here, rather than allowing UniqueKey/Constraint to do it.
		// We need full, simplified control over whether or not it happens.
		return new StringBuilder( "alter table " )
				.append( uniqueKey.getTable().getQualifiedName(
						dialect, defaultCatalog, defaultSchema ) )
				.append( " add constraint " )
				.append( uniqueKey.getName() )
				.append( uniqueConstraintSql( uniqueKey ) )
				.toString();
	}
	
	@Override
	public String applyUniquesOnAlter( UniqueKey uniqueKey  ) {
		// Do this here, rather than allowing UniqueKey/Constraint to do it.
		// We need full, simplified control over whether or not it happens.
		return new StringBuilder( "alter table " )
				.append( uniqueKey.getTable().getQualifiedName( dialect ) )
				.append( " add constraint " )
				.append( uniqueKey.getName() )
				.append( uniqueConstraintSql( uniqueKey ) )
				.toString();
	}
	
	@Override
	public String dropUniquesOnAlter( org.hibernate.mapping.UniqueKey uniqueKey,
			String defaultCatalog, String defaultSchema ) {
		// Do this here, rather than allowing UniqueKey/Constraint to do it.
		// We need full, simplified control over whether or not it happens.
		return new StringBuilder( "alter table " )
				.append( uniqueKey.getTable().getQualifiedName(
						dialect, defaultCatalog, defaultSchema ) )
				.append( " drop constraint " )
				.append( dialect.quote( uniqueKey.getName() ) )
				.toString();
	}
	
	@Override
	public String dropUniquesOnAlter( UniqueKey uniqueKey  ) {
		// Do this here, rather than allowing UniqueKey/Constraint to do it.
		// We need full, simplified control over whether or not it happens.
		return new StringBuilder( "alter table " )
				.append( uniqueKey.getTable().getQualifiedName( dialect ) )
				.append( " drop constraint " )
				.append( dialect.quote( uniqueKey.getName() ) )
				.toString();
	}
	
	@Override
	public String uniqueConstraintSql( org.hibernate.mapping.UniqueKey uniqueKey ) {
		StringBuilder sb = new StringBuilder();
		sb.append( " unique (" );
		Iterator columnIterator = uniqueKey.getColumnIterator();
		while ( columnIterator.hasNext() ) {
			org.hibernate.mapping.Column column
					= (org.hibernate.mapping.Column) columnIterator.next();
			sb.append( column.getQuotedName( dialect ) );
			if ( columnIterator.hasNext() ) {
				sb.append( ", " );
			}
		}
		
		return sb.append( ')' ).toString();
	}
	
	@Override
	public String uniqueConstraintSql( UniqueKey uniqueKey ) {
		StringBuilder sb = new StringBuilder();
		sb.append( " unique (" );
		Iterator columnIterator = uniqueKey.getColumns().iterator();
		while ( columnIterator.hasNext() ) {
			org.hibernate.mapping.Column column
					= (org.hibernate.mapping.Column) columnIterator.next();
			sb.append( column.getQuotedName( dialect ) );
			if ( columnIterator.hasNext() ) {
				sb.append( ", " );
			}
		}
		
		return sb.append( ')' ).toString();
	}

}
