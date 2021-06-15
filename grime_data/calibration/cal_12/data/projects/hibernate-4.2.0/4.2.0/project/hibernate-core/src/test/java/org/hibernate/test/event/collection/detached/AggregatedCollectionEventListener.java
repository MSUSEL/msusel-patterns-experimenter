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
package org.hibernate.test.event.collection.detached;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.jboss.logging.Logger;

import org.hibernate.HibernateException;
import org.hibernate.cfg.Configuration;
import org.hibernate.engine.spi.CollectionEntry;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.AbstractCollectionEvent;
import org.hibernate.event.spi.EventType;
import org.hibernate.event.spi.InitializeCollectionEvent;
import org.hibernate.event.spi.InitializeCollectionEventListener;
import org.hibernate.event.spi.PostCollectionRecreateEvent;
import org.hibernate.event.spi.PostCollectionRecreateEventListener;
import org.hibernate.event.spi.PostCollectionRemoveEvent;
import org.hibernate.event.spi.PostCollectionRemoveEventListener;
import org.hibernate.event.spi.PostCollectionUpdateEvent;
import org.hibernate.event.spi.PostCollectionUpdateEventListener;
import org.hibernate.event.spi.PreCollectionRecreateEvent;
import org.hibernate.event.spi.PreCollectionRecreateEventListener;
import org.hibernate.event.spi.PreCollectionRemoveEvent;
import org.hibernate.event.spi.PreCollectionRemoveEventListener;
import org.hibernate.event.spi.PreCollectionUpdateEvent;
import org.hibernate.event.spi.PreCollectionUpdateEventListener;
import org.hibernate.integrator.spi.Integrator;
import org.hibernate.metamodel.source.MetadataImplementor;
import org.hibernate.service.spi.SessionFactoryServiceRegistry;

/**
 * @author Steve Ebersole
 * @author Gail Badner
 */
public class AggregatedCollectionEventListener
		implements InitializeCollectionEventListener,
				   PreCollectionRecreateEventListener,
				   PostCollectionRecreateEventListener,
				   PreCollectionRemoveEventListener,
				   PostCollectionRemoveEventListener,
				   PreCollectionUpdateEventListener,
				   PostCollectionUpdateEventListener {

	private static final Logger log = Logger.getLogger( AggregatedCollectionEventListener.class );

	private final List<EventEntry> eventEntryList = new ArrayList<EventEntry>();

	public void reset() {
		eventEntryList.clear();
	}

	public List<EventEntry> getEventEntryList() {
		return eventEntryList;
	}

	@Override
	public void onInitializeCollection(InitializeCollectionEvent event) throws HibernateException {
		addEvent( event );
	}

	protected void addEvent(AbstractCollectionEvent event) {
		log.debugf( "Added collection event : %s", event );
		eventEntryList.add( new EventEntry( event ) );
	}


	// recreate ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	@Override
	public void onPreRecreateCollection(PreCollectionRecreateEvent event) {
		addEvent( event );
	}

	@Override
	public void onPostRecreateCollection(PostCollectionRecreateEvent event) {
		addEvent( event );
	}


	// remove ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	@Override
	public void onPreRemoveCollection(PreCollectionRemoveEvent event) {
		addEvent( event );
	}

	@Override
	public void onPostRemoveCollection(PostCollectionRemoveEvent event) {
		addEvent( event );
	}


	// update ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	@Override
	public void onPreUpdateCollection(PreCollectionUpdateEvent event) {
		addEvent( event );
	}

	@Override
	public void onPostUpdateCollection(PostCollectionUpdateEvent event) {
		addEvent( event );
	}

	public static class EventEntry {
		private final AbstractCollectionEvent event;
		private final Serializable snapshotAtTimeOfEventHandling;

		public EventEntry(AbstractCollectionEvent event) {
			this.event = event;
			// make a copy of the collection?
			this.snapshotAtTimeOfEventHandling = event.getSession()
					.getPersistenceContext()
					.getCollectionEntry( event.getCollection() )
					.getSnapshot();
		}

		public AbstractCollectionEvent getEvent() {
			return event;
		}

		public Serializable getSnapshotAtTimeOfEventHandling() {
			return snapshotAtTimeOfEventHandling;
		}
	}

	public static class IntegratorImpl implements Integrator {
		private AggregatedCollectionEventListener listener;

		public AggregatedCollectionEventListener getListener() {
			if ( listener == null ) {
				throw new HibernateException( "Integrator not yet processed" );
			}
			return listener;
		}

		@Override
		public void integrate(
				Configuration configuration,
				SessionFactoryImplementor sessionFactory,
				SessionFactoryServiceRegistry serviceRegistry) {
			integrate( serviceRegistry );
		}

		protected void integrate(SessionFactoryServiceRegistry serviceRegistry) {
			if ( listener != null ) {
				log.warn( "integrate called second time on testing collection listener Integrator (could be result of rebuilding SF on test failure)" );
			}
			listener = new AggregatedCollectionEventListener();

			final EventListenerRegistry listenerRegistry = serviceRegistry.getService( EventListenerRegistry.class );
			listenerRegistry.appendListeners( EventType.INIT_COLLECTION, listener );
			listenerRegistry.appendListeners( EventType.PRE_COLLECTION_RECREATE, listener );
			listenerRegistry.appendListeners( EventType.POST_COLLECTION_RECREATE, listener );
			listenerRegistry.appendListeners( EventType.PRE_COLLECTION_REMOVE, listener );
			listenerRegistry.appendListeners( EventType.POST_COLLECTION_REMOVE, listener );
			listenerRegistry.appendListeners( EventType.PRE_COLLECTION_UPDATE, listener );
			listenerRegistry.appendListeners( EventType.POST_COLLECTION_UPDATE, listener );
		}


		@Override
		public void integrate(
				MetadataImplementor metadata,
				SessionFactoryImplementor sessionFactory,
				SessionFactoryServiceRegistry serviceRegistry) {
			integrate( serviceRegistry );
		}

		@Override
		public void disintegrate(
				SessionFactoryImplementor sessionFactory, SessionFactoryServiceRegistry serviceRegistry) {
			//To change body of implemented methods use File | Settings | File Templates.
		}
	}
}
