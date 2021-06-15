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
package org.hibernate.ejb.connection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import javax.sql.DataSource;

import org.jboss.logging.Logger;

import org.hibernate.HibernateException;
import org.hibernate.cfg.Environment;
import org.hibernate.ejb.internal.EntityManagerMessageLogger;
import org.hibernate.service.jdbc.connections.internal.DatasourceConnectionProviderImpl;

/**
 * A specialization of {@link DatasourceConnectionProviderImpl} which uses the {@link DataSource} specified vi
 * {@link #setDataSource} rather than locating it from JNDI.
 * <p/>
 * NOTE : {@link #setDataSource} must be called prior to {@link #configure}.
 * <p/>
 * TODO : could not find where #setDataSource is actually called.  Can't this just be passed in to #configure???
 *
 * @author Emmanuel Bernard
 */
public class InjectedDataSourceConnectionProvider extends DatasourceConnectionProviderImpl {

    private static final EntityManagerMessageLogger LOG = Logger.getMessageLogger(EntityManagerMessageLogger.class,
                                                                           InjectedDataSourceConnectionProvider.class.getName());

	private String user;
	private String pass;

	@Override
    public void setDataSource(DataSource ds) {
		super.setDataSource( ds );
	}

	public void configure(Properties props) throws HibernateException {
		user = props.getProperty( Environment.USER );
		pass = props.getProperty( Environment.PASS );

        if (getDataSource() == null) throw new HibernateException("No datasource provided");
        LOG.usingProvidedDataSource();
	}

	@Override
	public Connection getConnection() throws SQLException {
        if (user != null || pass != null) return getDataSource().getConnection(user, pass);
        return getDataSource().getConnection();
	}
}
