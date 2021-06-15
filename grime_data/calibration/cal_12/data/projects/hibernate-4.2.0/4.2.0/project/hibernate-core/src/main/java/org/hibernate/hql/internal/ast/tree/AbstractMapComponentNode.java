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
package org.hibernate.hql.internal.ast.tree;

import java.util.Map;

import antlr.SemanticException;
import antlr.collections.AST;

import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.hql.internal.antlr.HqlSqlTokenTypes;
import org.hibernate.hql.internal.ast.util.ColumnHelper;
import org.hibernate.internal.util.StringHelper;
import org.hibernate.persister.collection.QueryableCollection;
import org.hibernate.type.CollectionType;
import org.hibernate.type.Type;

/**
 * Basic support for KEY, VALUE and ENTRY based "qualified identification variables".
 *
 * @author Steve Ebersole
 */
public abstract class AbstractMapComponentNode extends FromReferenceNode implements HqlSqlTokenTypes {
	private String[] columns;

	public FromReferenceNode getMapReference() {
		return ( FromReferenceNode ) getFirstChild();
	}

	public String[] getColumns() {
		return columns;
	}

	@Override
	public void setScalarColumnText(int i) throws SemanticException {
		ColumnHelper.generateScalarColumns( this, getColumns(), i );
	}

	@Override
	public void resolve(
			boolean generateJoin,
			boolean implicitJoin,
			String classAlias,
			AST parent) throws SemanticException {
		if ( parent != null ) {
			throw attemptedDereference();
		}

		FromReferenceNode mapReference = getMapReference();
		mapReference.resolve( true, true );

		FromElement sourceFromElement = null;
		if ( isAliasRef( mapReference ) ) {
			QueryableCollection collectionPersister = mapReference.getFromElement().getQueryableCollection();
			if ( Map.class.isAssignableFrom( collectionPersister.getCollectionType().getReturnedClass() ) ) {
				sourceFromElement = mapReference.getFromElement();
			}
		}
		else {
			if ( mapReference.getDataType().isCollectionType() ) {
				CollectionType collectionType = (CollectionType) mapReference.getDataType();
				if ( Map.class.isAssignableFrom( collectionType.getReturnedClass() ) ) {
					sourceFromElement = mapReference.getFromElement();
				}
			}
		}

		if ( sourceFromElement == null ) {
			throw nonMap();
		}

		setFromElement( sourceFromElement );
		setDataType( resolveType( sourceFromElement.getQueryableCollection() ) );
		this.columns = resolveColumns( sourceFromElement.getQueryableCollection() );
		initText( this.columns );
		setFirstChild( null );
	}

	private boolean isAliasRef(FromReferenceNode mapReference) {
		return ALIAS_REF == mapReference.getType();
	}

	private void initText(String[] columns) {
		String text = StringHelper.join( ", ", columns );
		if ( columns.length > 1 && getWalker().isComparativeExpressionClause() ) {
			text = "(" + text + ")";
		}
		setText( text );
	}

	protected abstract String expressionDescription();
	protected abstract String[] resolveColumns(QueryableCollection collectionPersister);
	protected abstract Type resolveType(QueryableCollection collectionPersister);

	protected SemanticException attemptedDereference() {
		return new SemanticException( expressionDescription() + " expression cannot be further de-referenced" );
	}

	protected SemanticException nonMap() {
		return new SemanticException( expressionDescription() + " expression did not reference map property" );
	}

	@Override
	public void resolveIndex(AST parent) throws SemanticException {
		throw new UnsupportedOperationException( expressionDescription() + " expression cannot be the source for an index operation" );
	}
}
