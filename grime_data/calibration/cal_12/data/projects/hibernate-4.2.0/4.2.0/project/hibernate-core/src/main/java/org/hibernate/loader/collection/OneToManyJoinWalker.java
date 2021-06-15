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
package org.hibernate.loader.collection;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hibernate.LockMode;
import org.hibernate.MappingException;
import org.hibernate.engine.spi.LoadQueryInfluencers;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.internal.util.StringHelper;
import org.hibernate.loader.BasicLoader;
import org.hibernate.loader.OuterJoinableAssociation;
import org.hibernate.persister.collection.QueryableCollection;
import org.hibernate.persister.entity.OuterJoinLoadable;
import org.hibernate.sql.JoinFragment;
import org.hibernate.sql.Select;

/**
 * Walker for one-to-many associations
 *
 * @see OneToManyLoader
 * @author Gavin King
 */
public class OneToManyJoinWalker extends CollectionJoinWalker {

	private final QueryableCollection oneToManyPersister;

	@Override
    protected boolean isDuplicateAssociation(
		final String foreignKeyTable,
		final String[] foreignKeyColumns
	) {
		//disable a join back to this same association
		final boolean isSameJoin = oneToManyPersister.getTableName().equals(foreignKeyTable) &&
			Arrays.equals( foreignKeyColumns, oneToManyPersister.getKeyColumnNames() );
		return isSameJoin ||
			super.isDuplicateAssociation(foreignKeyTable, foreignKeyColumns);
	}

	public OneToManyJoinWalker(
			QueryableCollection oneToManyPersister,
			int batchSize,
			String subquery,
			SessionFactoryImplementor factory,
			LoadQueryInfluencers loadQueryInfluencers) throws MappingException {
		super( factory, loadQueryInfluencers );

		this.oneToManyPersister = oneToManyPersister;

		final OuterJoinLoadable elementPersister = (OuterJoinLoadable) oneToManyPersister.getElementPersister();
		final String alias = generateRootAlias( oneToManyPersister.getRole() );

		walkEntityTree(elementPersister, alias);

		List allAssociations = new ArrayList();
		allAssociations.addAll(associations);
		allAssociations.add( OuterJoinableAssociation.createRoot( oneToManyPersister.getCollectionType(), alias, getFactory() ) );
		initPersisters(allAssociations, LockMode.NONE);
		initStatementString(elementPersister, alias, batchSize, subquery);
	}

	private void initStatementString(
		final OuterJoinLoadable elementPersister,
		final String alias,
		final int batchSize,
		final String subquery)
	throws MappingException {

		final int joins = countEntityPersisters( associations );
		suffixes = BasicLoader.generateSuffixes( joins + 1 );

		final int collectionJoins = countCollectionPersisters( associations ) + 1;
		collectionSuffixes = BasicLoader.generateSuffixes( joins + 1, collectionJoins );

		StringBuilder whereString = whereString(
				alias,
				oneToManyPersister.getKeyColumnNames(),
				subquery,
				batchSize
			);
		String filter = oneToManyPersister.filterFragment( alias, getLoadQueryInfluencers().getEnabledFilters() );
		whereString.insert( 0, StringHelper.moveAndToBeginning( filter ) );

		JoinFragment ojf = mergeOuterJoins(associations);
		Select select = new Select( getDialect() )
			.setSelectClause(
				oneToManyPersister.selectFragment(null, null, alias, suffixes[joins], collectionSuffixes[0], true) +
				selectString(associations)
			)
			.setFromClause(
				elementPersister.fromTableFragment(alias) +
				elementPersister.fromJoinFragment(alias, true, true)
			)
			.setWhereClause( whereString.toString() )
			.setOuterJoins(
				ojf.toFromFragmentString(),
				ojf.toWhereFragmentString() +
				elementPersister.whereJoinFragment(alias, true, true)
			);

		select.setOrderByClause( orderBy( associations, oneToManyPersister.getSQLOrderByString(alias) ) );

		if ( getFactory().getSettings().isCommentsEnabled() ) {
			select.setComment( "load one-to-many " + oneToManyPersister.getRole() );
		}

		sql = select.toStatementString();
	}

	@Override
    public String toString() {
		return getClass().getName() + '(' + oneToManyPersister.getRole() + ')';
	}

}
