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

package org.hsqldb.jdbc.pool;

import java.sql.SQLException;
import java.sql.Connection;

/**
 * @author Jakob Jenkov
 */
public class ConnectionDefaults {

    private int     holdability          = -1;
    private int     transactionIsolation = -1;
    private boolean isAutoCommit         = true;
    private boolean isReadOnly           = false;
    private String  catalog              = null;

    public ConnectionDefaults() {
    }

    public ConnectionDefaults(Connection connection) throws SQLException {

        this.holdability          = connection.getHoldability();
        this.transactionIsolation = connection.getTransactionIsolation();
        this.isAutoCommit         = connection.getAutoCommit();
        this.isReadOnly           = connection.isReadOnly();
        this.catalog              = connection.getCatalog();
    }

    public int getHoldability() throws SQLException {
        return this.holdability;
    }

    public void setHoldability(int holdability) throws SQLException {
        this.holdability = holdability;
    }

    public int getTransactionIsolation() throws SQLException {
        return this.transactionIsolation;
    }

    public void setTransactionIsolation(int level) throws SQLException {
        this.transactionIsolation = level;
    }

    public boolean getAutoCommit() throws SQLException {
        return this.isAutoCommit;
    }

    public void setAutoCommit(boolean autoCommit) throws SQLException {
        this.isAutoCommit = autoCommit;
    }

    public boolean isReadOnly() throws SQLException {
        return this.isReadOnly;
    }

    public void setReadOnly(boolean readOnly) throws SQLException {
        this.isReadOnly = readOnly;
    }

    public String getCatalog() throws SQLException {
        return this.catalog;
    }

    public void setCatalog(String catalog) throws SQLException {
        this.catalog = catalog;
    }

    public void setDefaults(Connection connection) throws SQLException {

        connection.setHoldability(this.holdability);

        if (this.transactionIsolation != Connection.TRANSACTION_NONE) {
            connection.setTransactionIsolation(this.transactionIsolation);
        }
        connection.setAutoCommit(this.isAutoCommit);
        connection.setReadOnly(this.isReadOnly);
        connection.setCatalog(this.catalog);
    }
}
