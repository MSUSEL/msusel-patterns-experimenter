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
package org.hibernate.metamodel.source.internal;

import java.io.Serializable;

import org.hibernate.EmptyInterceptor;
import org.hibernate.Interceptor;
import org.hibernate.ObjectNotFoundException;
import org.hibernate.SessionFactory;
import org.hibernate.internal.SessionFactoryImpl;
import org.hibernate.metamodel.SessionFactoryBuilder;
import org.hibernate.metamodel.source.MetadataImplementor;
import org.hibernate.proxy.EntityNotFoundDelegate;

/**
 * @author Gail Badner
 */
public class SessionFactoryBuilderImpl implements SessionFactoryBuilder {
	SessionFactoryOptionsImpl options;

	private final MetadataImplementor metadata;

	/* package-protected */
	SessionFactoryBuilderImpl(MetadataImplementor metadata) {
		this.metadata = metadata;
		options = new SessionFactoryOptionsImpl();
	}

	@Override
	public SessionFactoryBuilder with(Interceptor interceptor) {
		this.options.interceptor = interceptor;
		return this;
	}

	@Override
	public SessionFactoryBuilder with(EntityNotFoundDelegate entityNotFoundDelegate) {
		this.options.entityNotFoundDelegate = entityNotFoundDelegate;
		return this;
	}

	@Override
	public SessionFactory buildSessionFactory() {
		return new SessionFactoryImpl(metadata, options, null );
	}

	private static class SessionFactoryOptionsImpl implements SessionFactory.SessionFactoryOptions {
		private Interceptor interceptor = EmptyInterceptor.INSTANCE;

		// TODO: should there be a DefaultEntityNotFoundDelegate.INSTANCE?
		private EntityNotFoundDelegate entityNotFoundDelegate = new EntityNotFoundDelegate() {
				public void handleEntityNotFound(String entityName, Serializable id) {
					throw new ObjectNotFoundException( id, entityName );
				}
		};

		@Override
		public Interceptor getInterceptor() {
			return interceptor;
		}

		@Override
		public EntityNotFoundDelegate getEntityNotFoundDelegate() {
			return entityNotFoundDelegate;
		}
	}
}