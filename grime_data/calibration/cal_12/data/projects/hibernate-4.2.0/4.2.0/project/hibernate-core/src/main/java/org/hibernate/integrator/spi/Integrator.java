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
package org.hibernate.integrator.spi;

import org.hibernate.cfg.Configuration;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.metamodel.source.MetadataImplementor;
import org.hibernate.service.spi.SessionFactoryServiceRegistry;

/**
 * Contract for stuff that integrates with Hibernate.
 * <p/>
 * IMPL NOTE: called during session factory initialization (constructor), so not all parts of the passed session factory
 * will be available.
 * <p/>
 * For more information, see the following jiras:<ul>
 *     <li><a href="https://hibernate.onjira.com/browse/HHH-5562">HHH-5562</a></li>
 *     <li><a href="https://hibernate.onjira.com/browse/HHH-6081">HHH-6081</a></li>
 * </ul>
 *
 * @author Steve Ebersole
 * @since 4.0
 *
 * @todo : the signature here *will* change, guaranteed
 * @todo : better name ?
 */
public interface Integrator {

	/**
	 * Perform integration.
	 *
	 * @param configuration The configuration used to create the session factory
	 * @param sessionFactory The session factory being created
	 * @param serviceRegistry The session factory's service registry
	 */
	public void integrate(
			Configuration configuration,
			SessionFactoryImplementor sessionFactory,
			SessionFactoryServiceRegistry serviceRegistry);

	/**
     * Perform integration.
     *
     * @param metadata The metadata used to create the session factory
     * @param sessionFactory The session factory being created
     * @param serviceRegistry The session factory's service registry
     */
    public void integrate( MetadataImplementor metadata,
                           SessionFactoryImplementor sessionFactory,
                           SessionFactoryServiceRegistry serviceRegistry );

	/**
	 * Tongue-in-cheek name for a shutdown callback.
	 *
	 * @param sessionFactory The session factory being closed.
	 * @param serviceRegistry That session factory's service registry
	 */
	public void disintegrate(SessionFactoryImplementor sessionFactory, SessionFactoryServiceRegistry serviceRegistry);

}
