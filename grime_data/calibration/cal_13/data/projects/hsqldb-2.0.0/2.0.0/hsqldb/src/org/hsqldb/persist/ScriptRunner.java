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

package org.hsqldb.persist;

import java.io.EOFException;

import org.hsqldb.Database;
import org.hsqldb.HsqlNameManager.HsqlName;
import org.hsqldb.Session;
import org.hsqldb.Statement;
import org.hsqldb.StatementDML;
import org.hsqldb.StatementTypes;
import org.hsqldb.error.Error;
import org.hsqldb.error.ErrorCode;
import org.hsqldb.lib.IntKeyHashMap;
import org.hsqldb.lib.StopWatch;
import org.hsqldb.result.Result;
import org.hsqldb.result.ResultProperties;
import org.hsqldb.scriptio.ScriptReaderBase;
import org.hsqldb.scriptio.ScriptReaderDecode;
import org.hsqldb.scriptio.ScriptReaderText;

/**
 * Restores the state of a Database instance from an SQL log file. <p>
 *
 * If there is an error, processing stops at that line and the message is
 * logged to the application log. If memory runs out, an exception is thrown.
 *
 * @author Fred Toussi (fredt@users dot sourceforge.net)
 * @version 1.9.0
 * @since 1.7.2
 */
public class ScriptRunner {

    /**
     *  This is used to read the *.log file and manage any necessary
     *  transaction rollback.
     */
    public static void runScript(Database database, String logFilename) {

        IntKeyHashMap sessionMap = new IntKeyHashMap();
        Session       current    = null;
        int           currentId  = 0;

        database.setReferentialIntegrity(false);

        ScriptReaderBase scr = null;
        String           statement;
        int              statementType;
        Statement dummy = new StatementDML(StatementTypes.UPDATE_CURSOR,
                                           StatementTypes.X_SQL_DATA_CHANGE,
                                           null);

        try {
            StopWatch sw     = new StopWatch();
            Crypto    crypto = database.logger.getCrypto();

            if (crypto == null) {
                scr = new ScriptReaderText(database, logFilename);
            } else {
                scr = new ScriptReaderDecode(database, logFilename, crypto,
                                             true);
            }

            while (scr.readLoggedStatement(current)) {
                int sessionId = scr.getSessionNumber();

                if (current == null || currentId != sessionId) {
                    currentId = sessionId;
                    current   = (Session) sessionMap.get(currentId);

                    if (current == null) {
                        current =
                            database.getSessionManager().newSession(database,
                                database.getUserManager().getSysUser(), false,
                                true, null, 0);

                        sessionMap.put(currentId, current);
                    }
                }

                if (current.isClosed()) {
                    sessionMap.remove(currentId);

                    continue;
                }

                Result result = null;

                statementType = scr.getStatementType();

                switch (statementType) {

                    case ScriptReaderBase.ANY_STATEMENT :
                        statement = scr.getLoggedStatement();
                        result = current.executeDirectStatement(statement,
                                ResultProperties.defaultPropsValue);

                        if (result != null && result.isError()) {
                            if (result.getException() != null) {
                                throw result.getException();
                            }

                            throw Error.error(result);
                        }
                        break;

                    case ScriptReaderBase.COMMIT_STATEMENT :
                        current.commit(false);
                        break;

                    case ScriptReaderBase.INSERT_STATEMENT : {
                        current.sessionContext.currentStatement = dummy;

                        current.beginAction(dummy);

                        Object[] data = scr.getData();

                        scr.getCurrentTable().insertNoCheckFromLog(current,
                                data);
                        current.endAction(Result.updateOneResult);

                        break;
                    }
                    case ScriptReaderBase.DELETE_STATEMENT : {
                        current.sessionContext.currentStatement = dummy;

                        current.beginAction(dummy);

                        Object[] data = scr.getData();

                        scr.getCurrentTable().deleteNoCheckFromLog(current,
                                data);
                        current.endAction(Result.updateOneResult);

                        break;
                    }
                    case ScriptReaderBase.SET_SCHEMA_STATEMENT : {
                        HsqlName name =
                            database.schemaManager.findSchemaHsqlName(
                                scr.getCurrentSchema());

                        current.setCurrentSchemaHsqlName(name);

                        break;
                    }
                    case ScriptReaderBase.SESSION_ID : {
                        break;
                    }
                }

                if (current.isClosed()) {
                    sessionMap.remove(currentId);
                }
            }
        } catch (Throwable e) {

            // catch out-of-memory errors and terminate
            if (e instanceof EOFException) {

                // end of file - normal end
            } else if (e instanceof OutOfMemoryError) {
                database.logger.logSevereEvent("out of memory processing "
                                               + logFilename + " line: "
                                               + scr.getLineNumber(), e);

                throw Error.error(ErrorCode.OUT_OF_MEMORY);
            } else {

                // stop processing on bad script line
                database.logger.logSevereEvent(logFilename + " line: "
                                               + scr.getLineNumber(), e);
            }
        } finally {
            if (scr != null) {
                scr.close();
            }

            database.getSessionManager().closeAllSessions();
            database.setReferentialIntegrity(true);
        }
    }
}
