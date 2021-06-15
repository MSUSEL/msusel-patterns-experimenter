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

package org.hsqldb;

import org.hsqldb.result.Result;
import org.hsqldb.error.Error;
import org.hsqldb.error.ErrorCode;

/**
 * Class encapsulating all exceptions that can be thrown within the engine.
 * Instances are used to create instances of java.sql.SQLException and returned
 * to JDBC callers.
 *
 * @author Fred Toussi (fredt@users dot sourceforge.net)
 * @version 1.9.0
 * @since 1.7.2
 */
public class HsqlException extends RuntimeException {

    //
    public final static HsqlException[] emptyArray = new HsqlException[]{};
    public final static HsqlException noDataCondition =
        Error.error(ErrorCode.N_02000);

    //
    private String message;
    private String state;
    private int    code;
    private int    level;
    private int    statementGroup;
    private int    statementCode;

    /**
     * @param message String
     * @param state XOPEN / SQL code for exception
     * @param code number code in HSQLDB
     */
    public HsqlException(Throwable t, String message, String state, int code) {

        super(t);

        this.message = message;
        this.state   = state;
        this.code    = code;
    }

    /**
     * @param r containing the members
     */
    public HsqlException(Result r) {

        this.message = r.getMainString();
        this.state   = r.getSubString();
        this.code    = r.getErrorCode();
    }

    public HsqlException(Throwable t, String errorState, int errorCode) {

        super(t);

        this.message = t.toString();
        this.state   = errorState;
        this.code    = errorCode;
    }

    /**
     * @return message
     */
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * @return SQL State
     */
    public String getSQLState() {
        return state;
    }

    /**
     * @return vendor specific error code
     */
    public int getErrorCode() {
        return code;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

    public int getStatementCode() {
        return statementCode;
    }

    public void setStatementType(int group, int code) {
        statementGroup = group;
        statementCode  = code;
    }

    public static class HsqlRuntimeMemoryError extends OutOfMemoryError {
        HsqlRuntimeMemoryError() {}
    }

    public int hashCode() {
        return code;
    }

    public boolean equals(Object other) {

        if (other instanceof HsqlException) {
            HsqlException o = (HsqlException) other;

            return code == o.code && equals(state, o.state)
                   && equals(message, o.message);
        }

        return false;
    }

    private static boolean equals(Object a, Object b) {

        if (a == b) {
            return true;
        }

        if (a == null || b == null) {
            return false;
        }

        return a.equals(b);
    }
}
