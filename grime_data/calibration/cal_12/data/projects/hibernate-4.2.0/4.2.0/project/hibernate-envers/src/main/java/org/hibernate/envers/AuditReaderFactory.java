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
package org.hibernate.envers;

import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.envers.event.EnversListener;
import org.hibernate.envers.exception.AuditException;
import org.hibernate.envers.reader.AuditReaderImpl;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.event.spi.PostInsertEventListener;

/**
 * @author Adam Warski (adam at warski dot org)
 */
public class AuditReaderFactory {
    private AuditReaderFactory() { }

    /**
     * Create an audit reader associated with an open session.
     * @param session An open session.
     * @return An audit reader associated with the given sesison. It shouldn't be used
     * after the session is closed.
     * @throws AuditException When the given required listeners aren't installed.
     */
    public static AuditReader get(Session session) throws AuditException {
        SessionImplementor sessionImpl;
		if (!(session instanceof SessionImplementor)) {
			sessionImpl = (SessionImplementor) session.getSessionFactory().getCurrentSession();
		} else {
			sessionImpl = (SessionImplementor) session;
		}

		// todo : I wonder if there is a better means to do this via "named lookup" based on the session factory name/uuid
		final EventListenerRegistry listenerRegistry = sessionImpl
				.getFactory()
				.getServiceRegistry()
				.getService( EventListenerRegistry.class );

		for ( PostInsertEventListener listener : listenerRegistry.getEventListenerGroup( EventType.POST_INSERT ).listeners() ) {
			if ( listener instanceof EnversListener ) {
				// todo : slightly different from original code in that I am not checking the other listener groups...
				return new AuditReaderImpl(
						( (EnversListener) listener ).getAuditConfiguration(),
						session,
						sessionImpl
				);
			}
		}

        throw new AuditException( "Envers listeners were not properly registered" );
    }

    /**
     * Create an audit reader associated with an open entity manager.
     * @param entityManager An open entity manager.
     * @return An audit reader associated with the given entity manager. It shouldn't be used
     * after the entity manager is closed.
     * @throws AuditException When the given entity manager is not based on Hibernate, or if the required
     * listeners aren't installed.
     */
    public static AuditReader get(EntityManager entityManager) throws AuditException {
        if (entityManager.getDelegate() instanceof Session) {
            return get((Session) entityManager.getDelegate());
        }

        if (entityManager.getDelegate() instanceof EntityManager) {
            return get((EntityManager) entityManager.getDelegate());
        }

        throw new AuditException("Hibernate EntityManager not present!");
    }
}
