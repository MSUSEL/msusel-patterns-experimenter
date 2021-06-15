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
package org.hibernate.internal;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.jboss.logging.Logger;

import org.hibernate.engine.internal.StatefulPersistenceContext;
import org.hibernate.engine.spi.ActionQueue;
import org.hibernate.engine.spi.NonFlushedChanges;
import org.hibernate.event.spi.EventSource;

public final class NonFlushedChangesImpl implements NonFlushedChanges, Serializable {
    private static final Logger LOG = Logger.getLogger( NonFlushedChangesImpl.class.getName() );

	private transient ActionQueue actionQueue;
	private transient StatefulPersistenceContext persistenceContext;

	public NonFlushedChangesImpl(EventSource session) {
		this.actionQueue = session.getActionQueue();
		this.persistenceContext = ( StatefulPersistenceContext ) session.getPersistenceContext();
	}

	/* package-protected */
	ActionQueue getActionQueue() {
		return actionQueue;
	}

	/* package-protected */
	StatefulPersistenceContext getPersistenceContext() {
		return persistenceContext;
	}

	public void clear() {
	}

	private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
		LOG.trace( "Deserializing NonFlushedChangesImpl" );
		ois.defaultReadObject();
		persistenceContext = StatefulPersistenceContext.deserialize( ois, null );
		actionQueue = ActionQueue.deserialize( ois, null );
	}

	private void writeObject(ObjectOutputStream oos) throws IOException {
		LOG.trace( "Serializing NonFlushedChangesImpl" );
		oos.defaultWriteObject();
		persistenceContext.serialize( oos );
		actionQueue.serialize( oos );
	}

}