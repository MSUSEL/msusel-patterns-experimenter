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
package org.archive.httpclient;

import java.util.logging.Logger;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpConnection;
import org.apache.commons.httpclient.HttpMethod;
import org.archive.util.HttpRecorder;


/**
 * This class encapsulates the specializations supplied by the
 * overrides {@link HttpRecorderGetMethod} and {@link HttpRecorderPostMethod}.
 * 
 * It keeps instance of HttpRecorder and HttpConnection.
 *
 * @author stack
 * @version $Revision: 3351 $, $Date: 2005-04-07 21:44:47 +0000 (Thu, 07 Apr 2005) $
 */
public class HttpRecorderMethod {
    protected static Logger logger =
        Logger.getLogger(HttpRecorderMethod.class.getName());
    
    /**
     * Instance of http recorder we're using recording this http get.
     */
    private HttpRecorder httpRecorder = null;

    /**
     * Save around so can force close.
     *
     * See [ 922080 ] IllegalArgumentException (size is wrong).
     * https://sourceforge.net/tracker/?func=detail&aid=922080&group_id=73833&atid=539099
     */
    private HttpConnection connection = null;
    

	public HttpRecorderMethod(HttpRecorder recorder) {
        this.httpRecorder = recorder;
	}

	public void markContentBegin(HttpConnection c) {
        if (c != this.connection) {
            // We're checking that we're not being asked to work on
            // a connection that is other than the one we started
            // this method#execute with.
            throw new IllegalArgumentException("Connections differ: " +
                this.connection + " " + c + " " +
                Thread.currentThread().getName());
        }
		this.httpRecorder.markContentBegin();
	}
    
    /**
     * @return Returns the connection.
     */
    public HttpConnection getConnection() {
        return this.connection;
    }
    
    /**
     * @param connection The connection to set.
     */
    public void setConnection(HttpConnection connection) {
        this.connection = connection;
    }
    /**
     * @return Returns the httpRecorder.
     */
    public HttpRecorder getHttpRecorder() {
        return httpRecorder;
    }

    /**
     * If a 'Proxy-Connection' header has been added to the request,
     * it'll be of a 'keep-alive' type.  Until we support 'keep-alives',
     * override the Proxy-Connection setting and instead pass a 'close'
     * (Otherwise every request has to timeout before we notice
     * end-of-document).
     * @param method Method to find proxy-connection header in.
     */
    public void handleAddProxyConnectionHeader(HttpMethod method) {
        Header h = method.getRequestHeader("Proxy-Connection");
        if (h != null) {
            h.setValue("close");
            method.setRequestHeader(h);
        }
    }
}
