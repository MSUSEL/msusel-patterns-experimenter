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
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import antlr.RecognitionException;
import antlr.SemanticException;
import antlr.collections.AST;
import org.jboss.logging.Logger;

import org.hibernate.QueryException;
import org.hibernate.engine.internal.JoinSequence;
import org.hibernate.engine.spi.QueryParameters;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.hql.internal.ast.SqlGenerator;
import org.hibernate.hql.internal.ast.util.SessionFactoryHelper;
import org.hibernate.internal.CoreMessageLogger;
import org.hibernate.param.ParameterSpecification;
import org.hibernate.persister.collection.QueryableCollection;
import org.hibernate.type.CollectionType;
import org.hibernate.type.Type;

/**
 * Represents the [] operator and provides it's semantics.
 *
 * @author josh
 */
public class IndexNode extends FromReferenceNode {

	private static final CoreMessageLogger LOG = Logger.getMessageLogger( CoreMessageLogger.class, IndexNode.class.getName() );

	public void setScalarColumnText(int i) throws SemanticException {
		throw new UnsupportedOperationException( "An IndexNode cannot generate column text!" );
	}

	@Override
    public void prepareForDot(String propertyName) throws SemanticException {
		FromElement fromElement = getFromElement();
		if ( fromElement == null ) {
			throw new IllegalStateException( "No FROM element for index operator!" );
		}
		QueryableCollection queryableCollection = fromElement.getQueryableCollection();
		if ( queryableCollection != null && !queryableCollection.isOneToMany() ) {

			FromReferenceNode collectionNode = ( FromReferenceNode ) getFirstChild();
			String path = collectionNode.getPath() + "[]." + propertyName;
			LOG.debugf( "Creating join for many-to-many elements for %s", path );
			FromElementFactory factory = new FromElementFactory( fromElement.getFromClause(), fromElement, path );
			// This will add the new from element to the origin.
			FromElement elementJoin = factory.createElementJoin( queryableCollection );
			setFromElement( elementJoin );
		}
	}

	public void resolveIndex(AST parent) throws SemanticException {
		throw new UnsupportedOperationException();
	}

	public void resolve(boolean generateJoin, boolean implicitJoin, String classAlias, AST parent)
	throws SemanticException {
		if ( isResolved() ) {
			return;
		}
		FromReferenceNode collectionNode = ( FromReferenceNode ) getFirstChild();
		SessionFactoryHelper sessionFactoryHelper = getSessionFactoryHelper();
		collectionNode.resolveIndex( this );		// Fully resolve the map reference, create implicit joins.

		Type type = collectionNode.getDataType();
		if ( !type.isCollectionType() ) {
			throw new SemanticException( "The [] operator cannot be applied to type " + type.toString() );
		}
		String collectionRole = ( ( CollectionType ) type ).getRole();
		QueryableCollection queryableCollection = sessionFactoryHelper.requireQueryableCollection( collectionRole );
		if ( !queryableCollection.hasIndex() ) {
			throw new QueryException( "unindexed fromElement before []: " + collectionNode.getPath() );
		}

		// Generate the inner join -- The elements need to be joined to the collection they are in.
		FromElement fromElement = collectionNode.getFromElement();
		String elementTable = fromElement.getTableAlias();
		FromClause fromClause = fromElement.getFromClause();
		String path = collectionNode.getPath();

		FromElement elem = fromClause.findCollectionJoin( path );
		if ( elem == null ) {
			FromElementFactory factory = new FromElementFactory( fromClause, fromElement, path );
			elem = factory.createCollectionElementsJoin( queryableCollection, elementTable );
			LOG.debugf( "No FROM element found for the elements of collection join path %s, created %s", path, elem );
		}
		else {
			LOG.debugf( "FROM element found for collection join path %s", path );
		}

		// The 'from element' that represents the elements of the collection.
		setFromElement( fromElement );

		// Add the condition to the join sequence that qualifies the indexed element.
		AST selector = collectionNode.getNextSibling();
		if ( selector == null ) {
			throw new QueryException( "No index value!" );
		}

		// Sometimes use the element table alias, sometimes use the... umm... collection table alias (many to many)
		String collectionTableAlias = elementTable;
		if ( elem.getCollectionTableAlias() != null ) {
			collectionTableAlias = elem.getCollectionTableAlias();
		}

		// TODO: get SQL rendering out of here, create an AST for the join expressions.
		// Use the SQL generator grammar to generate the SQL text for the index expression.
		JoinSequence joinSequence = fromElement.getJoinSequence();
		String[] indexCols = queryableCollection.getIndexColumnNames();
		if ( indexCols.length != 1 ) {
			throw new QueryException( "composite-index appears in []: " + collectionNode.getPath() );
		}
		SqlGenerator gen = new SqlGenerator( getSessionFactoryHelper().getFactory() );
		try {
			gen.simpleExpr( selector ); //TODO: used to be exprNoParens! was this needed?
		}
		catch ( RecognitionException e ) {
			throw new QueryException( e.getMessage(), e );
		}
		String selectorExpression = gen.getSQL();
		joinSequence.addCondition( collectionTableAlias + '.' + indexCols[0] + " = " + selectorExpression );
		List paramSpecs = gen.getCollectedParameters();
		if ( paramSpecs != null ) {
			switch ( paramSpecs.size() ) {
				case 0 :
					// nothing to do
					break;
				case 1 :
					ParameterSpecification paramSpec = ( ParameterSpecification ) paramSpecs.get( 0 );
					paramSpec.setExpectedType( queryableCollection.getIndexType() );
					fromElement.setIndexCollectionSelectorParamSpec( paramSpec );
					break;
				default:
					fromElement.setIndexCollectionSelectorParamSpec(
							new AggregatedIndexCollectionSelectorParameterSpecifications( paramSpecs )
					);
					break;
			}
		}

		// Now, set the text for this node.  It should be the element columns.
		String[] elementColumns = queryableCollection.getElementColumnNames( elementTable );
		setText( elementColumns[0] );
		setResolved();
	}

	/**
	 * In the (rare?) case where the index selector contains multiple parameters...
	 */
	private static class AggregatedIndexCollectionSelectorParameterSpecifications implements ParameterSpecification {
		private final List paramSpecs;

		public AggregatedIndexCollectionSelectorParameterSpecifications(List paramSpecs) {
			this.paramSpecs = paramSpecs;
		}

		public int bind(PreparedStatement statement, QueryParameters qp, SessionImplementor session, int position)
		throws SQLException {
			int bindCount = 0;
			Iterator itr = paramSpecs.iterator();
			while ( itr.hasNext() ) {
				final ParameterSpecification paramSpec = ( ParameterSpecification ) itr.next();
				bindCount += paramSpec.bind( statement, qp, session, position + bindCount );
			}
			return bindCount;
		}

		public Type getExpectedType() {
			return null;
		}

		public void setExpectedType(Type expectedType) {
		}

		public String renderDisplayInfo() {
			return "index-selector [" + collectDisplayInfo() + "]" ;
		}

		private String collectDisplayInfo() {
			StringBuilder buffer = new StringBuilder();
			Iterator itr = paramSpecs.iterator();
			while ( itr.hasNext() ) {
				buffer.append( ( ( ParameterSpecification ) itr.next() ).renderDisplayInfo() );
			}
			return buffer.toString();
		}
	}
}
