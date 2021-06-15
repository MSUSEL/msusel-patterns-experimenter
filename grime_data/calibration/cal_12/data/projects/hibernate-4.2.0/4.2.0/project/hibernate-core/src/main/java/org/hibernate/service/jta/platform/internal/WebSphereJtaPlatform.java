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
package org.hibernate.service.jta.platform.internal;

import java.lang.reflect.Method;
import javax.transaction.TransactionManager;
import javax.transaction.UserTransaction;

import org.jboss.logging.Logger;

import org.hibernate.internal.CoreMessageLogger;
import org.hibernate.service.jta.platform.spi.JtaPlatformException;

/**
 * JTA platform implementation for WebSphere (versions 4, 5.0 and 5.1)
 *
 * @author Gavin King
 * @author Steve Ebersole
 */
public class WebSphereJtaPlatform extends AbstractJtaPlatform {
    private static final CoreMessageLogger LOG = Logger.getMessageLogger(CoreMessageLogger.class, WebSphereJtaPlatform.class.getName());

	public static final String VERSION_5_UT_NAME = "java:comp/UserTransaction";
	public static final String VERSION_4_UT_NAME = "jta/usertransaction";

	private final Class transactionManagerAccessClass;
	private final int webSphereVersion;

	public WebSphereJtaPlatform() {
		try {
			Class clazz;
			int version;
			try {
				clazz = Class.forName( "com.ibm.ws.Transaction.TransactionManagerFactory" );
				version = 5;
                LOG.debug("WebSphere 5.1");
			}
			catch ( Exception e ) {
				try {
					clazz = Class.forName( "com.ibm.ejs.jts.jta.TransactionManagerFactory" );
					version = 5;
                    LOG.debug("WebSphere 5.0");
				}
				catch ( Exception e2 ) {
					clazz = Class.forName( "com.ibm.ejs.jts.jta.JTSXA" );
					version = 4;
                    LOG.debug("WebSphere 4");
				}
			}

			transactionManagerAccessClass = clazz;
			webSphereVersion = version;
		}
		catch ( Exception e ) {
			throw new JtaPlatformException( "Could not locate WebSphere TransactionManager access class", e );
		}
	}

	@Override
	protected TransactionManager locateTransactionManager() {
		try {
			final Method method = transactionManagerAccessClass.getMethod( "getTransactionManager" );
			return ( TransactionManager ) method.invoke( null );
		}
		catch ( Exception e ) {
			throw new JtaPlatformException( "Could not obtain WebSphere TransactionManager", e );
		}

	}

	@Override
	protected UserTransaction locateUserTransaction() {
		final String utName = webSphereVersion == 5 ? VERSION_5_UT_NAME : VERSION_4_UT_NAME;
		return (UserTransaction) jndiService().locate( utName );
	}
}
