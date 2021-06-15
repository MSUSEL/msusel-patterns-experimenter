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
package com.jaspersoft.ireport.designer.connection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import org.hibernate.HibernateException;
import org.hibernate.cfg.Environment;

/**
 *
 * @author gtoffoli
 */
public class HibernateConnectionProvider implements org.hibernate.connection.ConnectionProvider {

    Connection conn = null;
    int useCounter = 0;
    JDBCConnection irConnection = null;


    public void configure(Properties props) throws HibernateException {
        irConnection = new JDBCConnection();
        irConnection.setJDBCDriver( props.getProperty(Environment.DRIVER));
        irConnection.setUsername( props.getProperty(Environment.USER));
        irConnection.setPassword( props.getProperty(Environment.PASS));
        irConnection.setUrl( props.getProperty(Environment.URL));
    }

    public Connection getConnection() throws SQLException {
        try {

            if (conn == null || conn.isClosed())
            {
                conn = irConnection.getConnection();
                useCounter=1;
            }
        } catch (SQLException sqlEx)
        {
            throw sqlEx;
        } catch (Exception ex)
        {
            throw new SQLException(ex.getMessage());
        }
        return conn;
    }

    public void closeConnection(Connection c) throws SQLException {
        if (conn != null && useCounter==1)
        {
            conn.close();
        }
        useCounter--;

    }

    public void close() throws HibernateException {

    }

    public boolean supportsAggressiveRelease() {
        return false;
    }

}
