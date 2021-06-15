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
package org.hibernate.bytecode.instrumentation.spi;
import java.io.Serializable;
import java.util.Set;

import org.hibernate.LazyInitializationException;
import org.hibernate.engine.spi.SessionImplementor;

/**
 * @author Steve Ebersole
 */
public abstract class AbstractFieldInterceptor implements FieldInterceptor, Serializable {

	private transient SessionImplementor session;
	private Set uninitializedFields;
	private final String entityName;

	private transient boolean initializing;
	private boolean dirty;

	protected AbstractFieldInterceptor(SessionImplementor session, Set uninitializedFields, String entityName) {
		this.session = session;
		this.uninitializedFields = uninitializedFields;
		this.entityName = entityName;
	}


	// FieldInterceptor impl ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	public final void setSession(SessionImplementor session) {
		this.session = session;
	}

	public final boolean isInitialized() {
		return uninitializedFields == null || uninitializedFields.size() == 0;
	}

	public final boolean isInitialized(String field) {
		return uninitializedFields == null || !uninitializedFields.contains( field );
	}

	public final void dirty() {
		dirty = true;
	}

	public final boolean isDirty() {
		return dirty;
	}

	public final void clearDirty() {
		dirty = false;
	}


	// subclass accesses ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	protected final Object intercept(Object target, String fieldName, Object value) {
		if ( initializing ) {
			return value;
		}

		if ( uninitializedFields != null && uninitializedFields.contains( fieldName ) ) {
			if ( session == null ) {
				throw new LazyInitializationException( "entity with lazy properties is not associated with a session" );
			}
			else if ( !session.isOpen() || !session.isConnected() ) {
				throw new LazyInitializationException( "session is not connected" );
			}

			final Object result;
			initializing = true;
			try {
				result = ( ( LazyPropertyInitializer ) session.getFactory()
						.getEntityPersister( entityName ) )
						.initializeLazyProperty( fieldName, target, session );
			}
			finally {
				initializing = false;
			}
			uninitializedFields = null; //let's assume that there is only one lazy fetch group, for now!
			return result;
		}
		else {
			return value;
		}
	}

	public final SessionImplementor getSession() {
		return session;
	}

	public final Set getUninitializedFields() {
		return uninitializedFields;
	}

	public final String getEntityName() {
		return entityName;
	}

	public final boolean isInitializing() {
		return initializing;
	}
}
