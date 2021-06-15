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
package org.hibernate.loader;
import org.hibernate.LockMode;
import org.hibernate.LockOptions;
import org.hibernate.dialect.Dialect;
import org.hibernate.engine.spi.LoadQueryInfluencers;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.persister.collection.CollectionPersister;
import org.hibernate.persister.entity.Loadable;
import org.hibernate.type.EntityType;

/**
 * Implements logic for walking a tree of associated classes.
 *
 * Generates an SQL select string containing all properties of those classes.
 * Tables are joined using an ANSI-style left outer join.
 *
 * @author Gavin King
 */
public abstract class OuterJoinLoader extends BasicLoader {

	protected Loadable[] persisters;
	protected CollectionPersister[] collectionPersisters;
	protected int[] collectionOwners;
	protected String[] aliases;
	private LockOptions lockOptions;
	protected LockMode[] lockModeArray;
	protected int[] owners;
	protected EntityType[] ownerAssociationTypes;
	protected String sql;
	protected String[] suffixes;
	protected String[] collectionSuffixes;

    private LoadQueryInfluencers loadQueryInfluencers;

	protected final Dialect getDialect() {
    	return getFactory().getDialect();
    }

	public OuterJoinLoader(
			SessionFactoryImplementor factory,
			LoadQueryInfluencers loadQueryInfluencers) {
		super( factory );
		this.loadQueryInfluencers = loadQueryInfluencers;
	}

	protected String[] getSuffixes() {
		return suffixes;
	}

	protected String[] getCollectionSuffixes() {
		return collectionSuffixes;
	}

	@Override
	public final String getSQLString() {
		return sql;
	}

	protected final Loadable[] getEntityPersisters() {
		return persisters;
	}

	protected int[] getOwners() {
		return owners;
	}

	protected EntityType[] getOwnerAssociationTypes() {
		return ownerAssociationTypes;
	}

	protected LockMode[] getLockModes(LockOptions lockOptions) {
		return lockModeArray;
	}

	protected LockOptions getLockOptions() {
		return lockOptions;
	}

	public LoadQueryInfluencers getLoadQueryInfluencers() {
		return loadQueryInfluencers;
	}

	protected final String[] getAliases() {
		return aliases;
	}

	protected final CollectionPersister[] getCollectionPersisters() {
		return collectionPersisters;
	}

	protected final int[] getCollectionOwners() {
		return collectionOwners;
	}

	protected void initFromWalker(JoinWalker walker) {
		persisters = walker.getPersisters();
		collectionPersisters = walker.getCollectionPersisters();
		ownerAssociationTypes = walker.getOwnerAssociationTypes();
		lockOptions = walker.getLockModeOptions();
		lockModeArray = walker.getLockModeArray();
		suffixes = walker.getSuffixes();
		collectionSuffixes = walker.getCollectionSuffixes();
		owners = walker.getOwners();
		collectionOwners = walker.getCollectionOwners();
		sql = walker.getSQLString();
		aliases = walker.getAliases();
	}

}
