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

import com.jaspersoft.ireport.designer.IReportConnection;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

/**
 *
 * @author gtoffoli
 */
public class SimpleSQLDataSource implements javax.sql.DataSource {

    private IReportConnection iRConnection = null;
    private PrintWriter pw = new PrintWriter(System.out);
    private int loginTimeout = 0;

    public SimpleSQLDataSource(IReportConnection c)
    {
        iRConnection = c;
    }
    
    public Connection getConnection() throws SQLException {
        return iRConnection.getConnection();
    }

    public Connection getConnection(String username, String password) throws SQLException {
        // Ignoring username and password
        return getConnection();
    }

    public PrintWriter getLogWriter() throws SQLException {
        return pw;
    }

    public void setLogWriter(PrintWriter out) throws SQLException {
        pw = out;
    }

    public void setLoginTimeout(int seconds) throws SQLException {
        loginTimeout = seconds;
    }

    public int getLoginTimeout() throws SQLException {
        return loginTimeout;
    }

    public <T> T unwrap(Class<T> iface) throws SQLException {
        return null;
    }

    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }

    /**
     * @return the iRConnection
     */
    public IReportConnection getIRConnection() {
        return iRConnection;
    }

    /**
     * @param iRConnection the iRConnection to set
     */
    public void setIRConnection(IReportConnection iRConnection) {
        this.iRConnection = iRConnection;
    }

}
