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
package org.hibernate.event.internal;

import java.io.Serializable;

import org.hibernate.HibernateException;
import org.hibernate.collection.spi.PersistentCollection;
import org.hibernate.event.spi.EventSource;
import org.hibernate.persister.collection.CollectionPersister;
import org.hibernate.type.CollectionType;

/**
 * When an entity is passed to replicate(), and there is an existing row, we must
 * inspect all its collections and
 * 1. associate any uninitialized PersistentCollections with this session
 * 2. associate any initialized PersistentCollections with this session, using the
 * existing snapshot
 * 3. execute a collection removal (SQL DELETE) for each null collection property
 * or "new" collection
 *
 * @author Gavin King
 */
public class OnReplicateVisitor extends ReattachVisitor {

	private boolean isUpdate;

	OnReplicateVisitor(EventSource session, Serializable key, Object owner, boolean isUpdate) {
		super( session, key, owner );
		this.isUpdate = isUpdate;
	}

	Object processCollection(Object collection, CollectionType type)
			throws HibernateException {

		if ( collection == CollectionType.UNFETCHED_COLLECTION ) {
			return null;
		}

		EventSource session = getSession();
		CollectionPersister persister = session.getFactory().getCollectionPersister( type.getRole() );

		if ( isUpdate ) {
			removeCollection( persister, extractCollectionKeyFromOwner( persister ), session );
		}
		if ( collection != null && ( collection instanceof PersistentCollection ) ) {
			PersistentCollection wrapper = ( PersistentCollection ) collection;
			wrapper.setCurrentSession( session );
			if ( wrapper.wasInitialized() ) {
				session.getPersistenceContext().addNewCollection( persister, wrapper );
			}
			else {
				reattachCollection( wrapper, type );
			}
		}
		else {
			// otherwise a null or brand new collection
			// this will also (inefficiently) handle arrays, which
			// have no snapshot, so we can't do any better
			//processArrayOrNewCollection(collection, type);
		}

		return null;

	}

}
