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

import org.jboss.logging.Logger;

import org.hibernate.EntityMode;
import org.hibernate.HibernateException;
import org.hibernate.collection.spi.PersistentCollection;
import org.hibernate.engine.spi.PersistenceContext;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.event.spi.EventSource;
import org.hibernate.internal.CoreMessageLogger;
import org.hibernate.persister.collection.CollectionPersister;
import org.hibernate.persister.entity.EntityPersister;
import org.hibernate.type.CollectionType;
import org.hibernate.type.CompositeType;
import org.hibernate.type.Type;

/**
 * Wrap collections in a Hibernate collection
 * wrapper.
 * @author Gavin King
 */
public class WrapVisitor extends ProxyVisitor {

    private static final CoreMessageLogger LOG = Logger.getMessageLogger(CoreMessageLogger.class, WrapVisitor.class.getName());

	boolean substitute = false;

	boolean isSubstitutionRequired() {
		return substitute;
	}

	WrapVisitor(EventSource session) {
		super(session);
	}

	@Override
    Object processCollection(Object collection, CollectionType collectionType)
	throws HibernateException {

		if ( collection!=null && (collection instanceof PersistentCollection) ) {

			final SessionImplementor session = getSession();
			PersistentCollection coll = (PersistentCollection) collection;
			if ( coll.setCurrentSession(session) ) {
				reattachCollection( coll, collectionType );
			}
			return null;

		}
		else {
			return processArrayOrNewCollection(collection, collectionType);
		}

	}

	final Object processArrayOrNewCollection(Object collection, CollectionType collectionType)
	throws HibernateException {

		final SessionImplementor session = getSession();

		if (collection==null) {
			//do nothing
			return null;
		}
		else {
			CollectionPersister persister = session.getFactory().getCollectionPersister( collectionType.getRole() );

			final PersistenceContext persistenceContext = session.getPersistenceContext();
			//TODO: move into collection type, so we can use polymorphism!
			if ( collectionType.hasHolder() ) {

				if (collection==CollectionType.UNFETCHED_COLLECTION) return null;

				PersistentCollection ah = persistenceContext.getCollectionHolder(collection);
				if (ah==null) {
					ah = collectionType.wrap(session, collection);
					persistenceContext.addNewCollection( persister, ah );
					persistenceContext.addCollectionHolder(ah);
				}
				return null;
			}
			else {

				PersistentCollection persistentCollection = collectionType.wrap(session, collection);
				persistenceContext.addNewCollection( persister, persistentCollection );

				if ( LOG.isTraceEnabled() ) {
					LOG.tracev( "Wrapped collection in role: {0}", collectionType.getRole() );
				}

				return persistentCollection; //Force a substitution!

			}

		}

	}

	@Override
    void processValue(int i, Object[] values, Type[] types) {
		Object result = processValue( values[i], types[i] );
		if (result!=null) {
			substitute = true;
			values[i] = result;
		}
	}

	@Override
    Object processComponent(Object component, CompositeType componentType)
	throws HibernateException {

		if (component!=null) {
			Object[] values = componentType.getPropertyValues( component, getSession() );
			Type[] types = componentType.getSubtypes();
			boolean substituteComponent = false;
			for ( int i=0; i<types.length; i++ ) {
				Object result = processValue( values[i], types[i] );
				if (result!=null) {
					values[i] = result;
					substituteComponent = true;
				}
			}
			if (substituteComponent) {
				componentType.setPropertyValues( component, values, EntityMode.POJO );
			}
		}

		return null;
	}

	@Override
    void process(Object object, EntityPersister persister) throws HibernateException {
		final Object[] values = persister.getPropertyValues( object );
		final Type[] types = persister.getPropertyTypes();
		processEntityPropertyValues( values, types );
		if ( isSubstitutionRequired() ) {
			persister.setPropertyValues( object, values );
		}
	}
}
