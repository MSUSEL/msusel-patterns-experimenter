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

import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hsqldb.Session;
import org.hsqldb.result.Result;
import org.hsqldb.result.ResultConstants;
import org.hsqldb.result.ResultProperties;

class OdbcPreparedStatement {

    public String  handle, query;
    public Result  ackResult;
    public Session session;
    private Map    containingMap;
    private List   portals = new ArrayList();

    protected OdbcPreparedStatement(OdbcPreparedStatement other) {
        this.handle    = other.handle;
        this.ackResult = other.ackResult;
    }

    /**
     * Instantiates an proxy OdbcPreparedStatement object for the
     * Connection Session, and adds the new instance to the specified map.
     */
    public OdbcPreparedStatement(String handle, String query,
                                 Map containingMap,
                                 Session session)
                                 throws RecoverableOdbcFailure {

        this.handle        = handle;
        this.query         = query;
        this.containingMap = containingMap;
        this.session       = session;

        Result psResult = Result.newPrepareStatementRequest();

        psResult.setPrepareOrExecuteProperties(
            query, 0, 0, 0, 0,ResultProperties.defaultPropsValue,
            Statement.NO_GENERATED_KEYS, null, null);

        ackResult = session.execute(psResult);

        switch (ackResult.getType()) {

            case ResultConstants.PREPARE_ACK :
                break;

            case ResultConstants.ERROR :
                throw new RecoverableOdbcFailure(ackResult);
            default :
                throw new RecoverableOdbcFailure(
                    "Output Result from Statement prep is of "
                    + "unexpected type: " + ackResult.getType());
        }

        containingMap.put(handle, this);
    }

    /**
     * Releases resources for this instance and all associated StatementPortals,
     * and removes this instance from the containing map.
     */
    public void close() {

        // TODO:  Free up resources!
        containingMap.remove(handle);

        while (portals.size() > 0) {
            ((StatementPortal) portals.remove(1)).close();
        }
    }

    /**
     * Associates an StatementPortal withwith OdbcPreparedStatement.
     */
    public void addPortal(StatementPortal portal) {
        portals.add(portal);
    }
}
