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

package org.hsqldb.util;

import java.io.Serializable;
import java.util.Vector;

/**
 * Database Transfer Tool
 * @author Nicolas BAZIN, INGENICO
 * @version 1.7.0
 */
class DataAccessPoint implements Serializable {

    Traceable      tracer;
    TransferHelper helper;
    String         databaseToConvert;

    public DataAccessPoint() {

        tracer            = null;
        helper            = HelperFactory.getHelper("");
        databaseToConvert = "";
    }

    public DataAccessPoint(Traceable t) {

        tracer = t;
        helper = HelperFactory.getHelper("");

        helper.set(null, t, "\'");

        databaseToConvert = "";
    }

    boolean isConnected() {
        return false;
    }

    boolean getAutoCommit() throws DataAccessPointException {
        return false;
    }

    void commit() throws DataAccessPointException {}

    void rollback() throws DataAccessPointException {}

    void setAutoCommit(boolean flag) throws DataAccessPointException {}

    boolean execute(String statement) throws DataAccessPointException {
        return false;
    }

    TransferResultSet getData(String statement)
    throws DataAccessPointException {
        return null;
    }

    void putData(String statement, TransferResultSet r,
                 int iMaxRows) throws DataAccessPointException {}

    Vector getSchemas() throws DataAccessPointException {
        return new Vector();
    }

    Vector getCatalog() throws DataAccessPointException {
        return new Vector();
    }

    void setCatalog(String sCatalog) throws DataAccessPointException {}

    Vector getTables(String sCatalog,
                     String[] sSchemas) throws DataAccessPointException {
        return new Vector();
    }

    void getTableStructure(TransferTable SQLCommands,
                           DataAccessPoint Dest)
                           throws DataAccessPointException {
        throw new DataAccessPointException("Nothing to Parse");
    }

    void close() throws DataAccessPointException {}

    void beginDataTransfer() throws DataAccessPointException {

        try {
            helper.beginDataTransfer();
        } catch (Exception e) {
            throw new DataAccessPointException(e.getMessage());
        }
    }

    void endDataTransfer() throws DataAccessPointException {

        try {
            helper.endDataTransfer();
        } catch (Exception e) {
            throw new DataAccessPointException(e.getMessage());
        }
    }

    /**
     * @return Returns the helper.
     */
    public TransferHelper getHelper() {
        return helper;
    }
}
