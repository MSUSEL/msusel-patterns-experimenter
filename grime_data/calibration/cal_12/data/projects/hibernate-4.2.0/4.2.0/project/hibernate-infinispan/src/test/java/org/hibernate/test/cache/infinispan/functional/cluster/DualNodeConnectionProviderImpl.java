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
package org.hibernate.test.cache.infinispan.functional.cluster;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.service.UnknownUnwrapTypeException;
import org.hibernate.service.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.service.spi.Configurable;
import org.hibernate.service.spi.Stoppable;
import org.hibernate.testing.env.ConnectionProviderBuilder;

/**
 * A {@link ConnectionProvider} implementation adding JTA-style transactionality around the returned
 * connections using the {@link DualNodeJtaTransactionManagerImpl}.
 * 
 * @author Brian Stansberry
 */
public class DualNodeConnectionProviderImpl implements ConnectionProvider, Configurable {
   private static ConnectionProvider actualConnectionProvider = ConnectionProviderBuilder.buildConnectionProvider();
   private String nodeId;
   private boolean isTransactional;

	@Override
	public boolean isUnwrappableAs(Class unwrapType) {
		return DualNodeConnectionProviderImpl.class.isAssignableFrom( unwrapType ) ||
				ConnectionProvider.class.isAssignableFrom( unwrapType );
	}

	@Override
	@SuppressWarnings( {"unchecked"})
	public <T> T unwrap(Class<T> unwrapType) {
		if ( DualNodeConnectionProviderImpl.class.isAssignableFrom( unwrapType ) ) {
			return (T) this;
		}
		else if ( ConnectionProvider.class.isAssignableFrom( unwrapType ) ) {
			return (T) actualConnectionProvider;
		}
		else {
			throw new UnknownUnwrapTypeException( unwrapType );
		}
	}

   public static ConnectionProvider getActualConnectionProvider() {
      return actualConnectionProvider;
   }

   public void setNodeId(String nodeId) throws HibernateException {
      if (nodeId == null) {
         throw new HibernateException( "nodeId not configured" );
	  }
	  this.nodeId = nodeId;
   }

   public Connection getConnection() throws SQLException {
      DualNodeJtaTransactionImpl currentTransaction = DualNodeJtaTransactionManagerImpl
               .getInstance(nodeId).getCurrentTransaction();
      if (currentTransaction == null) {
         isTransactional = false;
         return actualConnectionProvider.getConnection();
      } else {
         isTransactional = true;
         Connection connection = currentTransaction.getEnlistedConnection();
         if (connection == null) {
            connection = actualConnectionProvider.getConnection();
            currentTransaction.enlistConnection(connection);
         }
         return connection;
      }
   }

   public void closeConnection(Connection conn) throws SQLException {
      if (!isTransactional) {
         conn.close();
      }
   }

   public void close() throws HibernateException {
	   if ( actualConnectionProvider instanceof Stoppable ) {
		   ( ( Stoppable ) actualConnectionProvider ).stop();
	   }
   }

   public boolean supportsAggressiveRelease() {
      return true;
   }

	@Override
	public void configure(Map configurationValues) {
		nodeId = (String) configurationValues.get( "nodeId" );
	}
}
