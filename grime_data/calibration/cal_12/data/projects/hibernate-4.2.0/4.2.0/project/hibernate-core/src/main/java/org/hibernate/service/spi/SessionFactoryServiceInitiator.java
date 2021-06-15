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
package org.hibernate.service.spi;

import org.hibernate.cfg.Configuration;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.metamodel.source.MetadataImplementor;
import org.hibernate.service.Service;

/**
 * Contract for an initiator of services that target the specialized service registry
 * {@link SessionFactoryServiceRegistry}
 *
 * @author Steve Ebersole
 */
public interface SessionFactoryServiceInitiator<R extends Service> extends ServiceInitiator<R>{
	/**
	 * Initiates the managed service.
	 * <p/>
	 * Note for implementors: signature is guaranteed to change once redesign of SessionFactory building is complete
	 *
	 * @param sessionFactory The session factory.  Note the the session factory is still in flux; care needs to be taken
	 * in regards to what you call.
	 * @param configuration The configuration.
	 * @param registry The service registry.  Can be used to locate services needed to fulfill initiation.
	 *
	 * @return The initiated service.
	 */
	public R initiateService(SessionFactoryImplementor sessionFactory, Configuration configuration, ServiceRegistryImplementor registry);

	/**
	 * Initiates the managed service.
	 * <p/>
	 * Note for implementors: signature is guaranteed to change once redesign of SessionFactory building is complete
	 *
	 * @param sessionFactory The session factory.  Note the the session factory is still in flux; care needs to be taken
	 * in regards to what you call.
	 * @param metadata The configuration.
	 * @param registry The service registry.  Can be used to locate services needed to fulfill initiation.
	 *
	 * @return The initiated service.
	 */
	public R initiateService(SessionFactoryImplementor sessionFactory, MetadataImplementor metadata, ServiceRegistryImplementor registry);

}
