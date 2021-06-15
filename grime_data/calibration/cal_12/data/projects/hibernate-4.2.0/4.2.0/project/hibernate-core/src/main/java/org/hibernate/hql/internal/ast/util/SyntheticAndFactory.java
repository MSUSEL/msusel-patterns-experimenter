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
package org.hibernate.hql.internal.ast.util;

import java.util.Map;

import antlr.collections.AST;
import org.jboss.logging.Logger;

import org.hibernate.hql.internal.antlr.HqlSqlTokenTypes;
import org.hibernate.hql.internal.ast.HqlSqlWalker;
import org.hibernate.hql.internal.ast.tree.FromElement;
import org.hibernate.hql.internal.ast.tree.Node;
import org.hibernate.hql.internal.ast.tree.QueryNode;
import org.hibernate.hql.internal.ast.tree.RestrictableStatement;
import org.hibernate.hql.internal.ast.tree.SqlFragment;
import org.hibernate.internal.CoreMessageLogger;
import org.hibernate.internal.util.StringHelper;
import org.hibernate.param.CollectionFilterKeyParameterSpecification;
import org.hibernate.persister.entity.Queryable;
import org.hibernate.sql.JoinFragment;
import org.hibernate.type.Type;

/**
 * Creates synthetic and nodes based on the where fragment part of a JoinSequence.
 *
 * @author josh
 */
public class SyntheticAndFactory implements HqlSqlTokenTypes {

    private static final CoreMessageLogger LOG = Logger.getMessageLogger(CoreMessageLogger.class, SyntheticAndFactory.class.getName());

	private HqlSqlWalker hqlSqlWalker;
	private AST thetaJoins;
	private AST filters;

	public SyntheticAndFactory(HqlSqlWalker hqlSqlWalker) {
		this.hqlSqlWalker = hqlSqlWalker;
	}

	private Node create(int tokenType, String text) {
		return ( Node ) ASTUtil.create( hqlSqlWalker.getASTFactory(), tokenType, text );
	}

	public void addWhereFragment(
			JoinFragment joinFragment,
			String whereFragment,
			QueryNode query,
			FromElement fromElement,
			HqlSqlWalker hqlSqlWalker) {
		if ( whereFragment == null ) {
			return;
		}

		if ( !fromElement.useWhereFragment() && !joinFragment.hasThetaJoins() ) {
			return;
		}

		whereFragment = whereFragment.trim();
		if ( StringHelper.isEmpty( whereFragment ) ) {
			return;
		}

		// Forcefully remove leading ands from where fragments; the grammar will
		// handle adding them
		if ( whereFragment.startsWith( "and" ) ) {
			whereFragment = whereFragment.substring( 4 );
		}

		LOG.debugf( "Using unprocessed WHERE-fragment [%s]", whereFragment );

		SqlFragment fragment = ( SqlFragment ) create( SQL_TOKEN, whereFragment );
		fragment.setJoinFragment( joinFragment );
		fragment.setFromElement( fromElement );

		if ( fromElement.getIndexCollectionSelectorParamSpec() != null ) {
			fragment.addEmbeddedParameter( fromElement.getIndexCollectionSelectorParamSpec() );
			fromElement.setIndexCollectionSelectorParamSpec( null );
		}

		if ( hqlSqlWalker.isFilter() ) {
			if ( whereFragment.indexOf( '?' ) >= 0 ) {
				Type collectionFilterKeyType = hqlSqlWalker.getSessionFactoryHelper()
						.requireQueryableCollection( hqlSqlWalker.getCollectionFilterRole() )
						.getKeyType();
				CollectionFilterKeyParameterSpecification paramSpec = new CollectionFilterKeyParameterSpecification(
						hqlSqlWalker.getCollectionFilterRole(),
						collectionFilterKeyType,
						0
				);
				fragment.addEmbeddedParameter( paramSpec );
			}
		}

		JoinProcessor.processDynamicFilterParameters(
				whereFragment,
				fragment,
				hqlSqlWalker
		);

		if ( LOG.isDebugEnabled() ) {
			LOG.debugf( "Using processed WHERE-fragment [%s]", fragment.getText() );
		}

		// Filter conditions need to be inserted before the HQL where condition and the
		// theta join node.  This is because org.hibernate.loader.Loader binds the filter parameters first,
		// then it binds all the HQL query parameters, see org.hibernate.loader.Loader.processFilterParameters().
		if ( fragment.getFromElement().isFilter() || fragment.hasFilterCondition() ) {
			if ( filters == null ) {
				// Find or create the WHERE clause
				AST where = query.getWhereClause();
				// Create a new FILTERS node as a parent of all filters
				filters = create( FILTERS, "{filter conditions}" );
				// Put the FILTERS node before the HQL condition and theta joins
				ASTUtil.insertChild( where, filters );
			}

			// add the current fragment to the FILTERS node
			filters.addChild( fragment );
		}
		else {
			if ( thetaJoins == null ) {
				// Find or create the WHERE clause
				AST where = query.getWhereClause();
				// Create a new THETA_JOINS node as a parent of all filters
				thetaJoins = create( THETA_JOINS, "{theta joins}" );
				// Put the THETA_JOINS node before the HQL condition, after the filters.
				if (filters==null) {
					ASTUtil.insertChild( where, thetaJoins );
				}
				else {
					ASTUtil.insertSibling( thetaJoins, filters );
				}
			}

			// add the current fragment to the THETA_JOINS node
			thetaJoins.addChild(fragment);
		}

	}

	public void addDiscriminatorWhereFragment(
			RestrictableStatement statement,
			Queryable persister,
			Map enabledFilters,
			String alias) {
		String whereFragment = persister.filterFragment( alias, enabledFilters ).trim();
		if ( "".equals( whereFragment ) ) {
			return;
		}
		if ( whereFragment.startsWith( "and" ) ) {
			whereFragment = whereFragment.substring( 4 );
		}

		// Need to parse off the column qualifiers; this is assuming (which is true as of now)
		// that this is only used from update and delete HQL statement parsing
		whereFragment = StringHelper.replace( whereFragment, persister.generateFilterConditionAlias( alias ) + ".", "" );

		// Note: this simply constructs a "raw" SQL_TOKEN representing the
		// where fragment and injects this into the tree.  This "works";
		// however it is probably not the best long-term solution.
		//
		// At some point we probably want to apply an additional grammar to
		// properly tokenize this where fragment into constituent parts
		// focused on the operators embedded within the fragment.
		SqlFragment discrimNode = ( SqlFragment ) create( SQL_TOKEN, whereFragment );

		JoinProcessor.processDynamicFilterParameters(
				whereFragment,
				discrimNode,
				hqlSqlWalker
		);

		if ( statement.getWhereClause().getNumberOfChildren() == 0 ) {
			statement.getWhereClause().setFirstChild( discrimNode );
		}
		else {
			AST and = create( AND, "{and}" );
			AST currentFirstChild = statement.getWhereClause().getFirstChild();
			and.setFirstChild( discrimNode );
			and.addChild( currentFirstChild );
			statement.getWhereClause().setFirstChild( and );
		}
	}
}
