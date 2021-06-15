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
package org.hibernate.ejb.test.ejb3configuration;

import javax.persistence.EntityManagerFactory;

import org.junit.Assert;
import org.junit.Test;

import org.hibernate.SessionFactory;
import org.hibernate.SessionFactoryObserver;
import org.hibernate.ejb.AvailableSettings;
import org.hibernate.ejb.Ejb3Configuration;

/**
 * @author Emmanuel Bernard <emmanuel@hibernate.org>
 */
public class SessionFactoryObserverTest {
    @Test
	public void testSessionFactoryObserverProperty() {
		Ejb3Configuration conf = new Ejb3Configuration();
		conf.setProperty( AvailableSettings.SESSION_FACTORY_OBSERVER, GoofySessionFactoryObserver.class.getName() );
		conf.addAnnotatedClass( Bell.class );
		try {
			final EntityManagerFactory entityManagerFactory = conf.buildEntityManagerFactory();
			entityManagerFactory.close();
            Assert.fail( "GoofyException should have been thrown" );
		}
		catch ( GoofyException e ) {
			//success
		}
	}

	public static class GoofySessionFactoryObserver implements SessionFactoryObserver {

		public void sessionFactoryCreated(SessionFactory factory) {
		}

		public void sessionFactoryClosed(SessionFactory factory) {
			throw new GoofyException();
		}
	}

	public static class GoofyException extends RuntimeException {

	}
}
