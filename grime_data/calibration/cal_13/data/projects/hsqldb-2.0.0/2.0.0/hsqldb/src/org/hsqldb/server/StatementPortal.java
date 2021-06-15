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

package org.hsqldb.server;

import java.util.Map;

import org.hsqldb.Session;
import org.hsqldb.result.Result;
import org.hsqldb.result.ResultConstants;

class StatementPortal {
    public Object[] parameters;
    public Result bindResult, ackResult;
    public String lcQuery;
    public String handle;
    private Map containingMap;
    private Session session;

    /**
     * Convenience wrapper for the 3-param constructor.
     *
     * @see #StatementPortal(String, OdbcPreparedStatement, Object[], Map)
     */
    public StatementPortal(String handle,
    OdbcPreparedStatement odbcPs, Map containingMap)
    throws RecoverableOdbcFailure {
        this(handle, odbcPs, new Object[0], containingMap);
    }

    /**
     * Instantiates a proxy ODBC StatementPortal object for the
     * Connection Session, and adds the new instance to the specified map.
     *
     * @param paramObjs Param values are either String or BinaryData instances
     */
    public StatementPortal(String handle, OdbcPreparedStatement odbcPs,
    Object[] paramObjs, Map containingMap) throws RecoverableOdbcFailure {
        this.handle = handle;
        lcQuery = odbcPs.query.toLowerCase();
        ackResult = odbcPs.ackResult;
        session = odbcPs.session;
        this.containingMap = containingMap;
        bindResult = Result.newPreparedExecuteRequest(
            odbcPs.ackResult.parameterMetaData.getParameterTypes(),
            odbcPs.ackResult.getStatementID());
        switch (bindResult.getType()) {
            case ResultConstants.EXECUTE:
                break;
            case ResultConstants.ERROR:
                throw new RecoverableOdbcFailure(bindResult);
            default:
                throw new RecoverableOdbcFailure(
                    "Output Result from seconary Statement prep is of "
                    + "unexpected type: " + bindResult.getType());
        }
        if (paramObjs.length < 1) {
            parameters = new Object[0];
        } else {
            org.hsqldb.result.ResultMetaData pmd =
                odbcPs.ackResult.parameterMetaData;
            if (pmd == null) {
                throw new RecoverableOdbcFailure("No metadata for Result ack");
            }
            org.hsqldb.types.Type[] paramTypes = pmd.getParameterTypes();
            if (paramTypes.length != paramObjs.length) {
                throw new RecoverableOdbcFailure(null,
                    "Client didn't specify all " + paramTypes.length
                    + " parameters (" + paramObjs.length + ')', "08P01");
            }
            parameters = new Object[paramObjs.length];
            try {
                for (int i = 0; i < parameters.length; i++) {
                    parameters[i] = (paramObjs[i] instanceof String)
                        ? PgType.getPgType(paramTypes[i], true)
                            .getParameter((String) paramObjs[i], session)
                        : paramObjs[i];
                }
            } catch (java.sql.SQLException se) {
                throw new RecoverableOdbcFailure("Typing failure: " + se);
            }
        }
        containingMap.put(handle, this);
    }

    /**
     * Releases resources for this instance
     * and removes this instance from the containing map.
     */
    public void close() {
        // TODO:  Free up resources!
        containingMap.remove(handle);
    }
}
