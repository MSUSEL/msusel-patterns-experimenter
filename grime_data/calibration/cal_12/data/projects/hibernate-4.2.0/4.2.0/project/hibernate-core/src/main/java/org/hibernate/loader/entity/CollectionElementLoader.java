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
package org.hibernate.loader.entity;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.jboss.logging.Logger;

import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.MappingException;
import org.hibernate.engine.spi.LoadQueryInfluencers;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.internal.CoreMessageLogger;
import org.hibernate.internal.util.collections.ArrayHelper;
import org.hibernate.loader.JoinWalker;
import org.hibernate.loader.OuterJoinLoader;
import org.hibernate.persister.collection.QueryableCollection;
import org.hibernate.persister.entity.OuterJoinLoadable;
import org.hibernate.transform.ResultTransformer;
import org.hibernate.type.Type;

/**
 *
 *
 * @author Gavin King
 */
public class CollectionElementLoader extends OuterJoinLoader {

	private static final CoreMessageLogger LOG = Logger.getMessageLogger( CoreMessageLogger.class, CollectionElementLoader.class.getName() );

	private final OuterJoinLoadable persister;
	private final Type keyType;
	private final Type indexType;
	private final String entityName;

	public CollectionElementLoader(
			QueryableCollection collectionPersister,
			SessionFactoryImplementor factory,
			LoadQueryInfluencers loadQueryInfluencers) throws MappingException {
		super( factory, loadQueryInfluencers );

		this.keyType = collectionPersister.getKeyType();
		this.indexType = collectionPersister.getIndexType();
		this.persister = (OuterJoinLoadable) collectionPersister.getElementPersister();
		this.entityName = persister.getEntityName();

		JoinWalker walker = new EntityJoinWalker(
				persister, 
				ArrayHelper.join(
						collectionPersister.getKeyColumnNames(),
						collectionPersister.getIndexColumnNames()
				),
				1, 
				LockMode.NONE, 
				factory, 
				loadQueryInfluencers
			);
		initFromWalker( walker );

		postInstantiate();

		if ( LOG.isDebugEnabled() ) {
			LOG.debugf( "Static select for entity %s: %s", entityName, getSQLString() );
		}

	}

	public Object loadElement(SessionImplementor session, Object key, Object index)
	throws HibernateException {

		List list = loadEntity(
				session,
				key,
				index,
				keyType,
				indexType,
				persister
			);

		if ( list.size()==1 ) {
			return list.get(0);
		}
		else if ( list.size()==0 ) {
			return null;
		}
		else {
			if ( getCollectionOwners()!=null ) {
				return list.get(0);
			}
			else {
				throw new HibernateException("More than one row was found");
			}
		}

	}

	@Override
    protected Object getResultColumnOrRow(
		Object[] row,
		ResultTransformer transformer,
		ResultSet rs, SessionImplementor session)
	throws SQLException, HibernateException {
		return row[row.length-1];
	}

	@Override
    protected boolean isSingleRowLoader() {
		return true;
	}
}