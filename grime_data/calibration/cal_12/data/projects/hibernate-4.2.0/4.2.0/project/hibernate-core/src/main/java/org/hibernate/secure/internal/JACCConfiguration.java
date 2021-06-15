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
package org.hibernate.secure.internal;

import java.util.StringTokenizer;
import javax.security.jacc.EJBMethodPermission;
import javax.security.jacc.PolicyConfiguration;
import javax.security.jacc.PolicyConfigurationFactory;
import javax.security.jacc.PolicyContextException;

import org.jboss.logging.Logger;

import org.hibernate.HibernateException;
import org.hibernate.internal.CoreMessageLogger;

/**
 * Adds Hibernate permissions to roles via JACC
 *
 * @author Gavin King
 */
public class JACCConfiguration {

    private static final CoreMessageLogger LOG = Logger.getMessageLogger(CoreMessageLogger.class, JACCConfiguration.class.getName());

	private final PolicyConfiguration policyConfiguration;

	public JACCConfiguration(String contextId) throws HibernateException {
		try {
			policyConfiguration = PolicyConfigurationFactory
					.getPolicyConfigurationFactory()
					.getPolicyConfiguration( contextId, false );
		}
		catch (ClassNotFoundException cnfe) {
			throw new HibernateException( "JACC provider class not found", cnfe );
		}
		catch (PolicyContextException pce) {
			throw new HibernateException( "policy context exception occurred", pce );
		}
	}

	public void addPermission(String role, String entityName, String action) {

		if ( action.equals( "*" ) ) {
			action = "insert,read,update,delete";
		}

		StringTokenizer tok = new StringTokenizer( action, "," );

		while ( tok.hasMoreTokens() ) {
			String methodName = tok.nextToken().trim();
			EJBMethodPermission permission = new EJBMethodPermission(
					entityName,
					methodName,
					null, // interfaces
					null // arguments
				);

            LOG.debugf("Adding permission to role %s: %s", role, permission);
			try {
				policyConfiguration.addToRole( role, permission );
			}
			catch (PolicyContextException pce) {
				throw new HibernateException( "policy context exception occurred", pce );
			}
		}
	}
}
