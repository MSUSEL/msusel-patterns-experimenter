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

import org.hsqldb.result.Result;

class RecoverableOdbcFailure extends Exception {
    private String clientMessage = null;
    private String sqlStateCode = null;
    private Result errorResult = null;
    public String getSqlStateCode() {
        return sqlStateCode;
    }
    public Result getErrorResult() {
        return errorResult;
    }
    public RecoverableOdbcFailure(Result errorResult) {
        this.errorResult = errorResult;
    }
    /**
     * This constructor purposefully means that both server-side and
     * client-side message will be set to the specified message.
     */
    public RecoverableOdbcFailure(String m) {
        super(m);
        clientMessage = m;
    }
    /**
     * This constructor purposefully means that both server-side and
     * client-side message will be set to the specified message.
     * <P><B>
     * Note:  The parameters DO NOT SPECIFY server-side and client-side
     * messages.  Use the 3-parameter constructor for that.
     * </B></P>
     *
     * @see #RecoverableOdbcFailure(String, String, String)
     */
    public RecoverableOdbcFailure(String m, String sqlStateCode) {
        this(m);
        this.sqlStateCode = sqlStateCode;
    }
    /**
     * Set any parameter to null to skip the specified reporting.
     */
    public RecoverableOdbcFailure(
    String ourMessage, String clientMessage, String sqlStateCode) {
        super(ourMessage);
        this.clientMessage = clientMessage;
        this.sqlStateCode = sqlStateCode;
    }
    public String getClientMessage() {
        return clientMessage;
    }
}
