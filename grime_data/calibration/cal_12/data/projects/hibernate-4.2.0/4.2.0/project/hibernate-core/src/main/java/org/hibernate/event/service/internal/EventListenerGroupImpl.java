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
package org.hibernate.event.service.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import org.hibernate.event.service.spi.DuplicationStrategy;
import org.hibernate.event.service.spi.EventListenerGroup;
import org.hibernate.event.service.spi.EventListenerRegistrationException;
import org.hibernate.event.spi.EventType;

/**
 * @author Steve Ebersole
 */
public class EventListenerGroupImpl<T> implements EventListenerGroup<T> {
	private EventType<T> eventType;

	private final Set<DuplicationStrategy> duplicationStrategies = new LinkedHashSet<DuplicationStrategy>();
	private List<T> listeners;

	public EventListenerGroupImpl(EventType<T> eventType) {
		this.eventType = eventType;
		duplicationStrategies.add(
				// At minimum make sure we do not register the same exact listener class multiple times.
				new DuplicationStrategy() {
					@Override
					public boolean areMatch(Object listener, Object original) {
						return listener.getClass().equals( original.getClass() );
					}

					@Override
					public Action getAction() {
						return Action.ERROR;
					}
				}
		);
	}

	@Override
	public EventType<T> getEventType() {
		return eventType;
	}

	@Override
	public boolean isEmpty() {
		return count() <= 0;
	}

	@Override
	public int count() {
		return listeners == null ? 0 : listeners.size();
	}

	@Override
	public void clear() {
		if ( duplicationStrategies != null ) {
			duplicationStrategies.clear();
		}
		if ( listeners != null ) {
			listeners.clear();
		}
	}

	@Override
	public void addDuplicationStrategy(DuplicationStrategy strategy) {
		duplicationStrategies.add( strategy );
	}

	public Iterable<T> listeners() {
		return listeners == null ? Collections.<T>emptyList() : listeners;
	}

	@Override
	public void appendListeners(T... listeners) {
		for ( T listener : listeners ) {
			appendListener( listener );
		}
	}

	@Override
	public void appendListener(T listener) {
		if ( listenerShouldGetAdded( listener ) ) {
			internalAppend( listener );
		}
	}

	@Override
	public void prependListeners(T... listeners) {
		for ( T listener : listeners ) {
			prependListener( listener );
		}
	}

	@Override
	public void prependListener(T listener) {
		if ( listenerShouldGetAdded( listener ) ) {
			internalPrepend( listener );
		}
	}

	private boolean listenerShouldGetAdded(T listener) {
		if ( listeners == null ) {
			listeners = new ArrayList<T>();
			return true;
			// no need to do de-dup checks
		}

		boolean doAdd = true;
		strategy_loop: for ( DuplicationStrategy strategy : duplicationStrategies ) {
			final ListIterator<T> itr = listeners.listIterator();
			while ( itr.hasNext() ) {
				final T existingListener = itr.next();
				if ( strategy.areMatch( listener,  existingListener ) ) {
					switch ( strategy.getAction() ) {
						// todo : add debug logging of what happens here...
						case ERROR: {
							throw new EventListenerRegistrationException( "Duplicate event listener found" );
						}
						case KEEP_ORIGINAL: {
							doAdd = false;
							break strategy_loop;
						}
						case REPLACE_ORIGINAL: {
							itr.set( listener );
							doAdd = false;
							break strategy_loop;
						}
					}
				}
			}
		}
		return doAdd;
	}

	private void internalPrepend(T listener) {
		checkAgainstBaseInterface( listener );
		listeners.add( 0, listener );
	}

	private void checkAgainstBaseInterface(T listener) {
		if ( !eventType.baseListenerInterface().isInstance( listener ) ) {
			throw new EventListenerRegistrationException(
					"Listener did not implement expected interface [" + eventType.baseListenerInterface().getName() + "]"
			);
		}
	}

	private void internalAppend(T listener) {
		checkAgainstBaseInterface( listener );
		listeners.add( listener );
	}
}
