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
package org.hibernate.event.spi;

import java.lang.reflect.Field;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.hibernate.HibernateException;

/**
 * Enumeration of the recognized types of events, including meta-information about each.
 *
 * @author Steve Ebersole
 */
public class EventType<T> {
	public static final EventType<LoadEventListener> LOAD
			= new EventType<LoadEventListener>( "load", LoadEventListener.class );
	public static final EventType<ResolveNaturalIdEventListener> RESOLVE_NATURAL_ID
			= new EventType<ResolveNaturalIdEventListener>( "resolve-natural-id", ResolveNaturalIdEventListener.class );
	public static final EventType<InitializeCollectionEventListener> INIT_COLLECTION
			= new EventType<InitializeCollectionEventListener>( "load-collection", InitializeCollectionEventListener.class );

	public static final EventType<SaveOrUpdateEventListener> SAVE_UPDATE
			= new EventType<SaveOrUpdateEventListener>( "save-update", SaveOrUpdateEventListener.class );
	public static final EventType<SaveOrUpdateEventListener> UPDATE
			= new EventType<SaveOrUpdateEventListener>( "update", SaveOrUpdateEventListener.class );
	public static final EventType<SaveOrUpdateEventListener> SAVE
			= new EventType<SaveOrUpdateEventListener>( "save", SaveOrUpdateEventListener.class );
	public static final EventType<PersistEventListener> PERSIST
			= new EventType<PersistEventListener>( "create", PersistEventListener.class );
	public static final EventType<PersistEventListener> PERSIST_ONFLUSH
			= new EventType<PersistEventListener>( "create-onflush", PersistEventListener.class );

	public static final EventType<MergeEventListener> MERGE
			= new EventType<MergeEventListener>( "merge", MergeEventListener.class );

	public static final EventType<DeleteEventListener> DELETE
			= new EventType<DeleteEventListener>( "delete", DeleteEventListener.class );

	public static final EventType<ReplicateEventListener> REPLICATE
			= new EventType<ReplicateEventListener>( "replicate", ReplicateEventListener.class );

	public static final EventType<FlushEventListener> FLUSH
			= new EventType<FlushEventListener>( "flush", FlushEventListener.class );
	public static final EventType<AutoFlushEventListener> AUTO_FLUSH
			= new EventType<AutoFlushEventListener>( "auto-flush", AutoFlushEventListener.class );
	public static final EventType<DirtyCheckEventListener> DIRTY_CHECK
			= new EventType<DirtyCheckEventListener>( "dirty-check", DirtyCheckEventListener.class );
	public static final EventType<FlushEntityEventListener> FLUSH_ENTITY
			= new EventType<FlushEntityEventListener>( "flush-entity", FlushEntityEventListener.class );

	public static final EventType<EvictEventListener> EVICT
			= new EventType<EvictEventListener>( "evict", EvictEventListener.class );

	public static final EventType<LockEventListener> LOCK
			= new EventType<LockEventListener>( "lock", LockEventListener.class );

	public static final EventType<RefreshEventListener> REFRESH
			= new EventType<RefreshEventListener>( "refresh", RefreshEventListener.class );

	public static final EventType<PreLoadEventListener> PRE_LOAD
			= new EventType<PreLoadEventListener>( "pre-load", PreLoadEventListener.class );
	public static final EventType<PreDeleteEventListener> PRE_DELETE
			= new EventType<PreDeleteEventListener>( "pre-delete", PreDeleteEventListener.class );
	public static final EventType<PreUpdateEventListener> PRE_UPDATE
			= new EventType<PreUpdateEventListener>( "pre-update", PreUpdateEventListener.class );
	public static final EventType<PreInsertEventListener> PRE_INSERT
			= new EventType<PreInsertEventListener>( "pre-insert", PreInsertEventListener.class );

	public static final EventType<PostLoadEventListener> POST_LOAD
			= new EventType<PostLoadEventListener>( "post-load", PostLoadEventListener.class );
	public static final EventType<PostDeleteEventListener> POST_DELETE
			= new EventType<PostDeleteEventListener>( "post-delete", PostDeleteEventListener.class );
	public static final EventType<PostUpdateEventListener> POST_UPDATE
			= new EventType<PostUpdateEventListener>( "post-update", PostUpdateEventListener.class );
	public static final EventType<PostInsertEventListener> POST_INSERT
			= new EventType<PostInsertEventListener>( "post-insert", PostInsertEventListener.class );

