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
import org.hibernate.service.Service;

/**
 * Service for accessing each {@link EventListenerGroup} by {@link EventType}, as well as convenience
 * methods for managing the listeners registered in each {@link EventListenerGroup}.
 *
 * @author Steve Ebersole
 */
public interface EventListenerRegistry extends Service, Serializable {
	public <T> EventListenerGroup<T> getEventListenerGroup(EventType<T> eventType);

	public void addDuplicationStrategy(DuplicationStrategy strategy);

	public <T> void setListeners(EventType<T> type, Class<? extends T>... listeners);
	public <T> void setListeners(EventType<T> type, T... listeners);

	public <T> void appendListeners(EventType<T> type, Class<? extends T>... listeners);
	public <T> void appendListeners(EventType<T> type, T... listeners);

	public <T> void prependListeners(EventType<T> type, Class<? extends T>... listeners);
	public <T> void prependListeners(EventType<T> type, T... listeners);
}
