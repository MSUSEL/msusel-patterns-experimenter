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
package org.hibernate.event.service.spi;

import java.io.Serializable;

import org.hibernate.event.spi.EventType;

/**
 * Contract for a groups of events listeners for a particular event type.
 *
 * @author Steve Ebersole
 */
public interface EventListenerGroup<T> extends Serializable {

	/**
	 * Retrieve the event type associated with this groups of listeners.
	 *
	 * @return The event type.
	 */
	public EventType<T> getEventType();

	/**
	 * Are there no listeners registered?
	 *
	 * @return {@literal true} if no listeners are registered; {@literal false} otherwise.
	 */
	public boolean isEmpty();

	public int count();

	public Iterable<T> listeners();

	/**
	 * Mechanism to more finely control the notion of duplicates.
	 * <p/>
	 * For example, say you are registering listeners for an extension library.  This extension library
	 * could define a "marker interface" which indicates listeners related to it and register a strategy
	 * that checks against that marker interface.
	 *
	 * @param strategy The duplication strategy
	 */
	public void addDuplicationStrategy(DuplicationStrategy strategy);

	public void appendListener(T listener);
	public void appendListeners(T... listeners);

	public void prependListener(T listener);
	public void prependListeners(T... listeners);

	public void clear();

}