	public static final EventType<PostDeleteEventListener> POST_COMMIT_DELETE
			= new EventType<PostDeleteEventListener>( "post-commit-delete", PostDeleteEventListener.class );
	public static final EventType<PostUpdateEventListener> POST_COMMIT_UPDATE
			= new EventType<PostUpdateEventListener>( "post-commit-update", PostUpdateEventListener.class );
	public static final EventType<PostInsertEventListener> POST_COMMIT_INSERT
			= new EventType<PostInsertEventListener>( "post-commit-insert", PostInsertEventListener.class );

	public static final EventType<PreCollectionRecreateEventListener> PRE_COLLECTION_RECREATE
			= new EventType<PreCollectionRecreateEventListener>( "pre-collection-recreate", PreCollectionRecreateEventListener.class );
	public static final EventType<PreCollectionRemoveEventListener> PRE_COLLECTION_REMOVE
			= new EventType<PreCollectionRemoveEventListener>( "pre-collection-remove", PreCollectionRemoveEventListener.class );
	public static final EventType<PreCollectionUpdateEventListener> PRE_COLLECTION_UPDATE
			= new EventType<PreCollectionUpdateEventListener>( "pre-collection-update", PreCollectionUpdateEventListener.class );

	public static final EventType<PostCollectionRecreateEventListener> POST_COLLECTION_RECREATE
			= new EventType<PostCollectionRecreateEventListener>( "post-collection-recreate", PostCollectionRecreateEventListener.class );
	public static final EventType<PostCollectionRemoveEventListener> POST_COLLECTION_REMOVE
			= new EventType<PostCollectionRemoveEventListener>( "post-collection-remove", PostCollectionRemoveEventListener.class );
	public static final EventType<PostCollectionUpdateEventListener> POST_COLLECTION_UPDATE
			= new EventType<PostCollectionUpdateEventListener>( "post-collection-update", PostCollectionUpdateEventListener.class );


	/**
	 * Maintain a map of {@link EventType} instances keyed by name for lookup by name as well as {@link #values()}
	 * resolution.
	 */
	public static final Map<String,EventType> eventTypeByNameMap = AccessController.doPrivileged(
			new PrivilegedAction<Map<String, EventType>>() {
				@Override
				public Map<String, EventType> run() {
					final Map<String, EventType> typeByNameMap = new HashMap<String, EventType>();
					final Field[] fields = EventType.class.getDeclaredFields();
					for ( int i = 0, max = fields.length; i < max; i++ ) {
						if ( EventType.class.isAssignableFrom( fields[i].getType() ) ) {
							try {
								final EventType typeField = ( EventType ) fields[i].get( null );
								typeByNameMap.put( typeField.eventName(), typeField );
							}
							catch( Exception t ) {
								throw new HibernateException( "Unable to initialize EventType map", t );
							}
						}
					}
					return typeByNameMap;
				}
			}
	);

	/**
	 * Find an {@link EventType} by its name
	 *
	 * @param eventName The name
	 *
	 * @return The {@link EventType} instance.
	 *
	 * @throws HibernateException If eventName is null, or if eventName does not correlate to any known event type.
	 */
	public static EventType resolveEventTypeByName(final String eventName) {
		if ( eventName == null ) {
			throw new HibernateException( "event name to resolve cannot be null" );
		}
		final EventType eventType = eventTypeByNameMap.get( eventName );
		if ( eventType == null ) {
			throw new HibernateException( "Unable to locate proper event type for event name [" + eventName + "]" );
		}
		return eventType;
	}

	/**
	 * Get a collection of all {@link EventType} instances.
	 *
	 * @return All {@link EventType} instances
	 */
	public static Collection<EventType> values() {
		return eventTypeByNameMap.values();
	}


	private final String eventName;
	private final Class<? extends T> baseListenerInterface;

	private EventType(String eventName, Class<? extends T> baseListenerInterface) {
		this.eventName = eventName;
		this.baseListenerInterface = baseListenerInterface;
	}

	public String eventName() {
		return eventName;
	}

	public Class baseListenerInterface() {
		return baseListenerInterface;
	}

	@Override
	public String toString() {
		return eventName();
	}
}
